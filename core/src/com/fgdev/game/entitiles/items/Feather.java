package com.fgdev.game.entitiles.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.util.Assets;
import com.fgdev.game.util.AudioManager;
import com.fgdev.game.util.ValueManager;

import static com.fgdev.game.util.Constants.*;

public class Feather extends ItemTileObject {

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
        //create body and fixture variables
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((rect.x + rect.width / 2) / PPM, (rect.y + rect.height / 2) / PPM);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = FEATHER_BIT;
        fixtureDef.filter.maskBits = PLAYER_BIT;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(this);
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
        return 400;
    }
}
