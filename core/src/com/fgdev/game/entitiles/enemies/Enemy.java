package com.fgdev.game.entitiles.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.fgdev.game.Constants;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.entitiles.bullets.EnemyBullet;
import com.fgdev.game.entitiles.bullets.SpawningBullet;
import com.fgdev.game.entitiles.tiles.item.ItemObject;
import com.fgdev.game.helpers.ScoreIndicator;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.AudioManager;
import com.fgdev.game.utils.BodyFactory;
import com.fgdev.game.utils.ValueManager;

import java.util.LinkedList;

public abstract class Enemy extends Sprite {
    protected World world;
    protected Body body;
    protected BodyFactory bodyFactory;
    protected MapObject mapObject;
    protected int type;
    protected ScoreIndicator scoreIndicator;

    protected float speed;
    protected boolean runningRight;
    protected float stateTimer;

    protected boolean toBeDestroyed;
    protected boolean destroyed;

    protected boolean isDead;

    protected float timeDelayDie = 3;

    protected Array<EnemyBullet> bullets;
    protected LinkedList<SpawningBullet> bulletSpawnQueue;

    public Enemy(World world, ScoreIndicator scoreIndicator) {
        this.world = world;
        this.scoreIndicator = scoreIndicator;
        bodyFactory = BodyFactory.getInstance(world);
        // for spawning bullets
        bullets = new Array<EnemyBullet>();
        bulletSpawnQueue = new LinkedList<SpawningBullet>();
    }

    public void init() {
        stateTimer = 0;
        toBeDestroyed = false;
        destroyed = false;
        defineEnemy();
        body.setActive(false);
    }

    public void update(float dt) {
        if (body == null) return;
        if (isDead) {
            timeDelayDie -= dt;
            if (timeDelayDie < 0) {
                queueDestroy();
                // Sound
                ValueManager.instance.score += score();
                scoreIndicator.addScoreItem(getX(), getY(), score());
                isDead = false;
            }
        }

        if (toBeDestroyed && !destroyed) {
            if (body != null) {
                final Array<JointEdge> list = body.getJointList();
                while (list.size > 0) {
                    world.destroyJoint(list.get(0).joint);
                }
                body.setUserData(null);
                world.destroyBody(body);
                body = null;
                destroyed = true;
                setSize(0, 0);
                return;
            }
        }

        setBoundForRegion();
        setRegion(getFrame(dt));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    protected void running() {
        if (body != null) {
            checkMovingDirection();
            float velocityY = body.getLinearVelocity().y;
            if (runningRight) {
                body.setLinearVelocity(new Vector2(speed, velocityY));
            } else {
                body.setLinearVelocity(new Vector2(-speed, velocityY));
            }
        }
    }

    public abstract void init(MapObject mapObject, int type);

    protected abstract void defineEnemy();

    protected abstract void setBoundForRegion();

    protected abstract TextureRegion getFrame(float dt);

    public abstract int score();

    public void killed() {
        AudioManager.instance.play(Assets.instance.sounds.enemy_dead);
    }

    public abstract void beginAttack(Player player);

    public abstract void endAttack(Player player);

    protected void queueDestroy() {
        toBeDestroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    protected void becomeDead() {
        Filter filter;
        for (Fixture fixture : body.getFixtureList()) {
            filter = fixture.getFilterData();
            filter.maskBits = Constants.GROUND_BIT;
            fixture.setFilterData(filter);
        }
    }

    protected void checkMovingDirection() {
        Vector2 p1;
        Vector2 p2;

        RayCastCallback rayCastCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getUserData() instanceof ItemObject) {
                    return 1;
                }
                if (fraction < 1.0f && fixture.getUserData().getClass() != Player.class) {
                    runningRight = !runningRight;
                }
                return 0;
            }
        };

        if (runningRight) {
            p1 = new Vector2(body.getPosition().x + 1f, body.getPosition().y);
            p2 = new Vector2(p1).add(0.1f, 0);

            world.rayCast(rayCastCallback, p1, p2);
        }
        else {
            p1 = new Vector2(body.getPosition().x - 1f, body.getPosition().y);
            p2 = new Vector2(p1).add(-0.1f, 0);

            world.rayCast(rayCastCallback, p1, p2);
        }
    }

    protected World getWorld() {
        return world;
    }

    public void addSpawnBullet(float x, float y, boolean movingRight) {
        AudioManager.instance.play(Assets.instance.sounds.bullet_enemy);
        bulletSpawnQueue.add(new SpawningBullet(x, y, movingRight));
    }

    public void handleSpawningBullet() {
        if (bulletSpawnQueue.size() > 0) {
            SpawningBullet spawningBullet = bulletSpawnQueue.poll();
            bullets.add(new EnemyBullet(getWorld(), spawningBullet.x, spawningBullet.y, spawningBullet.movingRight));
        }
    }
}
