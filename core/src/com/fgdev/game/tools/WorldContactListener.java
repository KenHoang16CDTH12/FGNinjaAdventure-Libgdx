package com.fgdev.game.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.entitiles.items.ItemTileObject;
import com.fgdev.game.entitiles.statics.StaticTileObject;

import static com.fgdev.game.util.Constants.*;

public class WorldContactListener implements ContactListener {

    public WorldContactListener() {
        super();
    }

    // called when two fixtures start to collide
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef) {
            case  PLAYER_BIT | COIN_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((ItemTileObject) fixB.getUserData()).onHit((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((ItemTileObject) fixA.getUserData()).onHit((Player) fixB.getUserData());
                break;
            case  PLAYER_BIT | FEATHER_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT)
                    ((ItemTileObject) fixB.getUserData()).onHit((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == PLAYER_BIT))
                    ((ItemTileObject) fixA.getUserData()).onHit((Player) fixB.getUserData());
                break;
            case  ATTACK_BIT | CRATE_BIT:
                if (fixA.getFilterData().categoryBits == ATTACK_BIT)
                    ((ItemTileObject) fixB.getUserData()).onHit((Player) fixA.getUserData());
                else if ((fixB.getFilterData().categoryBits == ATTACK_BIT))
                    ((ItemTileObject) fixA.getUserData()).onHit((Player) fixB.getUserData());
                break;
        }
    }

    // called when two fixtures no longer collide
    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null) return;
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
