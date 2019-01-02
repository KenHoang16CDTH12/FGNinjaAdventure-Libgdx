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

public class Bone extends Enemy implements Pool.Poolable {

    public static final String TAG = Bone.class.getName();

    public enum State {
        WALK, DEAD, ATTACK
    }

    private State currentState;
    private State previousState;
    private Animation boneWalk;
    private Animation boneDead;
    private Animation boneAttack;

    private boolean isWalk;
    private boolean isAttack;

    public Bone(World world, ScoreIndicator scoreIndicator) {
        super(world, scoreIndicator);
    }

    public void init(MapObject mapObject, int type) {
        this.mapObject = mapObject;
        this.type = type;
        currentState = State.WALK;
        previousState = State.WALK;
        isWalk = true;
        isDead = false;
        isAttack = false;
        speed = 1f;
        boneWalk = Assets.instance.bone.animWalk;
        boneDead = Assets.instance.bone.animDead;
        boneAttack = Assets.instance.bone.animAttack;
        // Extend Abstract
        init();
        setRegion((TextureRegion) boneWalk.getKeyFrame(stateTimer));
    }

    @Override
    public void reset() {
        currentState = State.WALK;
        previousState = State.WALK;
        isWalk = true;
        isDead = false;
        isAttack = false;
        speed = 1f;
        boneWalk = Assets.instance.bone.animWalk;
        boneDead = Assets.instance.bone.animDead;
        boneAttack = Assets.instance.bone.animAttack;
    }


    public void update(float dt) {
        if (isWalk) {
            running();
        }
        super.update(dt);
    }

    @Override
    protected void setBoundForRegion() {
        setBounds(0, 0, 58 * 2  / PPM, 103 * 2 / PPM);
    }

    @Override
    protected TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        //depending on the state, get corresponding animation KeyFrame
        switch (currentState) {
            case DEAD:
                region = (TextureRegion) boneDead.getKeyFrame(stateTimer);
                break;
            case ATTACK:
                region = (TextureRegion) boneAttack.getKeyFrame(stateTimer, true);
                break;
            case WALK:
            default:
                region = (TextureRegion) boneWalk.getKeyFrame(stateTimer, true);
                break;
        }
        //if player is running left and the texture isnt facing left... flip it.
        if ((body.getLinearVelocity().x < 0 || !runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        //if player is running right and the texture isnt facing right... flip it.
        else if ((body.getLinearVelocity().x > 0 || runningRight) && !region.isFlipX()) {
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
            return State.WALK;
    }

    private void makeBoxody(float posx, float posy) {
        float width = (58 - 5) / PPM;
        float height = (103 - 5)/ PPM;
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
                new Vector2(0, (-height - 80) / PPM),
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
        return 100;
    }

    @Override
    public void killed() {
        setRegion((TextureRegion) boneDead.getKeyFrame(stateTimer));
        isDead = true;
        isWalk = false;
        becomeDead();
    }

    @Override
    public void beginAttack(Player player) {
        setRegion((TextureRegion) boneAttack.getKeyFrame(stateTimer));
        player.playerDie();
        isAttack = true;
        isWalk = false;
        body.getLinearVelocity().x = 0;
    }

    @Override
    public void endAttack(Player player) {
        setRegion((TextureRegion) boneWalk.getKeyFrame(stateTimer));
        isWalk = true;
        isAttack = false;
    }
}
