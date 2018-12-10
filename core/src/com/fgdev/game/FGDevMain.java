package com.fgdev.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.fgdev.game.screens.DirectedGame;
import com.fgdev.game.screens.MenuScreen;
import com.fgdev.game.screens.transitions.ScreenTransition;
import com.fgdev.game.screens.transitions.ScreenTransitionSlice;

public class FGDevMain extends DirectedGame {

	private static final String TAG = FGDevMain.class.getName();

	@Override
	public void create () {
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Start game at menu screen
		ScreenTransition transition = ScreenTransitionSlice.init(2,
				ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
		setScreen(new MenuScreen(this), transition);
	}
}
