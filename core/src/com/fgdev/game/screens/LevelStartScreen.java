package com.fgdev.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.fgdev.game.Constants;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.GamePreferences;
import com.fgdev.game.utils.ValueManager;

import static com.fgdev.game.Constants.WINDOW_HEIGHT;
import static com.fgdev.game.Constants.WINDOW_WIDTH;

public class LevelStartScreen extends AbstractGameScreen {

    private static final String TAG = LevelStartScreen.class.getName();

    private SpriteBatch batch;

    private OrthographicCamera cameraGUI;
    private AbstractGameScreen levelScreen;
    private boolean paused;
    private float timeNextScreen;

    public LevelStartScreen(DirectedGame game) {
        super(game);
    }

    public LevelStartScreen(DirectedGame game, AbstractGameScreen levelScreen) {
        this(game);
        this.levelScreen = levelScreen;
    }

    @Override
    public void render(float deltaTime) {
        if (!paused) {
            timeNextScreen -= deltaTime;
            if (timeNextScreen < 0 && ValueManager.instance.isNextLevel) {
                // switch to menu screen
                ValueManager.instance.isNextLevel = false;
                game.setScreen(new GameScreen(game));
            }
        }
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        renderGui(batch);
        batch.end();
    }

    private void renderGui(SpriteBatch batch) {
        // draw collected gold coins icon + text
        // (anchored to top left edge)
        renderGuiScore(batch);
        // draw level
        renderLevel(batch);
        // draw extra lives icon + text (anchored to top right edge)
        renderGuiExtraLive(batch);
    }

    private void renderLevel(SpriteBatch batch) {
        float x = WINDOW_WIDTH / 2;
        float y = WINDOW_HEIGHT / 2;
        Assets.instance.fonts.textFontNormal.draw(batch,
                "Level " + (int) ValueManager.instance.levelCurrent, x - 50, y - 50);
    }

    @Override
    public void resize(int width, int height) {
        cameraGUI.viewportHeight = Constants.WINDOW_HEIGHT;
        cameraGUI.viewportWidth = (Constants.WINDOW_HEIGHT / (float) height) * (float) width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }

    @Override
    public void show() {
        timeNextScreen = 3;
        batch = new SpriteBatch();
        // Camera gui
        cameraGUI = new OrthographicCamera(WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
        Gdx.input.setCatchBackKey(true);
    }

    private void renderGuiScore(SpriteBatch batch) {
        float x = -15;
        float y = -15;
        float offsetX = 50;
        float offsetY = 50;
        if (ValueManager.instance.scoreVisual < ValueManager.instance.score) {
            long shakeAlpha = System.currentTimeMillis() % 360;
            float shakeDist = 1.5f;
            offsetX += MathUtils.sinDeg(shakeAlpha * 2.2f) * shakeDist;
            offsetY += MathUtils.sinDeg(shakeAlpha * 2.9f) * shakeDist;
        }
        batch.draw(Assets.instance.goldCoin.goldCoin, x, y, offsetX, offsetY, WINDOW_HEIGHT / 6.3f,
                WINDOW_HEIGHT / 6.3f, 0.35f, -0.35f, 0);
        Assets.instance.fonts.textFontNormal.draw(batch,
                "" + (int) ValueManager.instance.scoreVisual, x + 75, y + 40);
    }

    private void renderGuiExtraLive(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
        float y = -15;
        for (int i = 0; i < Constants.LIVES_START; i++) {
            if (ValueManager.instance.lives <= i) batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            batch.draw(GamePreferences.instance.isGirl ? Assets.instance.playerGirl.head
                            : Assets.instance.playerBoy.head, x + i * 50, y, 50, 50,
                    WINDOW_HEIGHT / 5.3f,
                    WINDOW_HEIGHT / 5.3f, 0.35f, -0.35f, 0);
            batch.setColor(1, 1, 1, 1);
        }
        if (ValueManager.instance.lives >= 0
                && ValueManager.instance.livesVisual > ValueManager.instance.lives) {
            int i = ValueManager.instance.lives;
            float alphaColor = Math.max(0,
                    ValueManager.instance.livesVisual - ValueManager.instance.lives - 0.5f);
            float alphaScale = 0.35f
                    * (2 + ValueManager.instance.lives - ValueManager.instance.livesVisual)
                    * 2;
            float alphaRotate = -45 * alphaColor;
            batch.setColor(1.0f, 0.7f, 0.7f, alphaColor);
            batch.draw(GamePreferences.instance.isGirl ? Assets.instance.playerGirl.head
                            : Assets.instance.playerBoy.head, x + i * 50, y, 50, 50,
                    WINDOW_HEIGHT / 5.3f,
                    WINDOW_HEIGHT / 5.3f, alphaScale, -alphaScale, alphaRotate);
            batch.setColor(1, 1, 1, 1);
        }
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
        batch.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }
}
