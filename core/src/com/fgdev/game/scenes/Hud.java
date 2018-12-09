package com.fgdev.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.fgdev.game.Assets;
import com.fgdev.game.util.Constants;

public class Hud implements Disposable {

    private static final String TAG = Hud.class.getName();

    private OrthographicCamera cameraGUI;
    private SpriteBatch batch;

    public Hud(SpriteBatch batch) {
        this.batch = batch;
        init();
    }

    private void init() {
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
    }

    public void render() {
        renderGui(batch);
    }

    private void renderGui (SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        // draw FPS text (anchored to bottom right edge)
        renderGuiFpsCounter(batch);
        batch.end();
    }

    public void resize(int width, int height) {
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT
                / (float)height) * (float)width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2,
                cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }


    private void renderGuiFpsCounter (SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        if (fps >= 45) {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        } else if (fps >= 30) {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        } else {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }
        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // white
    }

    public void update(float dt) {
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
