package com.fgdev.game.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class BackgroundTiledMapRenderer extends OrthogonalTiledMapRenderer {
    private final Texture background;

    public BackgroundTiledMapRenderer(final TiledMap map, final float unitScale, final Texture background) {
        super(map, unitScale);
        this.background = background;
    }

    @Override
    protected void beginRender() {
        super.beginRender();

        // Draw the background
        getBatch().draw(background, viewBounds.x, viewBounds.y, viewBounds.getWidth(), viewBounds.getHeight());
    }
}
