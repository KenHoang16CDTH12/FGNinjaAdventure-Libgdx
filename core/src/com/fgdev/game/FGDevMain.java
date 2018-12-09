package com.fgdev.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fgdev.game.screens.LevelScreen;

public class FGDevMain extends Game {

	private static final String TAG = FGDevMain.class.getName();

	public SpriteBatch batch;

	private boolean paused;

	@Override
	public void create () {
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Init batch
		batch = new SpriteBatch();
		// Game world is active on start
		paused = false;
		// Set screen
		setScreen(new LevelScreen(this));
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render () {
		//Do not update game world when paused.
		if (!paused) {
			super.render();
			// Update game world by the time that has passed
			// since last rendered frame.
		}
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}
}
