package com.fgdev.game.entitiles.tiles.platform;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.Constants;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.BodyFactory;
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.PPM;

public class Sign extends PlatformObject implements Pool.Poolable {

    public static final int DEFAULT_SIGN = 0;
    public static final int ARROW_SIGN = 1;
    public static final int MAP_SIGN = 2;

    private int typeSign;
    private int level;

    public Sign(World world) {
        super(world);
    }

    public void init(MapObject mapObject, int typeSign, int level) {
        this.object = mapObject;
        this.typeSign = typeSign;
        this.level = level;
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
                BodyFactory.SIGN,
                BodyDef.BodyType.StaticBody,
                this
        );
    }

    public void savePos() {
        ValueManager.instance.posX = body.getPosition().x;
        ValueManager.instance.posY = body.getPosition().y;
        if (typeSign == MAP_SIGN) {
            if (level == 1) {
                // Next level 2
                ValueManager.instance.mapPath = Constants.LEVEL_02;
                ValueManager.instance.background = Assets.instance.textures.background2;
                ValueManager.instance.isNextLevel = true;
            } else if (level == 2) {
                // Next level 3
                ValueManager.instance.background = Assets.instance.textures.background2;
                ValueManager.instance.isNextLevel = true;
            } else {
                ValueManager.instance.background = Assets.instance.textures.background;
            }
        }
    }
}
