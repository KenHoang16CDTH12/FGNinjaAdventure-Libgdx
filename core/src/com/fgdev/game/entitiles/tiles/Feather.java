package com.fgdev.game.entitiles.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.AudioManager;
import com.fgdev.game.utils.BodyFactory;
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.*;

public class Feather extends TileObject {

    private static String TAG = Feather.class.getName();

    private TextureRegion feather;

    public Feather(World world, MapObject object) {
        super(world, object);
        feather = Assets.instance.feather.feather;
        setBounds(getX(), getY(), 46 * 2 / PPM, 40 * 2/ PPM);
        destroyed = false;
    }

    @Override
    protected void defineObject() {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        body = bodyFactory.makeBoxPolyBody((rect.x + rect.width / 2) / PPM,
                (rect.y + rect.height / 2) / PPM,
                rect.getWidth() / 2 / PPM,
                rect.getHeight() / 2 / PPM,
                BodyFactory.FEATHER,
                BodyDef.BodyType.StaticBody,
                this
        );
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(feather);
    }

    @Override
    public void onHit(Player player) {
        Gdx.app.log(TAG, "Collision");
        ValueManager.instance.score += score();
        AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
        player.setFeatherPowerup(true);
        destroy();
    }

    @Override
    public int score() {
        return 150;
    }
}
