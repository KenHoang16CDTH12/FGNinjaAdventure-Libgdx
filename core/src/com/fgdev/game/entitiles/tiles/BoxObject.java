package com.fgdev.game.entitiles.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.BodyFactory;

public abstract class BoxObject extends Sprite {
    protected World world;
    protected Body body;
    protected BodyFactory bodyFactory;
    protected MapObject object;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected ScoreIndicator scoreIndicator;

    public BoxObject(World world, ScoreIndicator scoreIndicator) {
        this.world = world;
        this.scoreIndicator = scoreIndicator;
        bodyFactory = BodyFactory.getInstance(world);
    }

    public void init() {
        defineObject();
        toDestroy = false;
        destroyed = false;
        setPosition(body.getPosition().x, body.getPosition().y);
        body.setActive(false);
    }

    protected abstract void init(MapObject object);

    public void destroy() {
        toDestroy = true;
    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed)
            super.draw(batch);
    }

    public void update(float dt) {
        if (toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    protected abstract void defineObject();

    public abstract void destroyBox();

    public abstract int score();

    public Body getBody() {
        return body;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
