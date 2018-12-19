package com.fgdev.game.entitiles.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.util.Assets;
import com.fgdev.game.util.AudioManager;
import com.fgdev.game.util.ValueManager;

import static com.fgdev.game.util.Constants.*;

public class Crate extends ItemTileObject {

    private static String TAG = Feather.class.getName();

    private TextureRegion crate;

    public Crate(World world, MapObject object) {
        super(world, object);
        crate = Assets.instance.item.crate;
        setBounds(getX(), getY(), 47 * 2 / PPM, 47 * 2 / PPM);
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
        fixtureDef.filter.categoryBits = CRATE_BIT;
        fixtureDef.filter.maskBits = ATTACK_BIT | PLAYER_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(crate);
    }

    @Override
    public void onHit(Player player) {
        Gdx.app.log(TAG, "Attack");
        ValueManager.instance.score += score();
        AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
        destroy();
    }

    @Override
    public int score() {
        return 100;
    }
}
