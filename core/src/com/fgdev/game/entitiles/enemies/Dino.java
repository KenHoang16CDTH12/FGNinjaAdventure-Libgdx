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
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.PPM;

public class Dino  extends Enemy implements Pool.Poolable {

    public static final String TAG = Dino.class.getName();

    public enum State {
        IDLE, RUN, DEAD, JUMP, WALK
    }

    private State currentState;
    private State previousState;

    private Animation dinoIdle;
    private Animation dinoRun;
    private Animation dinoDead;
    private Animation dinoWalk;
    private Animation dinoJump;

    private boolean isRun;
    private boolean isAttack;

    public Dino(World world, ScoreIndicator scoreIndicator) {
        super(world, scoreIndicator);
        dinoIdle = Assets.instance.dino.animIdle;
        dinoRun = Assets.instance.dino.animRun;
        dinoDead = Assets.instance.dino.animDead;
        dinoWalk = Assets.instance.dino.animWalk;
        dinoJump = Assets.instance.dino.animJump;
    }

    public void init(MapObject mapObject, int type) {
        this.mapObject = mapObject;
        this.type = type;
        currentState = State.IDLE;
        previousState = State.IDLE;
        isRun = true;
        isAttack = false;
        isDead = false;
        speed = 1f;
        // Extend Abstract
        init();
        setRegion((TextureRegion) dinoIdle.getKeyFrame(stateTimer));
    }

    @Override
    public void reset() {
        currentState = State.IDLE;
        previousState = State.IDLE;
        isRun = true;
        isDead = false;
        isAttack = false;
        speed = 1f;
    }

    public void update(float dt) {
        if (isRun) {
            running();
        }
        super.update(dt);
    }

    @Override
    protected void setBoundForRegion() {
        setBounds(0, 0, 136 * 2  / PPM, 94 * 2 / PPM);
    }

    @Override
    protected TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        //depending on the state, get corresponding animation KeyFrame
        switch (currentState) {
            case RUN:
                region = (TextureRegion) dinoRun.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = (TextureRegion) dinoDead.getKeyFrame(stateTimer);
                break;
            case WALK:
                region = (TextureRegion) dinoWalk.getKeyFrame(stateTimer, true);
                break;
            case JUMP:
                region = (TextureRegion) dinoJump.getKeyFrame(stateTimer, true);
                break;
            case IDLE:
            default:
                region = (TextureRegion) dinoIdle.getKeyFrame(stateTimer,true);
                break;
        }
        //if player is running left and the texture isnt facing left... flip it.
        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        //if player is running right and the texture isnt facing right... flip it.
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
        else if (body.getLinearVelocity().x != 0)
            return State.RUN;
        else if (isAttack)
            return State.JUMP;
            // if none of these return then he must be standing
        else
            return State.IDLE;
    }

    private void makeBoxRobotBody(float posx, float posy) {
        float width = (136 - 70)/ PPM;
        float height = (94 - 12) / PPM;
        // create dino
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
                new Vector2(0, (-height - 60) / PPM),
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
        return 500;
    }

    @Override
    public void killed() {
        setRegion((TextureRegion) dinoDead.getKeyFrame(stateTimer));
        isDead = true;
        isRun = false;
        becomeDead();
    }

    @Override
    public void beginAttack(Player player) {
        setRegion((TextureRegion) dinoJump.getKeyFrame(stateTimer));
        player.playerDie();
        isAttack = true;
        isRun = false;
        body.getLinearVelocity().x = 0;
    }

    @Override
    public void endAttack(Player player) {
        setRegion((TextureRegion) dinoIdle.getKeyFrame(stateTimer));
        isRun = true;
        isAttack = false;
    }
}
