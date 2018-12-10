package com.fgdev.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fgdev.game.FGDevMain;

public class DesktopLauncher {

	private static final int WIDTH = 1240;
	private static final int HEIGHT = 630;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WIDTH;
		config.height = HEIGHT;
		new LwjglApplication(new FGDevMain(), config);
	}
}
