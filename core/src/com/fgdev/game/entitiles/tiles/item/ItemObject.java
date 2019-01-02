package com.fgdev.game.entitiles.tiles.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.BodyFactory;

public abstract class ItemObject extends Sprite {
    protected World world;
    protected Body body;
    protected BodyFactory bodyFactory;
    protected MapObject object;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected ScoreIndicator scoreIndicator;
    protected float stateTimer;


    public ItemObject(World world, ScoreIndicator scoreIndicator) {
        this.world = world;
        this.scoreIndicator = scoreIndicator;
        bodyFactory = BodyFactory.getInstance(world);
    }

    protected void init() {
        defineObject();
        stateTimer = 0;
        toDestroy = false;
        destroyed = false;
        setPosition(body.getPosition().x, body.getPosition().y);
        body.setActive(false);
    }

    public abstract void init(MapObject object);

    public void destroy() {
        toDestroy = true;
    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed)
            super.draw(batch);
    }

    public void update(float dt) {
        if (body == null) return;
        if (toDestroy && !destroyed) {
            if (body != null) {
                final Array<JointEdge> list = body.getJointList();
                while (list.size > 0) {
                    world.destroyJoint(list.get(0).joint);
                }
                body.setUserData(null);
                world.destroyBody(body);
                body = null;
                destroyed = true;
                setSize(0, 0);
                return;
            }
        }
    }

    protected abstract void defineObject();

    public abstract void collected(Player player);

    public abstract int score();

    public Body getBody() {
        return body;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
