package com.fgdev.game.entitiles.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.BodyFactory;

import static com.fgdev.game.Constants.PPM;

public class Zombie extends Sprite {

    public static final String TAG = Zombie.class.getName();

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    public int type;

    public enum State {
        IDLE, WALK, DEAD, ATTACK,
    }
    public State currentState;
    public State previousState;
    public World world;
    public Body body;
    public MapObject mapObject;
    public BodyFactory bodyFactory;
    private Animation zombieIdle;
    private Animation zombieWalk;
    private Animation zombieDead;
    private Animation zombieAttack;
    private boolean runningRight;
    private boolean isDead;
    private boolean isAttack;
    private float stateTimer;

    public Zombie(World world, MapObject mapObject, int type) {
        this.world = world;
        this.mapObject = mapObject;
        this.type = type;
        bodyFactory = BodyFactory.getInstance(world);
        currentState = State.IDLE;
        previousState = State.IDLE;
        stateTimer = 0;
        isDead = false;
        isAttack = false;
        Assets.AssetZombie zombie = Assets.instance.zombie;
        zombieIdle = type == MALE ? zombie.animMaleIdle : zombie.animFeMaleIdle;
        zombieWalk = type == MALE ? zombie.animMaleWalk : zombie.animFeMaleWalk;
        zombieDead = type == MALE ? zombie.animMaleDead : zombie.animFeMaleDead;
        zombieAttack = type == MALE ? zombie.animMaleAttack : zombie.animFeMaleAttack;
        defineZombie();
        setRegion((TextureRegion) zombieIdle.getKeyFrame(stateTimer));
        body.setActive(false);
    }


    public void update(float dt) {
        setBoundForRegion();
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }


    private void setBoundForRegion() {
        currentState = getState();
        switch (currentState) {
            case WALK:
                if (type == MALE) setBounds(0, 0, 69 * 2  / PPM, 83 * 2 / PPM);
                else setBounds(0, 0, 78 * 2  / PPM, 86 * 2 / PPM);
                break;
            case ATTACK:
                if (type == MALE) setBounds(0, 0, 69 * 2  / PPM, 83 * 2 / PPM);
                else setBounds(0, 0, 78 * 2  / PPM, 86 * 2 / PPM);
                break;
            case DEAD:
                if (type == MALE) setBounds(0, 0, 101 * 2  / PPM, 84 * 2 / PPM);
                else setBounds(0, 0, 103 * 2  / PPM, 94 * 2 / PPM);
                break;
            case IDLE:
            default:
                if (type == MALE) setBounds(0, 0, 69 * 2  / PPM, 83 * 2 / PPM);
                else setBounds(0, 0, 78 * 2  / PPM, 86 * 2 / PPM);
                break;
        }
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        //depending on the state, get corresponding animation KeyFrame
        switch (currentState) {
            case WALK:
                region = (TextureRegion) zombieWalk.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = (TextureRegion) zombieDead.getKeyFrame(stateTimer);
                break;
            case ATTACK:
                region = (TextureRegion) zombieAttack.getKeyFrame(stateTimer);
                if (zombieAttack.isAnimationFinished(stateTimer)) { }
                break;
            case IDLE:
            default:
                region = (TextureRegion) zombieIdle.getKeyFrame(stateTimer, true);
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
            return State.WALK;
        else if (isAttack)
            return State.ATTACK;
            // if none of these return then he must be standing
        else
            return State.IDLE;
    }

    private void makeBoxZombieBody(float posx, float posy) {
        float width = type == MALE ? (69 - 25) / PPM : (78 - 25)/ PPM;
        float height = type == MALE ? (83 - 25) / PPM : (86 - 25) / PPM;
        // create zombie
        body = bodyFactory.makeBoxPolyBody(
                posx,
                posy,
                width,
                height,
                BodyFactory.ZOMBIE,
                BodyDef.BodyType.DynamicBody,
                this
        );
        // create foot sensor
        bodyFactory.makeObjectSensor(body,
                width,
                10 / PPM,
                new Vector2(0, (-height - 70) / PPM),
                0,
                BodyFactory.ZOMBIE_FOOT,
                this
        );
    }

    private void defineZombie() {
        Rectangle rect = ((RectangleMapObject) mapObject).getRectangle();
        makeBoxZombieBody(
                (rect.x + rect.width / 2) / PPM,
                (rect.y + rect.height / 2) / PPM
        );
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }
}
