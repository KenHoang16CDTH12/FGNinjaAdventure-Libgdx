package com.fgdev.game.tools;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.fgdev.game.entitiles.items.Coin;
import com.fgdev.game.entitiles.items.Crate;
import com.fgdev.game.entitiles.items.Feather;

import static com.fgdev.game.util.Constants.*;

public class B2WorldCreator {

    private Array<Coin> coins;
    private Array<Feather> feathers;
    private Array<Crate> crates;

    public B2WorldCreator(World world, Map map) {
        //create body and fixture variables
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;
        //create grounds bodies/fixtures
        for (MapObject object: map.getLayers().get("grounds").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);
            body = world.createBody(bodyDef);
            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = GROUND_BIT;
            fixtureDef.filter.maskBits = PLAYER_BIT;
            body.createFixture(fixtureDef).setUserData("ground");
        }

        //create signs bodies/fixtures
        for (MapObject object: map.getLayers().get("signs").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);
            body = world.createBody(bodyDef);
            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = SIGN_BIT;
            fixtureDef.filter.maskBits = PLAYER_BIT;
            body.createFixture(fixtureDef).setUserData("sign");
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
}
