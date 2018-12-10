package com.fgdev.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fgdev.game.Assets;
import com.fgdev.game.tools.B2WorldCreator;
import com.fgdev.game.util.Constants;
import com.fgdev.game.util.GamePreferences;

public class WorldRenderer implements Disposable {

    private static final String TAG = WorldRenderer.class.getName();

    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private SpriteBatch batch;
    private WorldController worldController;
    private Viewport gamePort;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init() {
        batch = new SpriteBatch();

        // Create cam used to follow mario through cam world
        camera = new OrthographicCamera();
        // Create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM, camera);
        // Initially set our camera to be centered correctly at the start of
        camera.position.set(0, 0, 0);
        // Camera gui
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
        // Init map
        initMap();
    }


    private void initMap() {
        // Load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(Constants.LEVEL_01);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);
    }

    public void render() {
        renderWorld(batch);
        renderGui(batch);
        renderDebug();
    }

    private void renderWorld (SpriteBatch batch) {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        //takes 1 step in the physics simulation (60 times per second)
        world.step(1/60f, 6, 2);
        // update our camera with correct coordinates after changes
        camera.update();
        // set the TiledMapRenderer view based on what the
        // camera sees, and render the map
        renderer.setView(camera);
        renderer.render();
    }

    private void renderGui (SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        // draw collected gold coins icon + text
        // (anchored to top left edge)
        // renderGuiScore(batch);
        // draw extra lives icon + text (anchored to top right edge)
        // renderGuiExtraLive(batch);
        // draw FPS text (anchored to bottom right edge)
        if (GamePreferences.instance.showFpsCounter)
            renderGuiFpsCounter(batch);
        // draw Game Over
        renderGuiGameOverMessage(batch);
        // draw collected feather icon (anchored to top left edge)
        // renderGuiFeatherPowerup(batch);
        batch.end();
    }

    private void renderDebug() {
        //render our Box2DDebugLines
        b2dr.render(world, camera.combined);
    }

    public void resize(int width, int height) {
        // updated our game viewport
        gamePort.update(width, height);
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT
                / (float)height) * (float)width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2,
                cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }


    private void renderGuiScore (SpriteBatch batch) {
        float x = -15;
        float y = -15;
        //batch.draw(Assets.instance.goldCoin.goldCoin,
        //        x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
        Assets.instance.fonts.defaultBig.draw(batch,
                "" + worldController.score,
                x + 75, y + 37);
    }

    private void renderGuiExtraLive (SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
        float y = -15;
        for (int i = 0; i < Constants.LIVES_START; i++) {
            if (worldController.lives <= i)
                batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            //batch.draw(Assets.instance.bunny.head,
            //        x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
            batch.setColor(1, 1, 1, 1);
        }
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

    private void renderGuiGameOverMessage (SpriteBatch batch) {
        float x = cameraGUI.viewportWidth / 2;
        float y = cameraGUI.viewportHeight / 2;
        if (worldController.isGameOver()) {
            BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
            fontGameOver.setColor(1, 0.75f, 0.25f, 1);
            fontGameOver.draw(batch, "GAME OVER", x, y);
            fontGameOver.setColor(1, 1, 1, 1);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }
}
