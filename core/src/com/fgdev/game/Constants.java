package com.fgdev.game;

import com.badlogic.gdx.math.Vector2;

public class Constants {
    //
    public static final float V_WIDTH = 2235.0f;
    public static final float V_HEIGHT = 1135.0f;
    // PPM
    public static final float PPM = 100.0f;
    /**
     * BIT
     */
    public static final short GROUND_BIT = 1;
    public static final short CRATE_BIT = 2;
    public static final short SIGN_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short FEATHER_BIT = 16;
    public static final short PLAYER_BIT = 32;
    public static final short ATTACK_BIT = 64;
    public static final short KUNAI_BIT = 128;
    public static final short ZOMBIE_BIT = 256;
    // Bullet
    public static final float BULLET_SPEED = 20f;
    // GUI Width
    public static final float VIEWPORT_GUI_WIDTH = 1240.0f;
    // GUI Height
    public static final float VIEWPORT_GUI_HEIGHT = 630.0f;
    // Location of atlas file for level
    public static final String LEVEL_01 = "levels/Level1/level1.tmx";
    public static final String LEVEL_02 = "levels/Level2/level2.tmx";
    // Amount of extra lives at level start
    public static final int LIVES_START = 3;
    // Delay after game over
    public static final float TIME_DELAY_GAME_OVER = 3;
    // Location of atlas
    public static final String TEXTURE_ATLAS_UI = "images/ui.atlas";
    public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/star-soldier-ui.atlas";
    public static final String TEXTURE_ATLAS_ITEM = "items/item.atlas";
    public static final String TEXTURE_ATLAS_PLAYER_BOY = "characters/boy.atlas";
    public static final String TEXTURE_ATLAS_PLAYER_GIRL = "characters/girl.atlas";
    // Location of atlas enemies
    public static final String TEXTURE_ATLAS_ZOMBIE = "enemies/zombie.atlas";
    // Location of description file for skins
    public static final String SKIN_UI = "images/ui.json";
    public static final String SKIN_LIBGDX_UI = "images/star-soldier-ui.json";

    /**
     * Duration of feather power-up in seconds
     */
    public static final float ITEM_FEATHER_POWERUP_DURATION = 9;
    /**
     * Game preferences file
     */
    public static final String PREFERENCES = "ninjaadventures.prefs";

    // Shader
    public static final String shaderMonochromeVertex =
            "shaders/monochrome.vs";
    public static final String shaderMonochromeFragment =
            "shaders/monochrome.fs";

    // Logic
    public static final Vector2 GRAVITY = new Vector2(0.0f, -9.8f);
    public static final float TIME_STEP = 1 / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
}
