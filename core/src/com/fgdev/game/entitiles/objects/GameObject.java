package com.fgdev.game.entitiles.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject {
    protected Body body;
    protected Vector2 position;
    protected Vector2 dimension;
    protected Vector2 origin;
    protected Vector2 scale;
    protected float rotation;

    protected Vector2 velocity;
    protected Vector2 terminalVelocity;
    protected Vector2 friction;

    protected Vector2 acceleration;
    protected Rectangle bounds;

    protected float stateTime;
    protected Animation animation;

    public GameObject() {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;
        velocity = new Vector2();
        terminalVelocity = new Vector2(1, 1);
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Rectangle();
    }

    public void setAnimation (Animation animation) {
        this.animation = animation;
        stateTime = 0;
    }

    public void update (float deltaTime) {
        stateTime += deltaTime;
        if (body == null) {
            updateMotionX(deltaTime);
            updateMotionY(deltaTime);
            // Move to new position
            position.x += velocity.x * deltaTime;
            position.y += velocity.y * deltaTime;
        } else {
            position.set(body.getPosition());
            rotation = body.getAngle() * MathUtils.radiansToDegrees;
        }

    }

    public abstract void render (SpriteBatch batch);

    protected void updateMotionX (float deltaTime) {
        if (velocity.x != 0) {
            // Apply friction
            if (velocity.x > 0) {
                velocity.x =
                        Math.max(velocity.x - friction.x * deltaTime, 0);
            } else {
                velocity.x =
                        Math.min(velocity.x + friction.x * deltaTime, 0);
            }
        }
        // Apply acceleration
        velocity.x += acceleration.x * deltaTime;
        // Make sure the object's velocity does not exceed the
        // positive or negative terminal velocity
        velocity.x = MathUtils.clamp(velocity.x,
                -terminalVelocity.x, terminalVelocity.x);
    }

    protected void updateMotionY (float deltaTime) {
        if (velocity.y != 0) {
            // Apply friction
            if (velocity.y > 0) {
                velocity.y = Math.max(velocity.y - friction.y *
                        deltaTime, 0);
            } else {
                velocity.y = Math.min(velocity.y + friction.y *
                        deltaTime, 0);
            }
        }
        // Apply acceleration
        velocity.y += acceleration.y * deltaTime;
        // Make sure the object's velocity does not exceed the
        // positive or negative terminal velocity
        velocity.y = MathUtils.clamp(velocity.y, -
                terminalVelocity.y, terminalVelocity.y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDimension() {
        return dimension;
    }
}
