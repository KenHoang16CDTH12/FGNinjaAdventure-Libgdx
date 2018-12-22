package com.fgdev.game.worlds;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.fgdev.game.entitiles.Player;
import com.fgdev.game.entitiles.enemies.Zombie;
import com.fgdev.game.entitiles.tiles.Coin;
import com.fgdev.game.entitiles.tiles.Crate;
import com.fgdev.game.entitiles.tiles.Feather;
import com.fgdev.game.entitiles.objects.Clouds;
import com.fgdev.game.helpers.BackgroundTiledMapRenderer;
import com.fgdev.game.screens.DirectedGame;
import com.fgdev.game.screens.GameScreen;
import com.fgdev.game.screens.MenuScreen;
import com.fgdev.game.screens.transitions.ScreenTransition;
import com.fgdev.game.screens.transitions.ScreenTransitionFade;
import com.fgdev.game.helpers.B2WorldCreator;
import com.fgdev.game.helpers.WorldContactListener;
import com.fgdev.game.utils.*;

import static com.fgdev.game.Constants.*;

public class WorldController extends InputAdapter implements Disposable {

    private static final String TAG = WorldController.class.getName();

    private DirectedGame game;

    public World world;
    // Tiled map variables
    public TmxMapLoader mapLoader;
    public TiledMap map;
    public OrthogonalTiledMapRenderer renderer;
    // Box2d variables
    public Box2DDebugRenderer b2dr;
    public B2WorldCreator creator;
    // Objects
    public Player player;
    // decoration
    public Clouds clouds;
    // accumulator
    public float accumulator;
    // background
    Texture background;
    public WorldController(DirectedGame game) {
        this.game = game;
        init();
    }

    private void init() {
        ValueManager.instance.init();
        accumulator = 0;
        initMap();
        initLevel();
    }

    private void initMap() {
        // Init world
        world = new World(GRAVITY, true);
        world.setContactListener(new WorldContactListener());
        // Load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(LEVEL_01);

        background = Assets.instance.textures.background;

        renderer = new BackgroundTiledMapRenderer(map, 1 / PPM , background);
        // renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(world, map);
        // decoration
        clouds = new Clouds(V_WIDTH);
        clouds.position.set(0, 2);

    }

    private void initLevel () {
        player = new Player(world);
    }


    private void initAgain () {
        world.destroyBody(player.getBody());
        player = new Player(world);
    }

    public void update (float deltaTime) {
        // Box2D world step
        accumulator += Math.min(deltaTime, 0.25f);
        while (accumulator >= TIME_STEP) {
            accumulator -= TIME_STEP;
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
        // Check game over
        if (ValueManager.instance.isGameOver()) {
            ValueManager.instance.timeLeftGameOverDelay -= deltaTime;
            if (ValueManager.instance.timeLeftGameOverDelay < 0) backToMenu();
        } else {
            handleInput(deltaTime);
        }
        // Handle Debug Input
        // handleDebugInput(deltaTime);
        // Update player
        player.update(deltaTime);
        // Update clouds
        clouds.update(deltaTime);
        // Update object
        updateTile(deltaTime);
        // Update box2d

        if (ValueManager.instance.livesVisual > ValueManager.instance.lives)
            ValueManager.instance.livesVisual = Math.max(ValueManager.instance.lives, ValueManager.instance.livesVisual - 1 * deltaTime);
        if (ValueManager.instance.scoreVisual < ValueManager.instance.score)
            ValueManager.instance.scoreVisual = Math.min(ValueManager.instance.score, ValueManager.instance.scoreVisual + 250 * deltaTime);
        if (!ValueManager.instance.isGameOver() && isPlayerFalling()) {
            AudioManager.instance.play(Assets.instance.sounds.liveLost);
            ValueManager.instance.lives--;
            if (ValueManager.instance.isGameOver())
                ValueManager.instance.timeLeftGameOverDelay = TIME_DELAY_GAME_OVER;
            else
                initAgain();
        }
    }

    private void updateTile(float deltaTime) {
        // Update crates
        for(Crate crate: creator.getCrates()) {
            crate.update(deltaTime);
            if (crate.getX() < player.getX() + 1250 / PPM)
                crate.getBody().setActive(true);
        }
        // Update coins
        for(Coin coin: creator.getCoins()) {
            coin.update(deltaTime);
            if (coin.getX() < player.getX() + 1250 / PPM)
                coin.getBody().setActive(true);
        }
        // Update feathers
        for(Feather feather: creator.getFeathers()) {
            feather.update(deltaTime);
            if (feather.getX() < player.getX() + 1250 / PPM)
                feather.getBody().setActive(true);
        }
        // Update zombies
        for(Zombie zombie: creator.getZombies()) {
            zombie.update(deltaTime);
            if (zombie.getX() < player.getX() + 1250 / PPM)
                zombie.getBody().setActive(true);
        }
    }

    private void backToMenu () {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                // switch to menu screen
                ScreenTransition transition = ScreenTransitionFade.init(0.75f);
                game.setScreen(new MenuScreen(game), transition);
            }
        });
    }

    private void handleInput(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.left();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.right();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.jump();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.down();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            player.attack();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            player.attackThrow();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            player.climb();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            player.jumpThrow();
        }
    }

    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        // Selected Sprite Controls
        float sprMoveSpeed = 5 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {}
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {}
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {}
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {}
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

        }
        // Back to Menu
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            backToMenu();
        }
        return false;
    }

    public boolean isPlayerFalling () {
        return player.getPosition().y < -5;
    }

    @Override
    public void dispose() {
        map.dispose();
        world.dispose();
        renderer.dispose();
        b2dr.dispose();
        background.dispose();
    }
}
