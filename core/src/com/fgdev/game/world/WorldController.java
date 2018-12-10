package com.fgdev.game.world;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.Interpolation;
import com.fgdev.game.screens.DirectedGame;
import com.fgdev.game.screens.MenuScreen;
import com.fgdev.game.screens.transitions.ScreenTransition;
import com.fgdev.game.screens.transitions.ScreenTransitionSlide;
import com.fgdev.game.util.CameraHelper;
import com.fgdev.game.util.Constants;

public class WorldController extends InputAdapter {

    private static final String TAG = WorldController.class.getName();


    private DirectedGame game;

    public CameraHelper cameraHelper;

    public int lives;
    public int score;

    private float timeLeftGameOverDelay;


    public WorldController(DirectedGame game) {
        this.game = game;
        init();
    }

    private void init() {
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        timeLeftGameOverDelay = 0;
        initLevel();
    }


    public void update (float deltaTime) {
        handleDebugInput(deltaTime);
        if (isGameOver()) {
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay < 0) backToMenu();
        } else {
            // handle after
            // handleInputGame(deltaTime);
        }
        cameraHelper.update(deltaTime);

        if (isGameOver())
            timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
        else
            initLevel();
    }

    private void initLevel () {
        score = 0;
        // cameraHelper.setTarget(level.bunnyHead);
    }

    private void backToMenu () {
        // switch to menu screen
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.DOWN, false, Interpolation.bounceOut);
        game.setScreen(new MenuScreen(game), transition);
    }


    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        // Selected Sprite Controls
        float sprMoveSpeed = 5 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {}
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {}
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {}
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {}

        if (!cameraHelper.hasTarget()) {
            // Camera Controls (move)
            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camMoveSpeed *=
                    camMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-camMoveSpeed,
                    0);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(camMoveSpeed,
                    0);
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0,
                    -camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
                cameraHelper.setPosition(0, 0);
        }

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camZoomSpeed *=
                camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA))
            cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(
                -camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);
    }

    private void moveCamera(float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);

        Gdx.app.debug(TAG, "x: " + x + " y: " + y);
    }


    public boolean isGameOver () {
        return lives < 0;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Reset game world
        if (keycode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world reseted");
        }
        // Toggle camera follow
        else if (keycode == Input.Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget()
                    ? null: null);
            Gdx.app.debug(TAG, "Camera follow enabled: "
                    + cameraHelper.hasTarget());
        }
        // Back to Menu
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            backToMenu();
        }
        return false;
    }
}
