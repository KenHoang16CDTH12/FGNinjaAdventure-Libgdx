package com.fgdev.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fgdev.game.FGDevMain;
import com.fgdev.game.scenes.Hud;
import com.fgdev.game.util.Constants;

public class LevelScreen implements Screen {
    private static final String TAG = LevelScreen.class.getName();
    private FGDevMain game;
    private Hud hud;
    private OrthographicCamera camera;
    private Viewport gamePort;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Debug
    private boolean debug = true;
    private ShapeRenderer debugRenderer;

    public LevelScreen(FGDevMain game) {
        this.game = game;
        // Init
        init();
    }

    private void init() {
        //Create our game HUD for score/timers/level info
        hud = new Hud(game.batch);
        // Create cam used to follow mario through cam world
        camera = new OrthographicCamera();
        // Create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM, camera);
        // Initially set our camera to be centered correctly at the start of
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        // Init map
        initMap();
        debugRenderer = new ShapeRenderer();
    }

    private void initMap() {
        // Load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(Constants.LEVEL_01);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Combined
        game.batch.setProjectionMatrix(camera.combined);
        // Separate our update logic from render
        update(delta);
        // Render World
        renderWorld();
        // Render GUI
        renderGUI();
        // Render debug rectangles
        if (debug) renderDebug();
    }

    public void update(float deltaTime) {
        // update
        hud.update(deltaTime);
        // update our camera with correct coordinates after changes
        camera.update();
    }

    private void renderWorld() {
        // set the TiledMapRenderer view based on what the
        // camera sees, and render the map
        renderer.setView(camera);
        renderer.render();
    }

    private void renderGUI() {
        // Hud render
        hud.render();
    }

    private void renderDebug () {
        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);

        debugRenderer.setColor(Color.RED);
        //player
        //debugRenderer.rect(koala.position.x, koala.position.y, Koala.WIDTH, Koala.HEIGHT);
//
//        debugRenderer.setColor(Color.YELLOW);
//        TiledMapTileLayer layer = map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class);
//        for (int y = 0; y <= layer.getHeight(); y++) {
//            for (int x = 0; x <= layer.getWidth(); x++) {
//                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
//                if (cell != null) {
//                    if (camera.frustum.boundsInFrustum(x + 0.5f, y + 0.5f, 0, 1, 1, 0))
//                        debugRenderer.rect(x, y, 1, 1);
//                }
//            }
//        }
        debugRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        // hud resize
        hud.resize(width, height);
        // updated our game viewport
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        hud.dispose();
        renderer.dispose();
    }
}
