package com.fgdev.game.entitiles.statics;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.fgdev.game.entitiles.Player;

import static com.fgdev.game.util.Constants.PPM;

public abstract class StaticTileObject {
    protected World world;
    protected Body body;
    protected Map map;
    protected MapObject object;
    protected Fixture fixture;

    public StaticTileObject(World world, MapObject object, Map map) {
        this.world = world;
        this.object = object;
        this.map = map;
        defineObject();
    }

    protected abstract void defineObject();

    public abstract void onHit(Player player);

    public abstract int score();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        return layer.getCell((int) (body.getPosition().x * PPM / 16), (int) (body.getPosition().y * PPM/ 16));
    }

    public Body getBody() {
        return body;
    }
}
