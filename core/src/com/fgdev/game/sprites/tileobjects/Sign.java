package com.fgdev.game.sprites.tileobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.fgdev.game.world.WorldRenderer;

public class Sign extends InteractiveTileObject {

    private static final String TAG = Sign.class.getName();

    public Sign(WorldRenderer worldRenderer, MapObject object) {
        super(worldRenderer, object);
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log(TAG, "Collision");
    }
}
