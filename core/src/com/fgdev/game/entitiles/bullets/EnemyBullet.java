package com.fgdev.game.entitiles.bullets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.BodyFactory;

import static com.fgdev.game.Constants.PPM;

public class EnemyBullet extends Bullet {

    private Animation robotBullet;

    public EnemyBullet(World world, float x, float y, boolean isDirectionRight) {
        super(world);
        this.x = x;
        this.y = y;
        this.isDirectionRight = isDirectionRight;
        robotBullet = Assets.instance.robot.animBullet;
        init();
        setRegion((TextureRegion) robotBullet.getKeyFrame(stateTime));
        setBounds(x, y, 22 * 2 / PPM, 18 * 2 / PPM);
    }

    @Override
    protected void defineBullet() {
        body = bodyFactory.makeBoxPolyBody(
                isDirectionRight ? x : x - 1.50f,
                y,
                22 / PPM,
                18 / PPM,
                BodyFactory.BULLET,
                BodyDef.BodyType.KinematicBody,
                this
        );
        body.setLinearVelocity(new Vector2(isDirectionRight ? 8 : -8,0));
    }


    @Override
    public void update(float dt) {
        super.update(dt);
        setRegion(getFrame(dt));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region = (TextureRegion) robotBullet.getKeyFrame(stateTime, true);
        //if region is running left and the texture isnt facing left... flip it.
        if ((body.getLinearVelocity().x < 0 || !isDirectionRight) && !region.isFlipX()) {
            region.flip(true, false);
            isDirectionRight = false;
        }
        //if region is running right and the texture isnt facing right... flip it.
        else if ((body.getLinearVelocity().x > 0 || isDirectionRight) && region.isFlipX()) {
            region.flip(true, false);
            isDirectionRight = true;
        }
        return region;
    }

}
