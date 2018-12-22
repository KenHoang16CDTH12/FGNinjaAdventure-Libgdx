package com.fgdev.game.entitiles.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.utils.AudioManager;
import com.fgdev.game.utils.BodyFactory;
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.*;

public class Coin extends TileObject {

    private static String TAG = Coin.class.getName();

    private Animation coinAnimation;

    private float stateTimer;

    public Coin(World world, MapObject object) {
        super(world, object);
        coinAnimation = Assets.instance.goldCoin.animGoldCoin;
        stateTimer = 0;
        setBounds(getX(), getY(), 56 / PPM, 56 / PPM);
        destroyed = false;
    }

    @Override
    protected void defineObject() {
        Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
        body = bodyFactory.makeCirclePolyBody(
                (ellipse.x + ellipse.width / 2) / PPM,
                (ellipse.y + ellipse.height / 2) / PPM,
                28 / PPM,
                BodyFactory.COIN,
                BodyDef.BodyType.StaticBody,
                this
        );
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        stateTimer += dt;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) coinAnimation.getKeyFrame(stateTimer));
    }

    @Override
    public void onHit(Player player) {
        Gdx.app.log(TAG, "Collision");
        ValueManager.instance.score += score();
        AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
        destroy();
    }

    @Override
    public int score() {
        return 100;
    }
}
