package de.obvious.ld32.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.obvious.shared.core.ResourcesBase;

public class GfxResources extends ResourcesBase {
	//Left, Right, Up, Down
    public Animation[] player = new Animation[] {
        new Animation(0.025f, atlas.findRegions("player/tLinks")),
        flip(new Animation(0.025f, atlas.findRegions("player/tLinks")), true, false),
        new Animation(0.05f, atlas.findRegions("player/tBack")),
        new Animation(0.05f, atlas.findRegions("player/tVor"))
    };
	public Animation[] playerHead = new Animation[] {
    	new Animation(0.025f, atlas.findRegions("player/tHeadLeft")),
    	flip(new Animation(0.025f, atlas.findRegions("player/tHeadLeft")), true, false),
    	new Animation(0.025f, atlas.findRegions("player/tHeadBack")),
    	new Animation(0.025f, atlas.findRegions("player/tHeadVor"))
	};

	public Animation[] weaponInsect = new Animation[] {
        new Animation(0.1f, atlas.findRegions("weapon/tWeaponInsekt")),
        new Animation(0.1f, atlas.findRegions("weapon/tWeaponInsektFire")),
	};
	public Sprite crosshair = atlas.createSprite("crosshair");
	public Texture insect = new Texture(Gdx.files.internal("Grafiken/Insekt/tInsektFront.png"));
	public Texture lifeBar = new Texture(Gdx.files.internal("Grafiken/lifebar.png"));

	public TiledMap LEVEL1 = new TmxMapLoader().load("RaumschiffEbene1.tmx");

    public GfxResources() {
        super("game.atlas");
    }
}
