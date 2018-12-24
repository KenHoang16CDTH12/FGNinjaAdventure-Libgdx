package com.fgdev.game.entitiles.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.AudioManager;
import com.fgdev.game.utils.BodyFactory;
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.*;

public class Feather extends ItemObject {

    private static String TAG = Feather.class.getName();

    private TextureRegion feather;

    public Feather(World world, MapObject object, ScoreIndicator scoreIndicator) {
        super(world, object, scoreIndicator);
        feather = Assets.instance.feather.feather;
        setBounds(getX(), getY(), 40 * 2 / PPM, 40 * 2/ PPM);
        destroyed = false;
    }

    @Override
    protected void defineObject() {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        body = bodyFactory.makeBoxPolyBody((rect.x + rect.width / 2) / PPM,
                (rect.y + rect.height / 2) / PPM,
                40 / PPM,
                40 / PPM,
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
    public void collected(Player player) {
        ValueManager.instance.score += score();
        scoreIndicator.addScoreItem(getX(), getY(), score());
        AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
        player.setFeatherPowerup(true);
        destroy();
    }

    @Override
    public int score() {
        return 100;
    }
}
