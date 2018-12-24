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

    public PlatformObject(World world, MapObject object) {
        this.world = world;
        this.object = object;
        bodyFactory = BodyFactory.getInstance(world);
        definePlatform();
    }

    protected abstract void definePlatform();

    public Body getBody() {
        return body;
    }
}
