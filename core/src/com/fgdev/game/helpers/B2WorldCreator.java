package com.fgdev.game.helpers;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.fgdev.game.entitiles.enemies.Zombie;
import com.fgdev.game.entitiles.tiles.*;

public class B2WorldCreator {
    // Platform
    private Array<Ground> grounds;
    private Array<Sign> signs;
    // Items
    private Array<Coin> coins;
    private Array<Feather> feathers;
    private Array<Crate> crates;
    // Enemies
    private Array<Zombie> zombies;

    public B2WorldCreator(World world, Map map) {
        grounds = new Array<Ground>();
        //create grounds bodies/fixtures
        for (MapObject object: map.getLayers().get("grounds").getObjects().getByType(RectangleMapObject.class)) {
            grounds.add(new Ground(world, object));
        }

        signs = new Array<Sign>();
        //create signs bodies/fixtures
        for (MapObject object: map.getLayers().get("signs").getObjects().getByType(RectangleMapObject.class)) {
            signs.add(new Sign(world, object));
        }

        crates = new Array<Crate>();
        //create crates bodies/fixtures
        for (MapObject object: map.getLayers().get("crates").getObjects().getByType(RectangleMapObject.class)) {
            crates.add(new Crate(world, object));
        }

        coins = new Array<Coin>();
        //create coins bodies/fixtures
        for (MapObject object: map.getLayers().get("coins").getObjects().getByType(EllipseMapObject.class)) {
            coins.add(new Coin(world, object));
        }

        feathers = new Array<Feather>();
        //create feathers bodies/fixtures
        for (MapObject object: map.getLayers().get("feathers").getObjects().getByType(RectangleMapObject.class)) {
            feathers.add(new Feather(world, object));
        }

        zombies = new Array<Zombie>();
        //create zombies bodies/fixtures
        for (MapObject object: map.getLayers().get("zombies").getObjects().getByType(RectangleMapObject.class)) {
            zombies.add(new Zombie(world, object, object.getProperties().get("type", Integer.class)));
        }
    }

    public Array<Ground> getGrounds() {
        return grounds;
    }

    public Array<Sign> getSigns() {
        return signs;
    }

    public Array<Coin> getCoins() {
        return coins;
    }

    public Array<Feather> getFeathers() {
        return feathers;
    }

    public Array<Crate> getCrates() {
        return crates;
    }

    public Array<Zombie> getZombies() {
        return zombies;
    }
}
