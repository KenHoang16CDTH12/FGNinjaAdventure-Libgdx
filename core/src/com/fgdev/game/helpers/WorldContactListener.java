package com.fgdev.game.helpers;

import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.entitiles.tiles.TileObject;

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
                    ((TileObject) fixB.getUserData()).onHit((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((TileObject) fixA.getUserData()).onHit((Player) fixB.getUserData());
                break;
            case  PLAYER_BIT | FEATHER_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((TileObject) fixB.getUserData()).onHit((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((TileObject) fixA.getUserData()).onHit((Player) fixB.getUserData());
                break;
            case  ATTACK_BIT | CRATE_BIT:
                if (fixA.getFilterData().categoryBits == ATTACK_BIT)
                    ((TileObject) fixB.getUserData()).onHit((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == ATTACK_BIT))
                    ((TileObject) fixA.getUserData()).onHit((Player) fixB.getUserData());
                break;
            case  KUNAI_BIT | CRATE_BIT:
                if (fixA.getFilterData().categoryBits == KUNAI_BIT)
                    ((TileObject) fixB.getUserData()).onHit((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == KUNAI_BIT))
                    ((TileObject) fixA.getUserData()).onHit((Player) fixB.getUserData());
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
