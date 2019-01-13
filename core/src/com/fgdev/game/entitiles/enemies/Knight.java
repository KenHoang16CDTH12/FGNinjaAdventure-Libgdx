package com.fgdev.game.entitiles.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.BodyFactory;

import static com.fgdev.game.Constants.PPM;

public class Knight extends Enemy implements Pool.Poolable {

    public static final String TAG = Knight.class.getName();

    public enum State {
        IDLE, RUN, DEAD, JUMP, ATTACK, JUMP_ATTACK, WALK
    }

    private State currentState;
    private State previousState;

    private Animation knightIdle;
    private Animation knightRun;
    private Animation knightDead;
    private Animation knightAttack;
    private Animation knightJumpAttack;
    private Animation knightWalk;
    private Animation knightJump;

    private boolean isRun;
    private boolean isAttack;
    private boolean isSlide;

    public Knight(World world, ScoreIndicator scoreIndicator) {
        super(world, scoreIndicator);
        knightIdle = Assets.instance.knight.animIdle;
        knightRun = Assets.instance.knight.animRun;
        knightDead = Assets.instance.knight.animDead;
        knightAttack = Assets.instance.knight.animAttack;
        knightJumpAttack = Assets.instance.knight.animJumpAttack;
        knightWalk = Assets.instance.knight.animWalk;
        knightJump = Assets.instance.knight.animJump;
    }

    public void init(MapObject mapObject, int type) {
        this.mapObject = mapObject;
        this.type = type;
        currentState = State.IDLE;
        previousState = State.IDLE;
        isRun = true;
        isDead = false;
        isAttack = false;
        isSlide = false;
        speed = 1f;
        // Extend Abstract
        init();
        setRegion((TextureRegion) knightIdle.getKeyFrame(stateTimer));
    }

    @Override
    public void reset() {

    }

    public void update(float dt) {
        if (isRun) {
            running();
        }
        super.update(dt);
    }

    @Override
    protected void setBoundForRegion() {
        currentState = getState();
        switch (currentState) {
            case DEAD:
                setBounds(0, 0, 142 * 2  / PPM, 113 * 2 / PPM);
                break;
            case RUN:
            case ATTACK:
            case JUMP_ATTACK:
            case JUMP:
            case WALK:
            case IDLE:
            default:
                setBounds(0, 0, 88 * 2  / PPM, 106 * 2 / PPM);
                break;
        }
    }

    @Override
    protected TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        //depending on the state, get corresponding animation KeyFrame
        switch (currentState) {
            case RUN:
                region = (TextureRegion) knightRun.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = (TextureRegion) knightDead.getKeyFrame(stateTimer);
                break;
            case ATTACK:
                region = (TextureRegion) knightAttack.getKeyFrame(stateTimer, true);
                break;
            case JUMP_ATTACK:
                region = (TextureRegion) knightJumpAttack.getKeyFrame(stateTimer);
                break;
            case WALK:
                region = (TextureRegion) knightWalk.getKeyFrame(stateTimer);
                break;
            case JUMP:
                region = (TextureRegion) knightJump.getKeyFrame(stateTimer);
                break;
            case IDLE:
            default:
                region = (TextureRegion) knightIdle.getKeyFrame(stateTimer,true);
                break;
        }

        //if object is running left and the texture isnt facing left... flip it.
        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        //if object is running right and the texture isnt facing right... flip it.
        else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;

        return region;
    }

    private State getState() {
        if (isDead)
            return State.DEAD;
        else if (isAttack)
            return State.ATTACK;
        else if (body.getLinearVelocity().x != 0)
            return State.RUN;
           // if none of these return then he must be standing
        else
            return State.IDLE;
    }

    private void makeBoxRobotBody(float posx, float posy) {
        float width = (88 - 30)/ PPM;
        float height = (106 - 15) / PPM;
        // create knight
        body = bodyFactory.makeBoxPolyBody(
                posx,
                posy,
                width,
                height,
                BodyFactory.ENEMY_DISABLE_PLAYER,
                BodyDef.BodyType.DynamicBody,
                this
        );
        // create foot sensor
        bodyFactory.makeShapeSensor(body,
                width,
                10 / PPM,
                new Vector2(0, (-height - 60) / PPM),
                0,
                BodyFactory.ENEMY_SENSOR,
                this
        );
        // create keep shape
        bodyFactory.makeEdgeSensor(body,
                new Vector2(0, (-height - 80) / PPM),
                new Vector2(6.8f / PPM / 6, 6.8f / PPM * 3),
                BodyFactory.ENEMY,
                this
        );
    }

    protected void defineEnemy() {
        Rectangle rect = ((RectangleMapObject) mapObject).getRectangle();
        makeBoxRobotBody(
                (rect.x + rect.width / 2) / PPM,
                (rect.y + rect.height / 2) / PPM
        );
    }

    @Override
    public int score() {
        return 150;
    }

    @Override
    public void killed() {
        super.killed();
        setRegion((TextureRegion) knightDead.getKeyFrame(stateTimer));
        isDead = true;
        isRun = false;
        becomeDead();
    }

    @Override
    public void beginAttack(Player player) {
        setRegion((TextureRegion) knightAttack.getKeyFrame(stateTimer));
        player.playerDie();
        isAttack = true;
        isRun = false;
        body.getLinearVelocity().x = 0;
    }

    @Override
    public void endAttack(Player player) {
        setRegion((TextureRegion) knightIdle.getKeyFrame(stateTimer));
        isRun = true;
        isAttack = false;
    }
}
