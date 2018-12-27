package com.fgdev.game.entitiles.tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.fgdev.game.utils.BodyFactory;

public abstract class PlatformObject {
    protected World world;
    protected Body body;
    protected BodyFactory bodyFactory;
    protected MapObject object;

    public PlatformObject(World world) {
        this.world = world;
        bodyFactory = BodyFactory.getInstance(world);
    }

    protected void init() {
        definePlatform();
    }

    public abstract void init(MapObject mapObject);

    protected abstract void definePlatform();

    public Body getBody() {
        return body;
    }
}
