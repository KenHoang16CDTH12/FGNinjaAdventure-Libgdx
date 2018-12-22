package com.fgdev.game.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class BackgroundTiledMapRenderer extends OrthogonalTiledMapRenderer {
    private Texture background;

    public BackgroundTiledMapRenderer(final TiledMap map, final float unitScale, Texture background) {
        super(map, unitScale);
        this.background = background;
    }

    @Override
    protected void beginRender() {
        super.beginRender();

        // Draw the background
        getBatch().draw(background, viewBounds.x, viewBounds.y, viewBounds.getWidth(), viewBounds.getHeight());
    }

    public void setBackground(Texture background) {
        this.background = background;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.background.dispose();
    }
}
