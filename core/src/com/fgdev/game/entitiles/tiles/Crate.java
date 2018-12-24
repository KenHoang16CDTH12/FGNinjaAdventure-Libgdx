package com.fgdev.game.entitiles.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.AudioManager;
import com.fgdev.game.utils.BodyFactory;
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.*;

public class Crate extends BoxObject {

    private static String TAG = Feather.class.getName();

    private TextureRegion crate;

    public Crate(World world, MapObject object, ScoreIndicator scoreIndicator) {
        super(world, object, scoreIndicator);
        crate = Assets.instance.item.crate;
        setBounds(getX(), getY(), 47 * 2 / PPM, 47 * 2 / PPM);
        destroyed = false;
    }

    @Override
    protected void defineObject() {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        body = bodyFactory.makeBoxPolyBody((rect.x + rect.width / 2) / PPM,
                (rect.y + rect.height / 2) / PPM,
                47 / PPM,
                47 / PPM,
                BodyFactory.CRATE,
                BodyDef.BodyType.DynamicBody,
                this
        );
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(crate);
    }

    public void destroyBox() {
        ValueManager.instance.score += score();
        scoreIndicator.addScoreItem(getX(), getY(), score());
        AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
        destroy();
    }

    @Override
    public int score() {
        return 10;
    }
}
