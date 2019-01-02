package com.fgdev.game.entitiles.bullets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.BodyFactory;

import static com.fgdev.game.Constants.*;

public class Kunai extends Bullet {

    private TextureAtlas.AtlasRegion kunai;

    public Kunai(World world, float x, float y, boolean isDirectionRight) {
        super(world);
        this.x = x;
        this.y = y;
        this.isDirectionRight = isDirectionRight;
        kunai = Assets.instance.playerGirl.kunai;
        init();
        setRegion(kunai);
        setBounds(x, y, 54 * 2 / PPM, 12 * 2 / PPM);
    }

    @Override
    protected void defineBullet() {
        body = bodyFactory.makeBoxPolyBody(
                x,
                y,
                54 / PPM,
                12 / PPM,
                BodyFactory.KUNAI,
                BodyDef.BodyType.KinematicBody,
                this
        );
        body.setLinearVelocity(new Vector2(isDirectionRight ? 10 : -10,0));
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setRegion(getFrame(dt));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    private TextureRegion getFrame(float dt) {
        //if kunai is running left and the texture isnt facing left... flip it.
        if ((body.getLinearVelocity().x < 0 || !isDirectionRight) && !kunai.isFlipX()) {
            kunai.flip(true, false);
            isDirectionRight = false;
        }
        //if kunai is running right and the texture isnt facing right... flip it.
        else if ((body.getLinearVelocity().x > 0 || isDirectionRight) && kunai.isFlipX()) {
            kunai.flip(true, false);
            isDirectionRight = true;
        }
        return kunai;
    }

}
