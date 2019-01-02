package com.fgdev.game.entitiles.tiles.platform;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.utils.BodyFactory;

import static com.fgdev.game.Constants.PPM;

public class Ground extends PlatformObject implements Pool.Poolable {

    public Ground(World world) {
        super(world);
    }

    public void init(MapObject mapObject) {
        this.object = mapObject;
        init();
    }

    @Override
    public void reset() {
    }

    @Override
    protected void definePlatform() {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        body = bodyFactory.makeBoxPolyBody(
                (rect.getX() + rect.getWidth() / 2) / PPM,
                (rect.getY() + rect.getHeight() / 2) / PPM,
                rect.getWidth() / 2 / PPM,
                rect.getHeight() / 2 / PPM,
                BodyFactory.GROUND,
                BodyDef.BodyType.StaticBody,
                this
        );
    }
}
