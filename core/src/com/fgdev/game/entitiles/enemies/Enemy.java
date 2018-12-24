package com.fgdev.game.entitiles.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.Constants;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.entitiles.tiles.ItemObject;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.BodyFactory;

public abstract class Enemy extends Sprite {
    protected World world;
    protected Body body;
    protected BodyFactory bodyFactory;
    protected MapObject mapObject;
    protected int type;
    protected ScoreIndicator scoreIndicator;

    protected boolean runningRight;
    protected float stateTimer;

    protected boolean toBeDestroyed;
    protected boolean destroyed;

    public Enemy(World world, MapObject mapObject, int type, ScoreIndicator scoreIndicator) {
        this.world = world;
        this.mapObject = mapObject;
        this.type = type;
        this.scoreIndicator = scoreIndicator;
        stateTimer = 0;
        bodyFactory = BodyFactory.getInstance(world);
        toBeDestroyed = false;
        destroyed = false;
        defineEnemy();
        body.setActive(false);
    }

    protected abstract void defineEnemy();

    public abstract void update(float dt);

    public abstract int score();

    public abstract void killed();

    public void queueDestroy() {
        toBeDestroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    protected void becomeDead() {
        Filter filter;
        for (Fixture fixture : body.getFixtureList()) {
            filter = fixture.getFilterData();
            filter.maskBits = Constants.GROUND_BIT;
            fixture.setFilterData(filter);
        }
    }

    protected void checkMovingDirection() {
        Vector2 p1;
        Vector2 p2;

        RayCastCallback rayCastCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getUserData() == this || fixture.getUserData() instanceof ItemObject) {
                    return 1;
                }
                if (fraction < 1.0f && fixture.getUserData().getClass() != Player.class) {
                    runningRight = !runningRight;
                }
                return 0;
            }
        };

        if (runningRight) {
            p1 = new Vector2(body.getPosition().x + 0.5f, body.getPosition().y);
            p2 = new Vector2(p1).add(0.1f, 0);

            world.rayCast(rayCastCallback, p1, p2);
        }
        else {
            p1 = new Vector2(body.getPosition().x - 0.5f, body.getPosition().y);
            p2 = new Vector2(p1).add(-0.1f, 0);

            world.rayCast(rayCastCallback, p1, p2);
        }
    }
}
