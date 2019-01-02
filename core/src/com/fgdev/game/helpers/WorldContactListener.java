package com.fgdev.game.helpers;

import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.entitiles.bullets.Bullet;
import com.fgdev.game.entitiles.bullets.EnemyBullet;
import com.fgdev.game.entitiles.bullets.Kunai;
import com.fgdev.game.entitiles.enemies.Enemy;
import com.fgdev.game.entitiles.tiles.box.Crate;
import com.fgdev.game.entitiles.tiles.item.ItemObject;
import com.fgdev.game.entitiles.tiles.platform.Sign;
import com.fgdev.game.entitiles.tiles.platform.Spike;

import static com.fgdev.game.Constants.*;

public class WorldContactListener implements ContactListener {

    public WorldContactListener() {
        super();
    }

    // called when two fixtures start to collide
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA == null || fixB == null) return;

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef) {
            case  GROUND_BIT | PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == GROUND_BIT)
                    ((Player) fixB.getUserData()).setOnGround(true);
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((Player) fixA.getUserData()).setOnGround(true);
                break;
            case  PLAYER_BIT | ITEM_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((ItemObject) fixB.getUserData()).collected((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((ItemObject) fixA.getUserData()).collected((Player) fixB.getUserData());
                break;
            case  ATTACK_BIT | CRATE_BIT:
                if (fixA.getFilterData().categoryBits == ATTACK_BIT)
                    ((Crate) fixB.getUserData()).destroyBox();
                else if ((fixB.getFilterData().categoryBits == ATTACK_BIT))
                    ((Crate) fixA.getUserData()).destroyBox();
                break;
            case  KUNAI_BIT | CRATE_BIT:
                if (fixA.getFilterData().categoryBits == KUNAI_BIT)
                    ((Crate) fixB.getUserData()).destroyBox();
                else if ((fixB.getFilterData().categoryBits == KUNAI_BIT))
                    ((Crate) fixA.getUserData()).destroyBox();
                if (fixA.getFilterData().categoryBits == CRATE_BIT)
                    ((Bullet) fixB.getUserData()).setToDestroy();
                else if (fixB.getFilterData().categoryBits == CRATE_BIT)
                    ((Bullet) fixA.getUserData()).setToDestroy();
                break;
            case  BULLET_BIT | CRATE_BIT:
                if (fixA.getFilterData().categoryBits == BULLET_BIT)
                    ((Crate) fixB.getUserData()).setToDestroy();
                else if ((fixB.getFilterData().categoryBits == BULLET_BIT))
                    ((Crate) fixA.getUserData()).setToDestroy();
                if (fixA.getFilterData().categoryBits == CRATE_BIT)
                    ((Bullet) fixB.getUserData()).setToDestroy();
                else if (fixB.getFilterData().categoryBits == CRATE_BIT)
                    ((Bullet) fixA.getUserData()).setToDestroy();
                break;
            case  ATTACK_BIT | ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == ATTACK_BIT)
                    ((Enemy) fixB.getUserData()).killed();
                else if ((fixB.getFilterData().categoryBits == ATTACK_BIT))
                    ((Enemy) fixA.getUserData()).killed();
                break;
            case  KUNAI_BIT | ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == KUNAI_BIT)
                    ((Enemy) fixB.getUserData()).killed();
                else if ((fixB.getFilterData().categoryBits == KUNAI_BIT))
                    ((Enemy) fixA.getUserData()).killed();
                if (fixA.getFilterData().categoryBits != KUNAI_BIT)
                    ((Kunai) fixB.getUserData()).setToDestroy();
                else if (fixB.getFilterData().categoryBits != KUNAI_BIT)
                    ((Kunai) fixA.getUserData()).setToDestroy();
                break;
            case  PLAYER_BIT | ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((Enemy) fixB.getUserData()).beginAttack((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((Enemy) fixA.getUserData()).beginAttack((Player) fixB.getUserData());
                break;
            case  PLAYER_BIT | SPIKE_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((Spike) fixB.getUserData()).killPlayer((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((Spike) fixA.getUserData()).killPlayer((Player) fixB.getUserData());
                break;
            case  PLAYER_BIT | BULLET_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((Bullet) fixB.getUserData()).killPlayer((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((Bullet) fixA.getUserData()).killPlayer((Player) fixB.getUserData());
                break;
            case  PLAYER_BIT | SIGN_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((Sign) fixB.getUserData()).savePos();
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((Sign) fixA.getUserData()).savePos();
                break;
            case  PLAYER_BIT | LADDER_BIT:
                if (fixA.getFilterData().categoryBits == LADDER_BIT)
                    ((Player) fixB.getUserData()).setOnLadder(true);
                else if ((fixB.getFilterData().categoryBits == LADDER_BIT))
                    ((Player) fixA.getUserData()).setOnLadder(true);
                break;
            case  BULLET_BIT | KUNAI_BIT:
                if (fixA.getFilterData().categoryBits == BULLET_BIT)
                    ((Kunai) fixB.getUserData()).setToDestroy();
                else if ((fixB.getFilterData().categoryBits == BULLET_BIT))
                    ((Kunai) fixA.getUserData()).setToDestroy();
                if (fixA.getFilterData().categoryBits == KUNAI_BIT)
                    ((EnemyBullet) fixB.getUserData()).setToDestroy();
                else if (fixB.getFilterData().categoryBits == KUNAI_BIT)
                    ((EnemyBullet) fixA.getUserData()).setToDestroy();
                break;
        }
    }

    // called when two fixtures no longer collide
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA == null || fixB == null) return;

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case  GROUND_BIT | PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == GROUND_BIT)
                    ((Player) fixB.getUserData()).setOnGround(false);
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((Player) fixA.getUserData()).setOnGround(false);
                break;
            case  PLAYER_BIT | ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((Enemy) fixB.getUserData()).endAttack((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((Enemy) fixA.getUserData()).endAttack((Player) fixB.getUserData());
                break;
            case  PLAYER_BIT | LADDER_BIT:
                if (fixA.getFilterData().categoryBits == LADDER_BIT)
                    ((Player) fixB.getUserData()).setOnLadder(false);
                else if ((fixB.getFilterData().categoryBits == LADDER_BIT))
                    ((Player) fixA.getUserData()).setOnLadder(false);
                break;
        }
    }

    // collision detection
    // pre solve
    // collision handling
    // post solve
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
