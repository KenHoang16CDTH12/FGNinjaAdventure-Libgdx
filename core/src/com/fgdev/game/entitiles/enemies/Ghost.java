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

public class Ghost extends Enemy implements Pool.Poolable {

    public static final String TAG = Ghost.class.getName();

    public enum State {
        IDLE, RUN, DEAD, JUMP,
    }

    private State currentState;
    private State previousState;

    private Animation ghostIdle;
    private Animation ghostRun;
    private Animation ghostDead;
    private Animation ghostJump;

    private boolean isRun;

    public Ghost(World world, ScoreIndicator scoreIndicator) {
        super(world, scoreIndicator);
    }

    public void init(MapObject mapObject, int type) {
        this.mapObject = mapObject;
        this.type = type;
        currentState = State.IDLE;
        previousState = State.IDLE;
        isRun = true;
        isDead = false;
        speed = 2f;
        ghostIdle = Assets.instance.ghost.animIdle;
        ghostRun = Assets.instance.ghost.animRun;
        ghostDead = Assets.instance.ghost.animDead;
        ghostJump = Assets.instance.ghost.animJump;
        // Extend Abstract
        init();
        setRegion((TextureRegion) ghostIdle.getKeyFrame(stateTimer));
    }

    @Override
    public void reset() {
        currentState = State.IDLE;
        previousState = State.IDLE;
        isRun = true;
        isDead = false;
        ghostIdle = Assets.instance.ghost.animIdle;
        ghostRun = Assets.instance.ghost.animRun;
        ghostDead = Assets.instance.ghost.animDead;
        ghostJump = Assets.instance.ghost.animJump;
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
            case RUN:
                setBounds(0, 0, 60 * 2  / PPM, 89 * 2 / PPM);
                break;
            case JUMP:
                setBounds(0, 0, 69 * 2  / PPM, 94 * 2 / PPM);
                break;
            case DEAD:
                setBounds(0, 0, 105 * 2  / PPM, 102 * 2 / PPM);
                break;
            case IDLE:
            default:
                setBounds(0, 0, 58 * 2  / PPM, 88 * 2 / PPM);
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
                region = (TextureRegion) ghostRun.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = (TextureRegion) ghostDead.getKeyFrame(stateTimer);
                break;
            case JUMP:
                region = (TextureRegion) ghostIdle.getKeyFrame(stateTimer, true);
                break;
            case IDLE:
            default:
                region = (TextureRegion) ghostIdle.getKeyFrame(stateTimer, true);
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
        else
            return State.IDLE;
    }

    private void makeBoxody(float posx, float posy) {
        float width = (60 - 5) / PPM;
        float height = (90 - 5) / PPM;
        // create zombie
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
                new Vector2(0, (-height - 70) / PPM),
                0,
                BodyFactory.ENEMY_SENSOR,
                this
        );
        // create keep shape
        bodyFactory.makeEdgeSensor(body,
                new Vector2(0, (-height - 70) / PPM),
                new Vector2(6.8f / PPM / 6, 6.8f / PPM * 3),
                BodyFactory.ENEMY,
                this
        );
    }

    protected void defineEnemy() {
        Rectangle rect = ((RectangleMapObject) mapObject).getRectangle();
        makeBoxody(
                (rect.x + rect.width / 2) / PPM,
                (rect.y + rect.height / 2) / PPM
        );
    }

    @Override
    public int score() {
        return 50;
    }

    @Override
    public void killed() {
        super.killed();
        setRegion((TextureRegion) ghostDead.getKeyFrame(stateTimer));
        isDead = true;
        isRun = false;
        becomeDead();
    }

    @Override
    public void beginAttack(Player player) {
        setRegion((TextureRegion) ghostIdle.getKeyFrame(stateTimer));
        player.playerDie();
        isRun = false;
        body.getLinearVelocity().x = 0;
    }

    @Override
    public void endAttack(Player player) {
        setRegion((TextureRegion) ghostRun.getKeyFrame(stateTimer));
        isRun = true;
    }
}
