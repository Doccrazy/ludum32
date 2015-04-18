package de.obvious.ld32.resources;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.obvious.shared.core.ResourcesBase;

public class GfxResources extends ResourcesBase {

	public TiledMap LEVEL1 = new TmxMapLoader().load("RaumschiffEbene1.tmx");

    public GfxResources() {
       // super("game.atlas");
    }
}
