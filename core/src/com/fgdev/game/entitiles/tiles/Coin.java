package com.fgdev.game.entitiles.tiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.utils.AudioManager;
import com.fgdev.game.utils.BodyFactory;
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.*;

public class Coin extends ItemObject implements Pool.Poolable {

    private static String TAG = Coin.class.getName();

    private Animation coinAnimation;

    private float stateTimer;

    public Coin(World world, ScoreIndicator scoreIndicator) {
        super(world, scoreIndicator);
        coinAnimation = Assets.instance.goldCoin.animGoldCoin;
    }

    @Override
    public void init(MapObject object) {
        this.object = object;
        stateTimer = 0;
        setBounds(getX(), getY(), 56 / PPM, 56 / PPM);
        destroyed = false;
        // Extend method
        init();
    }

    @Override
    public void reset() {
        coinAnimation = null;
        stateTimer = 0;
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
    public void collected(Player player) {
        ValueManager.instance.score += score();
        scoreIndicator.addScoreItem(getX(), getY(), score());
        AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
        destroy();
    }

    @Override
    public int score() {
        return 80;
    }
}
