package com.fgdev.game.entitiles.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.BodyFactory;
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.PPM;

public class Zombie extends Enemy implements Pool.Poolable {

    public static final String TAG = Zombie.class.getName();

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    public enum State {
        IDLE, WALK, DEAD, ATTACK,
    }

    private State currentState;
    private State previousState;
    private Animation zombieIdle;
    private Animation zombieWalk;
    private Animation zombieDead;
    private Animation zombieAttack;

    private boolean isWalk;
    private boolean isAttack;

    public Zombie(World world, ScoreIndicator scoreIndicator) {
        super(world, scoreIndicator);
    }

    public void init(MapObject mapObject, int type) {
        this.mapObject = mapObject;
        this.type = type;
        currentState = State.IDLE;
        previousState = State.IDLE;
        isWalk = true;
        isDead = false;
        isAttack = false;
        speed = 1f;
        Assets.AssetZombie zombie = Assets.instance.zombie;
        zombieIdle = type == MALE ? zombie.animMaleIdle : zombie.animFeMaleIdle;
        zombieWalk = type == MALE ? zombie.animMaleWalk : zombie.animFeMaleWalk;
        zombieDead = type == MALE ? zombie.animMaleDead : zombie.animFeMaleDead;
        zombieAttack = type == MALE ? zombie.animMaleAttack : zombie.animFeMaleAttack;
        // Extend Abstract
        init();
        setRegion((TextureRegion) zombieIdle.getKeyFrame(stateTimer));
    }

    @Override
    public void reset() {
        currentState = State.IDLE;
        previousState = State.IDLE;
        isWalk = true;
        isDead = false;
        isAttack = false;
        speed = 1f;
        Assets.AssetZombie zombie = Assets.instance.zombie;
        zombieIdle = type == MALE ? zombie.animMaleIdle : zombie.animFeMaleIdle;
        zombieWalk = type == MALE ? zombie.animMaleWalk : zombie.animFeMaleWalk;
        zombieDead = type == MALE ? zombie.animMaleDead : zombie.animFeMaleDead;
        zombieAttack = type == MALE ? zombie.animMaleAttack : zombie.animFeMaleAttack;
    }


    public void update(float dt) {
        if (isWalk) {
            running();
        }
        super.update(dt);
    }

    @Override
    protected void setBoundForRegion() {
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

    @Override
    protected TextureRegion getFrame(float dt) {
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
                region = (TextureRegion) zombieAttack.getKeyFrame(stateTimer, true);
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
        float width = type == MALE ? (69 - 30) / PPM : (78 - 30)/ PPM;
        float height = type == MALE ? (83 - 30) / PPM : (86 - 30) / PPM;
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
        makeBoxZombieBody(
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
        setRegion((TextureRegion) zombieDead.getKeyFrame(stateTimer));
        isDead = true;
        isWalk = false;
        becomeDead();
    }

    @Override
    public void beginAttack(Player player) {
        setRegion((TextureRegion) zombieAttack.getKeyFrame(stateTimer));
        player.playerDie();
        isAttack = true;
        isWalk = false;
        body.getLinearVelocity().x = 0;
    }

    @Override
    public void endAttack(Player player) {
        setRegion((TextureRegion) zombieIdle.getKeyFrame(stateTimer));
        isWalk = true;
        isAttack = false;
    }
}
