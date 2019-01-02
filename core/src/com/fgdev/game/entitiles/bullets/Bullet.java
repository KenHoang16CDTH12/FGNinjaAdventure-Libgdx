package com.fgdev.game.entitiles.bullets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.utils.BodyFactory;

public abstract class Bullet extends Sprite {

    protected Body body;
    protected BodyFactory bodyFactory;
    protected float stateTime;
    protected boolean setToDestroy;
    protected boolean isDirectionRight;
    protected World world;
    protected boolean alive;
    protected float x, y;

    public Bullet(World world) {
        this.world = world;
        bodyFactory = BodyFactory.getInstance(world);
    }


    public void init() {
        stateTime = 0;
        setToDestroy = false;
        alive = true;
        defineBullet();
        body.setActive(true);
    }

    public void update(float dt) {
        stateTime += dt;
        if((stateTime > 3 || setToDestroy) && alive) {
            world.destroyBody(body);
            setBounds(0, 0, 0,0);
            setSize(0, 0);
            alive = false;
            return;
        }

        if((isDirectionRight && body.getLinearVelocity().x < 0) || (!isDirectionRight && body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isAlive() {
        return alive;
    }

    protected abstract void defineBullet();

    public void killPlayer(Player player) {
        player.playerDie();
        setToDestroy();
    }
}
