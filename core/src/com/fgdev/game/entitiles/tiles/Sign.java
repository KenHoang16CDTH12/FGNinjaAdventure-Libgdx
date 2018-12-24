package com.fgdev.game.entitiles.tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.BodyFactory;

import static com.fgdev.game.Constants.PPM;

public class Sign extends PlatformObject {

    public Sign(World world, MapObject object) {
        super(world, object);
    }

    @Override
    protected void definePlatform() {
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
}
