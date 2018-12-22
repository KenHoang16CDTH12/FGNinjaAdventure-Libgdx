package com.fgdev.game.entitiles.tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.utils.BodyFactory;

import static com.fgdev.game.Constants.PPM;

public class Sign extends TileObject {

    public Sign(World world, MapObject object) {
        super(world, object);
        body.setActive(true);
    }

    @Override
    protected void defineObject() {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        body = bodyFactory.makeBoxPolyBody(
                (rect.getX() + rect.getWidth() / 2) / PPM,
                (rect.getY() + rect.getHeight() / 2) / PPM,
                rect.getWidth() / 2 / PPM,
                rect.getHeight() / 2 / PPM, BodyFactory.SIGN,
                BodyDef.BodyType.StaticBody,
                this
        );
    }

    // Sign object not use
    @Override
    public void onHit(Player player) {

    }

    // Sign object not use
    @Override
    public int score() {
        return 0;
    }
}
