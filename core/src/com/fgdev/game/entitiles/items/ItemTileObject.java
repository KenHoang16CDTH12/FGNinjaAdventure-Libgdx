package com.fgdev.game.entitiles.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.fgdev.game.entitiles.Player;

public abstract class ItemTileObject extends Sprite {
    protected World world;
    protected Body body;
    protected MapObject object;
    protected boolean toDestroy;
    protected boolean destroyed;

    public ItemTileObject(World world, MapObject object) {
        this.world = world;
        this.object = object;
        defineObject();
        toDestroy = false;
        destroyed = false;
        setPosition(body.getPosition().x, body.getPosition().y);
        body.setActive(false);
    }

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

    public abstract void onHit(Player player);

    public abstract int score();

    public Body getBody() {
        return body;
    }
}
