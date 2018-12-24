package com.fgdev.game.helpers;

import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.entitiles.bullets.Kunai;
import com.fgdev.game.entitiles.enemies.Enemy;
import com.fgdev.game.entitiles.enemies.Zombie;
import com.fgdev.game.entitiles.tiles.Crate;
import com.fgdev.game.entitiles.tiles.ItemObject;

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
            case  PLAYER_BIT | COIN_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((ItemObject) fixB.getUserData()).collected((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((ItemObject) fixA.getUserData()).collected((Player) fixB.getUserData());
                break;
            case  PLAYER_BIT | FEATHER_BIT:
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
                    ((Kunai) fixB.getUserData()).setToDestroy();
                else if (fixB.getFilterData().categoryBits == CRATE_BIT)
                    ((Kunai) fixA.getUserData()).setToDestroy();
                break;
            case  ATTACK_BIT | ZOMBIE_BIT:
                if (fixA.getFilterData().categoryBits == ATTACK_BIT)
                    ((Enemy) fixB.getUserData()).killed();
                else if ((fixB.getFilterData().categoryBits == ATTACK_BIT))
                    ((Enemy) fixA.getUserData()).killed();
                break;
            case  ATTACK_BIT | ROBOT_BIT:
                if (fixA.getFilterData().categoryBits == ATTACK_BIT)
                    ((Enemy) fixB.getUserData()).killed();
                else if ((fixB.getFilterData().categoryBits == ATTACK_BIT))
                    ((Enemy) fixA.getUserData()).killed();
                break;
            case  KUNAI_BIT | ZOMBIE_BIT:
                if (fixA.getFilterData().categoryBits == KUNAI_BIT)
                    ((Enemy) fixB.getUserData()).killed();
                else if ((fixB.getFilterData().categoryBits == KUNAI_BIT))
                    ((Enemy) fixA.getUserData()).killed();
                if (fixA.getFilterData().categoryBits == ZOMBIE_BIT)
                    ((Kunai) fixB.getUserData()).setToDestroy();
                else if (fixB.getFilterData().categoryBits == ZOMBIE_BIT)
                    ((Kunai) fixA.getUserData()).setToDestroy();
                break;
            case  KUNAI_BIT | ROBOT_BIT:
                if (fixA.getFilterData().categoryBits == KUNAI_BIT)
                    ((Enemy) fixB.getUserData()).killed();
                else if ((fixB.getFilterData().categoryBits == KUNAI_BIT))
                    ((Enemy) fixA.getUserData()).killed();
                if (fixA.getFilterData().categoryBits == ROBOT_BIT)
                    ((Kunai) fixB.getUserData()).setToDestroy();
                else if (fixB.getFilterData().categoryBits == ROBOT_BIT)
                    ((Kunai) fixA.getUserData()).setToDestroy();
                break;
            case  PLAYER_BIT | ZOMBIE_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((Zombie) fixB.getUserData()).zombieAttack((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((Zombie) fixA.getUserData()).zombieAttack((Player) fixB.getUserData());
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
            case  PLAYER_BIT | ZOMBIE_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((Zombie) fixB.getUserData()).zombieStopAttack((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((Zombie) fixA.getUserData()).zombieStopAttack((Player) fixB.getUserData());
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
