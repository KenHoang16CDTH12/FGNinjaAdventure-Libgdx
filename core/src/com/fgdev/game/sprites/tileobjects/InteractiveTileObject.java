package com.fgdev.game.sprites.tileobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.util.Constants;
import com.fgdev.game.world.WorldRenderer;

public abstract class InteractiveTileObject {
    protected WorldRenderer worldRenderer;
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected MapObject object;

    public InteractiveTileObject(WorldRenderer worldRenderer, MapObject object) {
        this.object = object;
        this.worldRenderer = worldRenderer;
        this.world = worldRenderer.getWorld();
        this.map = worldRenderer.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((bounds.getX() + bounds.getWidth() / 2) / Constants.PPM, (bounds.getY() + bounds.getHeight() / 2) / Constants.PPM);

        body = world.createBody(bodyDef);

        shape.setAsBox(bounds.getWidth() / 2 / Constants.PPM, bounds.getHeight() / 2 / Constants.PPM);
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
    }

    public abstract void onHeadHit();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * Constants.PPM / 16), (int) (body.getPosition().y * Constants.PPM / 16));
    }
}
