package com.fgdev.game.entitiles.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.entitiles.bullets.EnemyBullet;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.BodyFactory;
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.PPM;

public class Robot extends Enemy implements Pool.Poolable {

    public static final String TAG = Robot.class.getName();

    public static final int SHOOT = 0;
    public static final int MELEE = 1;

    public enum State {
        IDLE, RUN, DEAD, SHOOT, JUMP,
        JUMP_SHOOT, JUMP_MELEE, MELEE,
        RUN_SHOOT, SLIDE
    }

    private State currentState;
    private State previousState;

    private Animation robotIdle;
    private Animation robotRun;
    private Animation robotDead;
    private Animation robotShoot;
    private Animation robotJumpShoot;
    private Animation robotJumpMelee;
    private Animation robotMelee;
    private Animation robotRunShoot;
    private Animation robotSlide;
    private Animation robotJump;

    private boolean isRun;
    private boolean isShoot;
    private boolean isMelee;
    private boolean isSlide;

    private float timeDelayIdle = 1;
    private float timeDelayRun = 2;

    public Robot(World world, ScoreIndicator scoreIndicator) {
        super(world, scoreIndicator);
        robotIdle = Assets.instance.robot.animIdle;
        robotRun = Assets.instance.robot.animRun;
        robotDead = Assets.instance.robot.animDead;
        robotShoot = Assets.instance.robot.animShoot;
        robotJumpShoot = Assets.instance.robot.animJumpShoot;
        robotJumpMelee = Assets.instance.robot.animJumpMelee;
        robotMelee = Assets.instance.robot.animMelee;
        robotRunShoot = Assets.instance.robot.animRunShoot;
        robotSlide = Assets.instance.robot.animSlide;
        robotJump = Assets.instance.robot.animJump;
    }

    public void init(MapObject mapObject, int type) {
        this.mapObject = mapObject;
        this.type = type;
        currentState = State.IDLE;
        previousState = State.IDLE;
        isRun = true;
        isDead = false;
        isShoot = false;
        isMelee = false;
        isSlide = false;
        speed = 1.5f;
        // Extend Abstract
        init();
        setRegion((TextureRegion) robotIdle.getKeyFrame(stateTimer));
    }

    @Override
    public void reset() {
    }

    public void update(float dt) {
        if (timeDelayIdle > 0) {
            timeDelayIdle -= dt;
            if (timeDelayIdle < 0) timeDelayRun = 3;
        }

        if (isRun) {
            if (timeDelayRun > 0) {
                timeDelayRun -= dt;
                running();
            }
            if (timeDelayRun < 0 && timeDelayIdle < 0) {
                previousState = State.SHOOT;
                isShoot = true;
            }

        }

        // spawn
        handleSpawningBullet();

        // update bullets
        for (EnemyBullet bullet : bullets) {
            bullet.update(dt);
        }
        // clean
        for (int i = 0; i < bullets.size; i++) {
            if (!bullets.get(i).isAlive()) {
                bullets.removeIndex(i);
            }
        }

        super.update(dt);
    }

    @Override
    protected void setBoundForRegion() {
        currentState = getState();
        switch (currentState) {
            case DEAD:
                setBounds(0, 0, 112 * 2  / PPM, 104 * 2 / PPM);
                break;
            case RUN:
            case SHOOT:
            case RUN_SHOOT:
            case SLIDE:
            case JUMP:
            case JUMP_MELEE:
            case JUMP_SHOOT:
            case MELEE:
            case IDLE:
            default:
                setBounds(0, 0, 113 * 2  / PPM, 111 * 2 / PPM);
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
                region = (TextureRegion) robotRun.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = (TextureRegion) robotDead.getKeyFrame(stateTimer);
                break;
            case SHOOT:
                region = (TextureRegion) robotShoot.getKeyFrame(stateTimer);
                if (robotShoot.isAnimationFinished(stateTimer)) {
                    if (isShoot) {
                        float x = runningRight ? 1f : -1f;
                        float y = -0.1f;
                        // if you want to spawn a new bullet:
                        addSpawnBullet(body.getPosition().x + x, body.getPosition().y + y, runningRight ? true : false);
                        isShoot = false;
                        timeDelayIdle = 1;
                    }
                }
                break;
            case JUMP_SHOOT:
                region = (TextureRegion) robotJumpShoot.getKeyFrame(stateTimer);
                break;
            case JUMP_MELEE:
                region = (TextureRegion) robotJumpMelee.getKeyFrame(stateTimer);
                break;
            case MELEE:
                region = (TextureRegion) robotMelee.getKeyFrame(stateTimer, true);
                break;
            case RUN_SHOOT:
                region = (TextureRegion) robotRunShoot.getKeyFrame(stateTimer);
                break;
            case SLIDE:
                region = (TextureRegion) robotSlide.getKeyFrame(stateTimer);
                break;
            case JUMP:
                region = (TextureRegion) robotJump.getKeyFrame(stateTimer);
                break;
            case IDLE:
            default:
                region = (TextureRegion) robotIdle.getKeyFrame(stateTimer,true);
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
        else if (isMelee)
            return State.MELEE;
        else if (isSlide)
            return State.SLIDE;
        else if (body.getLinearVelocity().x != 0)
            return State.RUN;
        else if (isShoot)
            return State.SHOOT;
            // if none of these return then he must be standing
        else
            return State.IDLE;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        // draw bullets
        for (EnemyBullet bullet : bullets) {
            bullet.draw(batch);
        }
    }

    private void makeBoxRobotBody(float posx, float posy) {
        float width = (113 - 60)/ PPM;
        float height = (111 - 15) / PPM;
        // create robot
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
                new Vector2(0, (-height - 85) / PPM),
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
        return 100;
    }

    @Override
    public void killed() {
        super.killed();
        setRegion((TextureRegion) robotDead.getKeyFrame(stateTimer));
        isDead = true;
        isRun = false;
        becomeDead();
    }

    @Override
    public void beginAttack(Player player) {
        setRegion((TextureRegion) robotMelee.getKeyFrame(stateTimer));
        player.playerDie();
        isMelee = true;
        isRun = false;
        body.getLinearVelocity().x = 0;
    }

    @Override
    public void endAttack(Player player) {
        setRegion((TextureRegion) robotIdle.getKeyFrame(stateTimer));
        isRun = true;
        isMelee = false;
    }
}
