package com.fgdev.game.sprites.tileobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.fgdev.game.screens.GameScreen;
import com.fgdev.game.world.WorldRenderer;

public class Ground extends InteractiveTileObject {

    private static final String TAG = Ground.class.getName();

    public Ground(WorldRenderer worldRenderer, MapObject object) {
        super(worldRenderer, object);
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log(TAG, "Collision");
    }
}
