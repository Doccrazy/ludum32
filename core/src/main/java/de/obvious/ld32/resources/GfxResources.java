package de.obvious.ld32.resources;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.obvious.ld32.data.Emotion;
import de.obvious.shared.core.ResourcesBase;

public class GfxResources extends ResourcesBase {
	//Left, Right, Up, Down
    public Animation[] player = new Animation[] {
        new Animation(0.025f, atlas.findRegions("player/tLinks")),
        flip(new Animation(0.025f, atlas.findRegions("player/tLinks")), true, false),
        new Animation(0.05f, atlas.findRegions("player/tBack")),
        new Animation(0.05f, atlas.findRegions("player/tVor"))
    };
	public Map<Emotion, Animation[]> playerHead = new HashMap<Emotion, Animation[]>() {{
	    put(Emotion.NEUTRAL, new Animation[] {
            new Animation(0.025f, atlas.findRegions("player/head/tHeadLeft")),
            flip(new Animation(0.025f, atlas.findRegions("player/head/tHeadLeft")), true, false),
            new Animation(0.025f, atlas.findRegions("player/head/tHeadRueck")),
            new Animation(0.025f, atlas.findRegions("player/head/tHeadVor"))
        });
	    put(Emotion.ANGRY, new Animation[] {
	            new Animation(0.025f, atlas.findRegions("player/head/tHeadAngryLeft")),
	            flip(new Animation(0.025f, atlas.findRegions("player/head/tHeadAngryLeft")), true, false),
	            new Animation(0.025f, atlas.findRegions("player/head/tHeadRueck")),
	            new Animation(0.025f, atlas.findRegions("player/head/tHeadAngryVor"))
	    });
	    put(Emotion.JOYFUL, new Animation[] {
	            new Animation(0.025f, atlas.findRegions("player/head/tHeadLeft")),  //TODO
	            flip(new Animation(0.025f, atlas.findRegions("player/head/tHeadLeft")), true, false),
	            new Animation(0.025f, atlas.findRegions("player/head/tHeadRueck")),
	            new Animation(0.025f, atlas.findRegions("player/head/tHeadJoyVor"))
	    });
	    put(Emotion.TROUBLED, new Animation[] {
	            new Animation(0.025f, atlas.findRegions("player/head/tHeadTroubleLeft")),
	            flip(new Animation(0.025f, atlas.findRegions("player/head/tHeadTroubleLeft")), true, false),
	            new Animation(0.025f, atlas.findRegions("player/head/tHeadRueck")),
	            new Animation(0.025f, atlas.findRegions("player/head/tHeadTroubleVor"))
	    });
	}};
	public Animation[] playerHeadWounded = new Animation[] {
        new Animation(0.025f, atlas.findRegions("player/head/tHeadWoundedLeft")),
        new Animation(0.025f, atlas.findRegions("player/head/tHeadWoundedRight")),
        new Animation(0.025f, atlas.findRegions("player/head/tHeadWoundedRueck")),
        new Animation(0.025f, atlas.findRegions("player/head/tHeadWoundedVor"))
	};

	public Animation[] weaponStart = new Animation[] {
        new Animation(0.1f, atlas.findRegions("weapon/tWeaponBeg")),
        new Animation(0.1f, atlas.findRegions("weapon/tWeaponBegFire")),
	};
	public Animation[] weaponInsect = new Animation[] {
	        new Animation(0.1f, atlas.findRegions("weapon/tWeaponInsekt")),
	        new Animation(0.1f, atlas.findRegions("weapon/tWeaponInsektFire")),
	};
	public Animation[] weaponShroom = new Animation[] {
	        new Animation(0.1f, atlas.findRegions("weapon/tWeaponPilz")),
	        new Animation(0.1f, atlas.findRegions("weapon/tWeaponPilzFire")),
	};
	public Pixmap crosshair = new Pixmap(Gdx.files.internal("crosshair.png"));
	public Texture insect = new Texture(Gdx.files.internal("Grafiken/Insekt/tInsektFront.png"));
	public Texture shroom = new Texture(Gdx.files.internal("Grafiken/Pilzgegner/tPilzGegner_1.png"));
	public Texture lifeBar = new Texture(Gdx.files.internal("Grafiken/lifebar.png"));
	public Texture itemSlot = new Texture(Gdx.files.internal("Grafiken/tFeatureUmrandung.png"));
	public Texture startWeapon = new Texture(Gdx.files.internal("Grafiken/tBeginnerwaffe.png"));
	public Texture startWeaponX = new Texture(Gdx.files.internal("Grafiken/tBeginnerwaffeX.png"));
	public Texture insectWeapon = new Texture(Gdx.files.internal("Grafiken/tInsektenwaffe.png"));
	public Texture shroomWeapon = new Texture(Gdx.files.internal("Grafiken/tPilzwaffe.png"));


	public TiledMap LEVEL1 = new TmxMapLoader().load("RaumschiffEbene1.tmx");

    public GfxResources() {
        super("game.atlas");
    }
}
