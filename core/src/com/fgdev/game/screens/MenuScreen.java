package com.fgdev.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.fgdev.game.Constants;
import com.fgdev.game.utils.Assets;
import com.fgdev.game.utils.AudioManager;
import com.fgdev.game.utils.GamePreferences;
import com.fgdev.game.utils.ValueManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

public class MenuScreen extends AbstractGameScreen {

    private static final String TAG = MenuScreen.class.getName();

    private Stage stage;
    private Skin skinFGDev;
    private Skin skinLibgdx;
    // menu
    private Image imgBackground;
    private Image imgLogo;
    private Image imgInfo;
    private Image imgCoins;
    private Image imgCharacter;
    private Button btnMenuPlay;
    private Button btnMenuOptions;
    // options
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private CheckBox chkGirl;
    private CheckBox chkShowFpsCounter;
    private CheckBox chkUseMonoChromeShader;
    private CheckBox chkDebug;
    // debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;

    public MenuScreen (DirectedGame game) {
        super(game);
    }

    private void rebuildStage () {
        skinFGDev = new Skin(Gdx.files.internal(Constants.SKIN_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
        skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();
        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);
        stage.addActor(layerOptionsWindow);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (debugEnabled) {
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constants.WINDOW_WIDTH,
                Constants.WINDOW_HEIGHT));
        ValueManager.instance.init();
        rebuildStage();
    }

    @Override
    public void hide() {
        stage.dispose();
        skinFGDev.dispose();
        skinLibgdx.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public InputProcessor getInputProcessor () {
        return stage;
    }

    private Table buildBackgroundLayer () {
        Table layer = new Table();
        // + Background
        imgBackground = new Image(skinFGDev, "background");
        imgBackground.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        layer.add(imgBackground).size(imgBackground.getWidth(), imgBackground.getHeight());
        return layer;
    }

    private Table buildObjectsLayer () {
        Table layer = new Table();
        // + Coins
        imgCoins = new Image(skinFGDev, "coins");
        layer.addActor(imgCoins);
        imgCoins.setOrigin(imgCoins.getWidth() / 2,
                imgCoins.getHeight() / 2);
        imgCoins.addAction(sequence(
                moveTo(Constants.WINDOW_WIDTH / 9, Constants.WINDOW_HEIGHT / 5),
                scaleTo(0, 0),
                fadeOut(0),
                delay(2.5f),
                parallel(moveBy(-20, -20, 1.5f, Interpolation.fade),
                        scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
                        alpha(1.0f, 0.5f))));
        // + Bunny
        imgCharacter = new Image(skinFGDev, "character");
        layer.addActor(imgCharacter);
        imgCharacter.setPosition(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 4);
        return layer;
    }
    private Table buildLogosLayer () {
        Table layer = new Table();
        layer.left().top();
        layer.pad(10);
        // + Game Logo
        imgLogo = new Image(skinFGDev, "logo");
        layer.add(imgLogo);
        layer.row().expandY();
        // + Info Logos
        imgInfo = new Image(skinFGDev, "info");
        layer.add(imgInfo).bottom().pad(10);
        if (debugEnabled) layer.debug();
        return layer;
    }
    private Table buildControlsLayer () {
        Table layer = new Table();
        layer.right().bottom();// + Play Button
        btnMenuPlay = new Button(skinFGDev, "play");
        layer.add(btnMenuPlay);
        btnMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        layer.row();
        // + Options Button
        btnMenuOptions = new Button(skinFGDev, "options");
        layer.add(btnMenuOptions);
        btnMenuOptions.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }
        });
        if (debugEnabled) layer.debug();
        return layer;
    }
    private Table buildOptionsWindowLayer () {
        winOptions = new Window("", skinLibgdx);
        // + Audio Settings: Sound/Music CheckBox and Volume Slider
        winOptions.add(buildOptWinAudioSettings()).row();
        // + Character: Show option change character
        winOptions.add(buildOptCharacter()).row();
        // + Debug: Show FPS Counter
        winOptions.add(buildOptWinDebug()).row();
        // + Separator and Buttons (Save, Cancel)
        winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);
        // Make options window slightly transparent
        winOptions.setColor(1, 1, 1, 0.8f);
        // Hide options window by default
        showOptionsWindow(false, false);
        if (debugEnabled) winOptions.debug();
        // Let TableLayout recalculate widget sizes and positions
        winOptions.pack();
        // Move options window to bottom right corner
        winOptions.setPosition
                (Constants.WINDOW_WIDTH - winOptions.getWidth() - 50,
                        50);
        return winOptions;
    }

    private void loadSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        chkSound.setChecked(prefs.sound);
        sldSound.setValue(prefs.volSound);
        chkMusic.setChecked(prefs.music);
        sldMusic.setValue(prefs.volMusic);
        chkGirl.setChecked(prefs.isGirl);
        chkShowFpsCounter.setChecked(prefs.showFpsCounter);
        chkUseMonoChromeShader.setChecked(prefs.useMonochromeShader);
        chkDebug.setChecked(prefs.debug);
    }

    private void saveSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.sound = chkSound.isChecked();
        prefs.volSound = sldSound.getValue();
        prefs.music = chkMusic.isChecked();
        prefs.volMusic = sldMusic.getValue();
        prefs.isGirl = chkGirl.isChecked();
        prefs.showFpsCounter = chkShowFpsCounter.isChecked();
        prefs.useMonochromeShader = chkUseMonoChromeShader.isChecked();
        prefs.debug = chkDebug.isChecked();
        prefs.save();
    }

    private void onSaveClicked() {
        onCancelClicked();
        saveSettings();
    }
    private void onCancelClicked() {
        showMenuButtons(true);
        showOptionsWindow(false, true);
        AudioManager.instance.onSettingsUpdated();
    }

    private void onPlayClicked () {
        AudioManager.instance.play(Assets.instance.music.background);
        ValueManager.instance.isNextLevel = true;
        game.setScreen(new LevelStartScreen(game));
    }

    private void onOptionsClicked () {
        loadSettings();
        showMenuButtons(false);
        showOptionsWindow(true, true);
    }

    private Table buildOptWinAudioSettings () {
        Table tbl = new Table();
        // + Title: "Audio"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Audio", skinLibgdx, "font",
                Color.ORANGE)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Sound" label, sound volume slider
        chkSound = new CheckBox("", skinLibgdx);
        tbl.add(chkSound);
        tbl.add(new Label("Sound", skinLibgdx));
        sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldSound);
        tbl.row();
        // + Checkbox, "Music" label, music volume slider
        chkMusic = new CheckBox("", skinLibgdx);
        tbl.add(chkMusic);
        tbl.add(new Label("Music", skinLibgdx));
        sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldMusic);
        tbl.row();
        return tbl;
    }

    private Table buildOptCharacter () {
        Table tbl = new Table();
        // + Title: "Character"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Character", skinLibgdx, "font",
                Color.GREEN)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Change gender" label
        chkGirl = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Girl", skinLibgdx));
        tbl.add(chkGirl);
        tbl.row();
        return tbl;
    }

    private Table buildOptWinDebug () {
        Table tbl = new Table();
        // + Title: "Debug"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Debug", skinLibgdx, "font",
                Color.RED)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Show FPS Counter" label
        chkShowFpsCounter = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Show FPS Counter", skinLibgdx));
        tbl.add(chkShowFpsCounter);
        tbl.row();
        // + Checkbox, "Use Monochrome Shader" label
        chkUseMonoChromeShader = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Use Monochrome Shader", skinLibgdx));
        tbl.add(chkUseMonoChromeShader);
        tbl.row();
        // + Checkbox, "Debug" label
        chkDebug = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Debug", skinLibgdx));
        tbl.add(chkDebug);
        tbl.row();
        return tbl;
    }

    private Table buildOptWinButtons () {
        Table tbl = new Table();
        // + Separator
        Label lbl = null;
        lbl = new Label("", skinLibgdx);lbl.setColor(0.75f, 0.75f, 0.75f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
        tbl.row();
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.5f, 0.5f, 0.5f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
        tbl.row();
        // + Save Button with event handler
        btnWinOptSave = new TextButton("Save", skinLibgdx);
        tbl.add(btnWinOptSave).padRight(30);
        btnWinOptSave.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });
        // + Cancel Button with event handler
        btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
        tbl.add(btnWinOptCancel);
        btnWinOptCancel.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onCancelClicked();
            }
        });
        return tbl;
    }

    private void showMenuButtons (boolean visible) {
        float moveDuration = 1.0f;
        Interpolation moveEasing = Interpolation.swing;
        float delayOptionsButton = 0.25f;
        float moveX = 300 * (visible ? -1 : 1);
        float moveY = 0 * (visible ? -1 : 1);
        final Touchable touchEnabled = visible ? Touchable.enabled
                : Touchable.disabled;
        btnMenuPlay.addAction(
                moveBy(moveX, moveY, moveDuration, moveEasing));
        btnMenuOptions.addAction(sequence(
                delay(delayOptionsButton),
                moveBy(moveX, moveY, moveDuration, moveEasing)));
        SequenceAction seq = sequence();
        if (visible)
            seq.addAction(delay(delayOptionsButton + moveDuration));
        seq.addAction(run(new Runnable() {
            public void run () {
                btnMenuPlay.setTouchable(touchEnabled);
                btnMenuOptions.setTouchable(touchEnabled);
            }
        }));
        stage.addAction(seq);
    }
    private void showOptionsWindow (boolean visible,
                                    boolean animated) {
        float alphaTo = visible ? 0.8f : 0.0f;
        float duration = animated ? 1.0f : 0.0f;
        Touchable touchEnabled = visible ? Touchable.enabled
                : Touchable.disabled;
        winOptions.addAction(sequence(
                touchable(touchEnabled),
                alpha(alphaTo, duration)));
    }

}
