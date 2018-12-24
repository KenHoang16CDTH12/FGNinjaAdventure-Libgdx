package com.fgdev.game.entitiles.bullets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.BodyFactory;

import static com.fgdev.game.Constants.*;

public class Kunai extends Sprite {

    private TextureAtlas.AtlasRegion kunai;
    private Body body;
    private BodyFactory bodyFactory;
    private float stateTime;
    private boolean destroyed;
    private boolean setToDestroy;
    private boolean isDirectionRight;
    private Player player;

    public Kunai(Player player, float x, float y, boolean isDirectionRight) {
        this.player = player;
        this.isDirectionRight = isDirectionRight;
        bodyFactory = BodyFactory.getInstance(player.getWorld());
        kunai = Assets.instance.player.kunai;
        defineKunai(x, y);
        setRegion(kunai);
        setBounds(x, y, 54 * 2 / PPM, 12 * 2 / PPM);
    }

    private void defineKunai(float x, float y) {
        body = bodyFactory.makeBoxPolyBody(
                isDirectionRight ? x : x - 1.50f,
                y,
                54 / PPM,
                12 / PPM,
                BodyFactory.KUNAI,
                BodyDef.BodyType.KinematicBody,
                this
        );
        body.setLinearVelocity(new Vector2(isDirectionRight ? 10 : -10,0));
    }

    public void update(float dt){
        stateTime += dt;
        setRegion(getFrame(dt));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        if((stateTime > 3 || setToDestroy) && !destroyed) {
            player.getWorld().destroyBody(body);
            destroyed = true;
        }

        if((isDirectionRight && body.getLinearVelocity().x < 0) || (!isDirectionRight && body.getLinearVelocity().x > 0))
            setToDestroy();
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


    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }

}
