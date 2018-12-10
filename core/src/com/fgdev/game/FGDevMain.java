package com.fgdev.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fgdev.game.screens.GameScreen;
import com.fgdev.game.screens.MenuScreen;

public class FGDevMain extends Game {

	private static final String TAG = FGDevMain.class.getName();

	@Override
	public void create () {
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}
