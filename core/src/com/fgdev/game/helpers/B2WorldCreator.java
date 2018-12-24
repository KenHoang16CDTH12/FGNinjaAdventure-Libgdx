package com.fgdev.game.helpers;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.fgdev.game.entitiles.enemies.Robot;
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
    private Array<Robot> robots;

    public B2WorldCreator(World world, Map map, ScoreIndicator scoreIndicator) {
        grounds = new Array<Ground>();
        //create grounds bodies/fixtures
        for (MapObject object: map.getLayers().get("grounds").getObjects().getByType(RectangleMapObject.class)) {
            grounds.add(new Ground(world, object));
        }

        //create hidden wall bodies/fixtures
        for (MapObject object: map.getLayers().get("hidden_walls").getObjects().getByType(RectangleMapObject.class)) {
            new HiddenWall(world, object);
        }

        signs = new Array<Sign>();
        //create signs bodies/fixtures
        for (MapObject object: map.getLayers().get("signs").getObjects().getByType(RectangleMapObject.class)) {
            signs.add(new Sign(world, object));
        }

        crates = new Array<Crate>();
        //create crates bodies/fixtures
        for (MapObject object: map.getLayers().get("crates").getObjects().getByType(RectangleMapObject.class)) {
            crates.add(new Crate(world, object, scoreIndicator));
        }

        coins = new Array<Coin>();
        //create coins bodies/fixtures
        for (MapObject object: map.getLayers().get("coins").getObjects().getByType(EllipseMapObject.class)) {
            coins.add(new Coin(world, object, scoreIndicator));
        }

        feathers = new Array<Feather>();
        //create feathers bodies/fixtures
        for (MapObject object: map.getLayers().get("feathers").getObjects().getByType(RectangleMapObject.class)) {
            feathers.add(new Feather(world, object, scoreIndicator));
        }

        zombies = new Array<Zombie>();
        //create zombies bodies/fixtures
        for (MapObject object: map.getLayers().get("zombies").getObjects().getByType(RectangleMapObject.class)) {
            zombies.add(new Zombie(world, object, scoreIndicator, object.getProperties().get("type", Integer.class)));
        }

        robots = new Array<Robot>();
        //create zombies bodies/fixtures
        for (MapObject object: map.getLayers().get("robots").getObjects().getByType(RectangleMapObject.class)) {
            robots.add(new Robot(world, object, scoreIndicator, object.getProperties().get("type", Integer.class)));
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

    public Array<Robot> getRobots() {
        return robots;
    }
}
