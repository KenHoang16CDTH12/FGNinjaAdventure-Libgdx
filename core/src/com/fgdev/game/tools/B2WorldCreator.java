package com.fgdev.game.tools;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.screens.GameScreen;
import com.fgdev.game.sprites.tileobjects.Crate;
import com.fgdev.game.sprites.tileobjects.Sign;
import com.fgdev.game.sprites.tileobjects.Ground;
import com.fgdev.game.world.WorldRenderer;

public class B2WorldCreator {

    public B2WorldCreator(WorldRenderer worldRenderer) {
        Map map = worldRenderer.getMap();
        World world = worldRenderer.getWorld();
        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        //create grounds bodies/fixtures
        for (MapObject object: map.getLayers().get("grounds").getObjects().getByType(RectangleMapObject.class)) {
            new Ground(worldRenderer, object);
        }
        //create crates bodies/fixtures
        for (MapObject object: map.getLayers().get("crates").getObjects().getByType(RectangleMapObject.class)) {
            new Crate(worldRenderer, object);
        }
        //create signs bodies/fixtures
        for (MapObject object: map.getLayers().get("signs").getObjects().getByType(RectangleMapObject.class)) {
            new Sign(worldRenderer, object);
        }
    }
}
