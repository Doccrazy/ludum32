package de.obvious.ld32.resources;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.obvious.shared.core.ResourcesBase;

public class GfxResources extends ResourcesBase {
	public Sprite player = atlas.createSprite("player");
	public Animation playerLeft = new Animation(0.1f, atlas.findRegions("player/tLinks"));
	public Sprite crosshair = atlas.createSprite("crosshair");

	public TiledMap LEVEL1 = new TmxMapLoader().load("RaumschiffEbene1.tmx");

    public GfxResources() {
        super("game.atlas");
    }
}
