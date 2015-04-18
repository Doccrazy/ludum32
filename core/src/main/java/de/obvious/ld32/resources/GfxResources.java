package de.obvious.ld32.resources;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.graphics.g2d.Sprite;

import de.obvious.shared.core.ResourcesBase;

public class GfxResources extends ResourcesBase {
	public Sprite player = atlas.createSprite("player");

	public TiledMap LEVEL1 = new TmxMapLoader().load("RaumschiffEbene1.tmx");

    public GfxResources() {
        super("game.atlas");
    }
}
