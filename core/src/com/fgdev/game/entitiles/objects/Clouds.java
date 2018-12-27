package com.fgdev.game.entitiles.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.fgdev.game.entitiles.bullets.Kunai;
import com.fgdev.game.utils.Assets;

public class Clouds extends GameObject {

    private float length;

    private Array<TextureRegion> regClouds;

    // array containing the active kunaies.
    private Array<Cloud> activeClouds = new Array<Cloud>();

    // cloud pool.
    private final Pool<Cloud> cloudPool = new Pool<Cloud>() {
        @Override
        protected Cloud newObject() {
            return new Cloud();
        }
    };

    public Clouds(float length) {
        this.length = length;
        init();
    }

    private void init() {
        dimension.set(3.0f, 1.5f);
        regClouds = new Array<TextureRegion>();
        regClouds.add(Assets.instance.assetObjectDecoration.cloud01);
        regClouds.add(Assets.instance.assetObjectDecoration.cloud02);
        regClouds.add(Assets.instance.assetObjectDecoration.cloud03);

        int distFac = 5;
        int numClouds = (int)(length / distFac);
        activeClouds = new Array<Cloud>(2 * numClouds);
        for (int i = 0; i < numClouds; i++) {
            // if you want to spawn a new bullet:
            Cloud item = cloudPool.obtain();
            item.init(dimension, regClouds.random(), length);
            item.position.x = i * distFac;
            activeClouds.add(item);
        }
    }

    @Override
    public void update(float deltaTime) {
        Cloud item;
        int len = activeClouds.size;
        for (int i = len; --i >= 0;) {
            item = activeClouds.get(i);
            item.update(deltaTime);
            if (item.position.x < -10) {
                activeClouds.removeIndex(i);
                cloudPool.free(item);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Cloud item;
        int len = activeClouds.size;
        for (int i = len; --i >= 0;) {
            item = activeClouds.get(i);
            if (item.position.x > -10) {
                item.render(batch);
            }
        }
    }

    private class Cloud extends GameObject implements Pool.Poolable {

        private TextureRegion regCloud;

        private Cloud() {

        }

        public void init(Vector2 dimension, TextureRegion regCloud, float length) {
            this.dimension.set(dimension);
            // select random cloud image
            this.setRegion(regCloud);
            // position
            Vector2 pos = new Vector2();
            pos.x = length + 10; // position after end of level
            pos.y += 8.75; // base position
            pos.y += MathUtils.random(0.0f, - 3.0f)
                    * (MathUtils.randomBoolean() ? 1 : -1); // random additional position
            this.position.set(pos);
            // speed
            Vector2 speed = new Vector2();
            speed.x += 0.5f; // base speed
            // random additional speed
            speed.x += MathUtils.random(0.0f, 0.75f);
            this.terminalVelocity.set(speed);
            speed.x *= -1; // move left
            this.velocity.set(speed);
        }

        private void setRegion (TextureRegion region) {
            regCloud = region;
        }

        @Override
        public void render(SpriteBatch batch) {
            TextureRegion reg = regCloud;
            batch.draw(reg.getTexture(), position.x + origin.x,
                    position.y + origin.y, origin.x, origin.y, dimension.x,
                    dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
                    reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
                    false, false);
        }

        @Override
        public void reset() {
            this.dimension.set(0, 0);
            // select random cloud image
            this.setRegion(null);
            // position
            this.position.set(0, 0);
            // speed
            this.terminalVelocity.set(0, 0);
            this.velocity.set(0, 0);
        }
    }
}
