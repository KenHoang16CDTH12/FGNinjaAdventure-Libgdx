package com.fgdev.game.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerApp {

    private static boolean rebuildAtlas = true;
    private static boolean drawDebugOutline = false;
    private static String input = "desktop/assets-raw/tileset/foreground";
    private static String output = "android/assets/packers";
    private static String packFileName = "foreground";
    // Commandline => convert -resize 20% BoyIdle10.png anim_boy_iddle_10.png
    public static void main(final String[] args) {
        if (rebuildAtlas) {
            TexturePacker.Settings settings = new TexturePacker.Settings();
            settings.maxHeight = 2048;
            settings.maxWidth = 2048;
            settings.paddingX = 128;
            settings.duplicatePadding = false;
            settings.debug = drawDebugOutline;
            TexturePacker.process(settings, input, output, packFileName);
        }
    }
}
