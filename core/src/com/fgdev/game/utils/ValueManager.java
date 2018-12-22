package com.fgdev.game.utils;

import com.fgdev.game.Constants;

public class ValueManager {

    public static final String TAG = ValueManager.class.getName();

    public static final ValueManager instance = new ValueManager();

    public int score;
    public int lives;
    public float livesVisual;
    public float scoreVisual;
    public float timeLeftGameOverDelay;

    // singleton: prevent instantiation from other classes
    private ValueManager() { }

    public void init() {
        lives = Constants.LIVES_START;
        livesVisual = lives;
        score = 0;
        scoreVisual = score;
        timeLeftGameOverDelay = 0;
    }

    public boolean isGameOver () {
        return lives < 0;
    }
}
