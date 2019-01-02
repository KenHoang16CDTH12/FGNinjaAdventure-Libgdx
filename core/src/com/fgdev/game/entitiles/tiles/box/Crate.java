package com.fgdev.game.entitiles.tiles.box;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.entitiles.tiles.item.Feather;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.AudioManager;
import com.fgdev.game.utils.BodyFactory;
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.*;

public class Crate extends BoxObject implements Pool.Poolable {

    private static String TAG = Feather.class.getName();

    private TextureRegion crate;

    public Crate(World world, ScoreIndicator scoreIndicator) {
        super(world, scoreIndicator);
    }

    @Override
    public void init(MapObject object) {
        this.object = object;
        crate = Assets.instance.item.crate;
        setBounds(getX(), getY(), 47 * 2 / PPM, 47 * 2 / PPM);
        // Extend Abstract
        init();
    }

    @Override
    public void reset() {
        crate = Assets.instance.item.crate;
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
        if (body != null) {
            setRegion(crate);
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        }
    }

    @Override
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
