package com.fgdev.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Constants {
    // SIZE
    public static final float V_WIDTH = 22.35f;
    public static final float V_HEIGHT = 12.75f;
    public static final int WINDOW_WIDTH = Gdx.graphics.getWidth();
    public static final int WINDOW_HEIGHT = Gdx.graphics.getHeight();
    // PPM
    public static final float PPM = 100.0f;
    /**
     * BIT
     */
    public static final short HIDDEN_WALL_BIT = -1;
    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short CRATE_BIT = 2;
    public static final short SIGN_BIT = 4;
    public static final short ITEM_BIT = 8;
    public static final short PLAYER_BIT = 16;
    public static final short ATTACK_BIT = 32;
    public static final short KUNAI_BIT = 64;
    public static final short ENEMY_BIT = 128;
    public static final short SPIKE_BIT = 256;
    public static final short BULLET_BIT = 512;
    public static final short LADDER_BIT = 1024;
    // Location of atlas file for level
    public static final String LEVEL_01 = "levels/map/1/map1.tmx";
    public static final String LEVEL_02 = "levels/map/2/map2.tmx";
    public static final String LEVEL_03 = "levels/map/3/map3.tmx";
    public static final String LEVEL_04 = "levels/map/4/map4.tmx";
    public static final String LEVEL_05 = "levels/map/5/map5.tmx";
    // Amount of extra lives at level start
    public static final int LIVES_START = 3;
    // Delay after game over
    public static final float TIME_DELAY_GAME_OVER = 5;
    public static final float TIME_NEXT_LEVEL = 5;
    public static final float TIME_DELAY_LIVE_LOST = 3;
    public static final int TOTAL_LEVEL = 5;
    // Location of atlas
    public static final String TEXTURE_ATLAS_UI = "images/ui.atlas";
    public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/star-soldier-ui.atlas";
    public static final String TEXTURE_ATLAS_ITEM = "items/item.atlas";
    public static final String TEXTURE_ATLAS_JOYSTICK = "images/joystick.atlas";
    public static final String TEXTURE_ATLAS_PLAYER_BOY = "characters/boy.atlas";
    public static final String TEXTURE_ATLAS_PLAYER_GIRL = "characters/girl.atlas";
    // Location of atlas enemies
    public static final String TEXTURE_ATLAS_ZOMBIE = "enemies/zombie.atlas";
    public static final String TEXTURE_ATLAS_ROBOT = "enemies/robot.atlas";
    public static final String TEXTURE_ATLAS_ADVENTURE_GIRL = "enemies/adventure_girl.atlas";
    public static final String TEXTURE_ATLAS_DINO = "enemies/dino.atlas";
    public static final String TEXTURE_ATLAS_KNIGHT = "enemies/knight.atlas";
    public static final String TEXTURE_ATLAS_SANTA = "enemies/santa.atlas";
    public static final String TEXTURE_ATLAS_GHOST = "enemies/ghost.atlas";
    public static final String TEXTURE_ATLAS_BONE = "enemies/bone.atlas";
    // Location of description file for skins
    public static final String SKIN_UI = "images/ui.json";
    public static final String SKIN_LIBGDX_UI = "images/star-soldier-ui.json";

    /**
     * Duration of feather power-up in seconds
     */
    public static final float ITEM_FEATHER_POWERUP_DURATION = 5;
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
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
    // speed
    public static final float SPEED_PLAYER_X = 0.1f;
    // Position player
    public static final float POS_X = 100 / PPM;
    public static final float POS_Y = 450 / PPM;
}
