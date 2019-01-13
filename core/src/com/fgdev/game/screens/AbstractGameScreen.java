package com.fgdev.game.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.fgdev.game.utils.Assets;

public abstract class AbstractGameScreen implements Screen {

    protected DirectedGame game;

    public AbstractGameScreen (DirectedGame game) {
        this.game = game;
    }

    public abstract InputProcessor getInputProcessor ();

    @Override
    public void resume() {
        Assets.instance.init(new AssetManager());
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }
}
