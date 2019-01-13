package com.fgdev.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.fgdev.game.screens.DirectedGame;
import com.fgdev.game.screens.MenuScreen;
import com.fgdev.game.screens.transitions.ScreenTransition;
import com.fgdev.game.screens.transitions.ScreenTransitionFade;
import com.fgdev.game.screens.transitions.ScreenTransitionSlice;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.GamePreferences;

public class FGDevMain extends DirectedGame {

	private static final String TAG = FGDevMain.class.getName();

	public static final String GAME_TITLE = "FGDev | Ninja Adventure 1.0 [fps: %s]";

	@Override
	public void create () {
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
		// Start game at menu screen
		setScreen(new MenuScreen(FGDevMain.this));
	}

	@Override
	public void render() {
		Gdx.graphics.setTitle(String.format(GAME_TITLE, Gdx.graphics.getFramesPerSecond()));
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.instance.dispose();
	}
}
