package com.fgdev.game.entitiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.fgdev.game.entitiles.bullets.Kunai;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.AudioManager;
import com.fgdev.game.Constants;
import com.fgdev.game.utils.BodyFactory;
import com.fgdev.game.utils.GamePreferences;

import static com.fgdev.game.Constants.*;

public class Player extends Sprite {

    public enum State {
        IDDLE, RUN, JUMP, CLIMB,
        DEAD, GLIDE, JUMP_ATTACK,
        JUMP_THROW, ATTACK, SLIDE,
        THROW, DELAY
    }
    public State currentState;
    public State previousState;
    public World world;
    public Body body;
    public BodyFactory bodyFactory;
    private Animation playerIddle;
    private Animation playerDelay;
    private Animation playerRun;
    private Animation playerJump;
    private Animation playerClimb;
    private Animation playerDead;
    private Animation playerGlide;
    private Animation playerJumpAttack;
    private Animation playerJumpThrow;
    private Animation playerAttack;
    private Animation playerSlide;
    private Animation playerThrow;
    private boolean runningRight;
    private float stateTimer;
    private boolean isGirl;
    private boolean isDead;
    private boolean isSlide;
    private boolean isClimb;
    private boolean isAttack;
    private boolean isThrow;
    private boolean isDelay;
    private boolean canThrow;

    private boolean timeToRedefinePlayer;
    private boolean timeTodefinePlayerAttackRight;
    private boolean timeTodefinePlayerAttackLeft;

    public float timeJumping;
    public boolean hasFeatherPowerup;
    public float timeLeftFeatherPowerup;

    private final float JUMP_TIME_MAX = 0.3f;
    private final float JUMP_TIME_MIN = 0.1f;
    private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

    // Shoot
    private Array<Kunai> kunaies;

    private boolean isOnGround;

    public Player(World world) {
        this.world = world;
        bodyFactory = BodyFactory.getInstance(world);
        currentState = State.IDDLE;
        previousState = State.IDDLE;
        stateTimer = 0;
        timeJumping = 0;
        isDead = false;
        isSlide = false;
        isClimb = false;
        isAttack = false;
        isThrow = false;
        isDelay = false;
        canThrow = true;
        isOnGround = false;
        runningRight = true;
        isGirl = GamePreferences.instance.isGirl;
        // Power-ups
        hasFeatherPowerup = false;
        timeLeftFeatherPowerup = 0;
        // Iddle
        playerIddle = Assets.instance.player.animIddle;
        // Delay
        playerDelay = Assets.instance.player.animIddle;
        // Run
        playerRun = Assets.instance.player.animRun;
        // Jump
        playerJump = Assets.instance.player.animJump;
        // Climb
        playerClimb = Assets.instance.player.animClimb;
        // Dead
        playerDead = Assets.instance.player.animDead;
        // Glide
        playerGlide = Assets.instance.player.animGlide;
        // JumpAttack
        playerJumpAttack = Assets.instance.player.animJumpAttack;
        // JumpThrow
        playerJumpThrow = Assets.instance.player.animJumpThrow;
        // Attack
        playerAttack = Assets.instance.player.animAttack;
        // Slide
        playerSlide = Assets.instance.player.animSlide;
        // Throw
        playerThrow = Assets.instance.player.animThrow;
        definePlayer();
        setRegion((TextureRegion) playerIddle.getKeyFrame(stateTimer));
        // kunai
        kunaies = new Array<Kunai>();
    }

    public void update(float dt) {
        setBoundForRegion();
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 - 0.1f);
        setRegion(getFrame(dt));
        if (timeTodefinePlayerAttackRight)
            definePlayerAttackRight();
        if (timeTodefinePlayerAttackLeft)
            definePlayerAttackLeft();
        if (timeToRedefinePlayer)
            reDefinePlayer();
        for (Kunai kunai: kunaies) {
            kunai.update(dt);
            if (kunai.isDestroyed())
                kunaies.removeValue(kunai, true);
        }
    }

    private void setBoundForRegion() {
        currentState = getState();
        switch (currentState) {
            case RUN:
                if (isGirl) setBounds(0, 0, 56 * 2  / PPM, 78 * 2 / PPM);
                else setBounds(0, 0, 54 * 2  / PPM, 69 * 2 / PPM);
                break;
            case JUMP:
                if (isGirl) setBounds(0, 0, 60 * 2  / PPM, 81 * 2 / PPM);
                else setBounds(0, 0, 54 * 2  / PPM, 72 * 2 / PPM);
                break;
            case CLIMB:
                if (isGirl) setBounds(0, 0, 54 * 2  / PPM, 75 * 2 / PPM);
                else setBounds(0, 0, 42 * 2  / PPM, 70 * 2 / PPM);
                break;
            case DEAD:
                if (isGirl) setBounds(0, 0, 87 * 2  / PPM, 90 * 2 / PPM);
                else setBounds(0, 0, 72 * 2  / PPM, 75 * 2 / PPM);
                break;
            case GLIDE:
                if (isGirl) setBounds(0, 0, 76 * 2  / PPM, 71 * 2 / PPM);
                else setBounds(0, 0, 66 * 2  / PPM, 68 * 2 / PPM);
                break;
            case JUMP_ATTACK:
                if (isGirl) setBounds(0, 0, 74 * 2  / PPM, 87 * 2 / PPM);
                else setBounds(0, 0, 76 * 2  / PPM, 78 * 2 / PPM);
                break;
            case JUMP_THROW:
                if (isGirl) setBounds(0, 0, 64 * 2  / PPM, 75 * 2 / PPM);
                else setBounds(0, 0, 54 * 2  / PPM, 65 * 2 / PPM);
                break;
            case ATTACK:
                if (isGirl) setBounds(0, 0, 79 * 2  / PPM, 85 * 2 / PPM);
                else setBounds(0, 0, 80 * 2  / PPM, 74 * 2 / PPM);
                break;
            case SLIDE:
                if (isGirl) setBounds(0, 0, 60 * 2  / PPM, 60 * 2 / PPM);
                else setBounds(0, 0, 56 * 2  / PPM, 53 * 2 / PPM);
                break;
            case THROW:
                if (isGirl) setBounds(0, 0, 57 * 2  / PPM, 77 * 2 / PPM);
                else setBounds(0, 0, 57 * 2  / PPM, 68 * 2 / PPM);
                break;
            case IDDLE:
            default:
                if (isGirl) setBounds(0, 0, 44 * 2  / PPM, 75 * 2 / PPM);
                else setBounds(0, 0, 35 * 2  / PPM, 66 * 2 / PPM);
                break;
        }
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        //depending on the state, get corresponding animation KeyFrame
        switch (currentState) {
            case RUN:
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case JUMP:
                region = (TextureRegion) playerJump.getKeyFrame(stateTimer);
                break;
            case CLIMB:
                region = (TextureRegion) playerClimb.getKeyFrame(stateTimer);
                if (playerClimb.isAnimationFinished(stateTimer))
                    isClimb = false;
                break;
            case DEAD:
                region = (TextureRegion) playerDead.getKeyFrame(stateTimer);
                break;
            case GLIDE:
                region = (TextureRegion) playerGlide.getKeyFrame(stateTimer, true);
                break;
            case JUMP_ATTACK:
                region = (TextureRegion) playerJumpAttack.getKeyFrame(stateTimer);
                if (playerJumpAttack.isAnimationFinished(stateTimer)) {}
                break;
            case JUMP_THROW:
                region = (TextureRegion) playerJumpThrow.getKeyFrame(stateTimer);
                if (playerJumpThrow.isAnimationFinished(stateTimer)) {
                    if (!canThrow && !world.isLocked())
                        kunaies.add(new Kunai(this, body.getPosition().x - getWidth() / 2 + 1.25f, body.getPosition().y - getHeight() / 2 + 0.65f, runningRight ? true : false));
                    canThrow = true;
                }
                break;
            case ATTACK:
                region = (TextureRegion) playerAttack.getKeyFrame(stateTimer);
                if (playerAttack.isAnimationFinished(stateTimer)) {
                    isAttack = false;
                    isDelay = true;
                    if (runningRight)
                        timeTodefinePlayerAttackRight = true;
                    else
                        timeTodefinePlayerAttackLeft = true;
                }
                break;
            case SLIDE:
                region = (TextureRegion) playerSlide.getKeyFrame(stateTimer);
                if (playerSlide.isAnimationFinished(stateTimer))
                    isSlide = false;
                break;
            case THROW:
                region = (TextureRegion) playerThrow.getKeyFrame(stateTimer);
                if (playerThrow.isAnimationFinished(stateTimer)) {
                    canThrow = true;
                    isThrow = false;
                }
                break;
            case DELAY:
                region = (TextureRegion) playerDelay.getKeyFrame(stateTimer);
                if (playerDelay.isAnimationFinished(stateTimer)) {
                    isDelay = false;
                    timeToRedefinePlayer = true;
                }
                break;
            case IDDLE:
            default:
                region = (TextureRegion) playerIddle.getKeyFrame(stateTimer, true);
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
        //return our final adjusted frame
        if (timeLeftFeatherPowerup > 0) {
            timeLeftFeatherPowerup -= dt;
            if (timeLeftFeatherPowerup < 0) {
                // disable power-up
                timeLeftFeatherPowerup = 0;
                setFeatherPowerup(false);
            }
        }
        return region;
    }

    private State getState() {
            if (isDead)
                return State.DEAD;
            else if ((body.getLinearVelocity().y > 0 && currentState == State.GLIDE) || (body.getLinearVelocity().y < 0 && previousState == State.GLIDE) && hasFeatherPowerup)
                return State.GLIDE;
            else if ((body.getLinearVelocity().y > 0 && currentState == State.JUMP) || (body.getLinearVelocity().y < 0 && previousState == State.JUMP))
                return State.JUMP;
            else if ((body.getLinearVelocity().y > 0 && currentState == State.JUMP_THROW) || (body.getLinearVelocity().y < 0 && previousState == State.JUMP_THROW))
                return State.JUMP_THROW;
            else if (body.getLinearVelocity().x != 0)
                return State.RUN;
            else if (isSlide)
                return State.SLIDE;
            else if (isClimb)
                return State.CLIMB;
            else if (isAttack)
                return State.ATTACK;
            else if (isThrow)
                return State.THROW;
            else if (isDelay)
                return State.DELAY;
                // if none of these return then he must be standing
            else
                return State.IDDLE;
    }

    public void jump() {
        if (currentState != State.JUMP && currentState != State.ATTACK) {
            if (currentState != State.GLIDE) {
                if (hasFeatherPowerup) {
                    AudioManager.instance.play(Assets.instance.sounds.glide);
                    body.applyLinearImpulse(new Vector2(0, 1.5f), body.getWorldCenter(), true);
                    currentState = State.GLIDE;
                }
                else if (currentState != State.JUMP_THROW) {
                    // AudioManager.instance.play(Assets.instance.sounds.jump);
                    body.applyLinearImpulse(new Vector2(0, 7f), body.getWorldCenter(), true);
                    currentState = State.JUMP;
                }
            } else {
                if (hasFeatherPowerup) {
                    body.applyLinearImpulse(new Vector2(0, 0.2f), body.getWorldCenter(), true);
                    currentState = State.GLIDE;
                }
            }
        }
    }

    public void right() {
        if (currentState != State.ATTACK && currentState != State.THROW)
            body.applyLinearImpulse(new Vector2(0.1f, 0), body.getWorldCenter(), true);
    }

    public void left() {
        if (currentState != State.ATTACK && currentState != State.THROW)
            body.applyLinearImpulse(new Vector2(-0.1f, 0), body.getWorldCenter(), true);
    }

    public void down() {
        isSlide = true;
    }

    public void climb() {
        isClimb = true;
    }

    public void jumpThrow() {
        if (currentState != State.JUMP
                && currentState != State.JUMP_THROW
                && currentState != State.GLIDE) {
            // AudioManager.instance.play(Assets.instance.sounds.jump);
            body.applyLinearImpulse(new Vector2(0, 7f), body.getWorldCenter(), true);
            currentState = State.JUMP_THROW;
            canThrow = false;
        }
    }

    public void attack() {
        if (currentState != State.ATTACK && currentState != State.GLIDE && currentState != State.JUMP) {
            isAttack = true;
        }
    }

    public void attackThrow() {
        if (currentState != State.JUMP_THROW && currentState != State.JUMP && currentState != State.GLIDE && currentState != State.ATTACK && canThrow && !world.isLocked()) {
            kunaies.add(new Kunai(this, body.getPosition().x - getWidth() / 2 + 1.25f, body.getPosition().y - getHeight() / 2 + 0.65f, runningRight ? true : false));
            isThrow =true;
            canThrow = false;
        }
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void setFeatherPowerup (boolean pickedUp) {
        hasFeatherPowerup = pickedUp;
        if (pickedUp) {
            timeLeftFeatherPowerup =
                    Constants.ITEM_FEATHER_POWERUP_DURATION;
        }
    }

    public boolean hasFeatherPowerup() {
        return hasFeatherPowerup && timeLeftFeatherPowerup > 0;
    }

    private void makeBoxPlayerBody(float posx, float posy) {
        float width = isGirl ? (56 - 10) / PPM : (54 - 10) / PPM;
        float height = isGirl ? (78 - 10) / PPM : (69 - 10) / PPM;
        // create player
        body = bodyFactory.makeBoxPolyBody(
                posx,
                posy,
                width,
                height,
                BodyFactory.PLAYER,
                BodyDef.BodyType.DynamicBody,
                this
        );
        // create foot sensor
        bodyFactory.makeObjectSensor(body,
                width,
                10 / PPM,
                new Vector2(0, -height - (7 / PPM)),
                0,
                BodyFactory.PLAYER_FOOT,
                this
        );
    }

    private void definePlayer() {
        makeBoxPlayerBody(100 / PPM, 500 / PPM);
    }

    private void reDefinePlayer() {
        Vector2 currentPosition = body.getPosition();
        world.destroyBody(body);
        makeBoxPlayerBody(currentPosition.x, currentPosition.y);
        timeToRedefinePlayer = false;
    }

    private void definePlayerAttackRight() {
        Vector2 currentPosition = body.getPosition();
        world.destroyBody(body);
        makeBoxPlayerBody(currentPosition.x, currentPosition.y);
        // create right attack sensor
        bodyFactory.makeObjectSensor(body,
                20 / PPM,
                60 / PPM,
                new Vector2(61 / PPM, 0),
                0,
                BodyFactory.PLAYER_ATTACK,
                this
        );
        timeTodefinePlayerAttackRight = false;
    }

    private void definePlayerAttackLeft() {Vector2 currentPosition = body.getPosition();
        world.destroyBody(body);
        makeBoxPlayerBody(currentPosition.x, currentPosition.y);
        // create left attack sensor
        bodyFactory.makeObjectSensor(body,
                20 / PPM,
                60 / PPM,
                new Vector2(-61 / PPM, 0),
                0,
                BodyFactory.PLAYER_ATTACK,
                this
        );
        timeTodefinePlayerAttackLeft = false;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        for(Kunai kunai : kunaies)
            kunai.draw(batch);
    }

    public void setOnGround(boolean onGround) {
        isOnGround = onGround;
    }

}