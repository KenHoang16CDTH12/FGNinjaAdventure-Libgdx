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

public class Santa extends Enemy implements Pool.Poolable {

    public static final String TAG = Santa.class.getName();

    public enum State {
        IDLE, RUN, DEAD, JUMP, SLIDE, WALK
    }

    private State currentState;
    private State previousState;

    private Animation santaIdle;
    private Animation santaRun;
    private Animation santaDead;
    private Animation santaSlide;
    private Animation santaWalk;
    private Animation santaJump;

    private boolean isRun;
    private boolean isAttack;
    private boolean isSlide;


    public Santa(World world, ScoreIndicator scoreIndicator) {
        super(world, scoreIndicator);
        santaIdle = Assets.instance.santa.animIdle;
        santaRun = Assets.instance.santa.animRun;
        santaDead = Assets.instance.santa.animDead;
        santaSlide = Assets.instance.santa.animSlide;
        santaWalk = Assets.instance.santa.animWalk;
        santaJump = Assets.instance.santa.animJump;
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
        setRegion((TextureRegion) santaIdle.getKeyFrame(stateTimer));
    }

    @Override
    public void reset() {
        currentState = State.IDLE;
        previousState = State.IDLE;
        isRun = true;
        isDead = false;
        isAttack = false;
        isSlide = false;
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
        setBounds(0, 0, 103 * 2  / PPM, 71 * 2 / PPM);
    }

    @Override
    protected TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        //depending on the state, get corresponding animation KeyFrame
        switch (currentState) {
            case RUN:
                region = (TextureRegion) santaRun.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = (TextureRegion) santaDead.getKeyFrame(stateTimer);
                break;
            case SLIDE:
                region = (TextureRegion) santaSlide.getKeyFrame(stateTimer);
                break;
            case WALK:
                region = (TextureRegion) santaWalk.getKeyFrame(stateTimer, true);
                break;
            case JUMP:
                region = (TextureRegion) santaJump.getKeyFrame(stateTimer, true);
                break;
            case IDLE:
            default:
                region = (TextureRegion) santaIdle.getKeyFrame(stateTimer,true);
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
        else if (isAttack)
            return State.JUMP;
        else if (body.getLinearVelocity().x != 0)
            return State.RUN;
            // if none of these return then he must be standing
        else
            return State.IDLE;
    }

    private void makeBoxRobotBody(float posx, float posy) {
        float width = (103 - 70)/ PPM;
        float height = (71 - 10) / PPM;
        // create santa
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
                new Vector2(0, (-height - 50) / PPM),
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
        return 200;
    }

    @Override
    public void killed() {
        super.killed();
        setRegion((TextureRegion) santaDead.getKeyFrame(stateTimer));
        isDead = true;
        isRun = false;
        becomeDead();
    }

    @Override
    public void beginAttack(Player player) {
        setRegion((TextureRegion) santaJump.getKeyFrame(stateTimer));
        player.playerDie();
        isAttack = true;
        isRun = false;
        body.getLinearVelocity().x = 0;
    }

    @Override
    public void endAttack(Player player) {
        setRegion((TextureRegion) santaIdle.getKeyFrame(stateTimer));
        isRun = true;
        isAttack = false;
    }
}
