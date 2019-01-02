package com.fgdev.game.entitiles.bullets;

public class SpawningBullet {
    public float x;
    public float y;
    public boolean movingRight;

    public SpawningBullet(float x, float y, boolean movingRight) {
        this.x = x;
        this.y = y;
        this.movingRight = movingRight;
    }
}
