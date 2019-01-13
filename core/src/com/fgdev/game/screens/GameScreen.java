package com.fgdev.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.fgdev.game.utils.GamePreferences;
import com.fgdev.game.logics.GameScreenLogic;

public class GameScreen extends AbstractGameScreen {

    private static final String TAG = GameScreen.class.getName();
    
    private GameScreenLogic gameScreenLogic;
    private boolean paused;

    public GameScreen (DirectedGame game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        if (!paused) {
            // Update game world by the time that has passed
            // since last rendered frame.
            gameScreenLogic.update(deltaTime);
        }
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Do not update game world when paused.
        gameScreenLogic.render();
    }

    @Override
    public void show() {
        GamePreferences.instance.load();
        gameScreenLogic = new GameScreenLogic(game);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void resize(int width, int height) {
        gameScreenLogic.resize(width, height);
    }

    @Override
    public void pause() {
        paused = true;

    }

    @Override
    public void resume() {
        super.resume();
        // Only called on Android!
        paused = false;
    }

    @Override
    public void hide() {
        gameScreenLogic.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return gameScreenLogic.getJoystickOverlay().getStage();
    }

}
