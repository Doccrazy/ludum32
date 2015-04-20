package de.obvious.ld32.resources;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.obvious.ld32.data.Emotion;
import de.obvious.shared.core.ResourcesBase;

public class GfxResources extends ResourcesBase {
	//Left, Right, Up, Down, Dead
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
	public Animation[] weaponSpike = new Animation[] {
	        new Animation(0.1f, atlas.findRegions("weapon/tWeaponStachel")),
	        new Animation(0.1f, atlas.findRegions("weapon/tWeaponStachelFire")),
	};

	public Animation[] weaponRoot = new Animation[] {
			new Animation(0.1f, atlas.findRegions("weapon/tWeaponRanke")),
			new Animation(0.1f, atlas.findRegions("weapon/tWeaponRankeFire")),
	};

	public Animation[] enemyShroom = new Animation[] {
	        new Animation(0.1f, atlas.findRegions("enemy/Pilzgegner/tPilzGegnerLinks")),
	        new Animation(0.1f, atlas.findRegions("enemy/Pilzgegner/tPilzGegnerRechts")),
	        new Animation(0.1f, atlas.findRegions("enemy/Pilzgegner/tPilzGegnerHinten")),
	        new Animation(0.1f, atlas.findRegions("enemy/Pilzgegner/tPilzGegner")),
	        new Animation(0.1f, atlas.findRegions("enemy/Pilzgegner/tPilzGegnerTod")),
	};
	public Sprite[] enemyShroomParts = new Sprite[] {
	        atlas.createSprite("enemy/Pilzgegner/tPilzGegnerTodTile", 1),
	        atlas.createSprite("enemy/Pilzgegner/tPilzGegnerTodTile", 2),
	        atlas.createSprite("enemy/Pilzgegner/tPilzGegnerTodTile", 3),
	        atlas.createSprite("enemy/Pilzgegner/tPilzGegnerTodTile", 4),
	        atlas.createSprite("enemy/Pilzgegner/tPilzGegnerTodTile", 5),
	};
	public Animation enemyShroomStunned = new Animation(0.1f, atlas.findRegions("enemy/Pilzgegner/tPilzGegnerTaub"));
	public Animation enemyShroomShake = new Animation(0.1f, atlas.findRegions("enemy/Pilzgegner/tPilzGegnerWackeln"));
	public Animation enemyShroomCloud = new Animation(0.33f, atlas.findRegions("enemy/Pilzgegner/tPilzRauch"));
	public Animation enemyShroomHeal = new Animation(0.1f, atlas.findRegions("enemy/Pilzgegner/tPilzHeilung"));

    public Animation[] enemySpiky = new Animation[] {
            new Animation(0.1f, atlas.findRegions("enemy/Stachelmonster/tStachelmonsterLinks")),
            new Animation(0.1f, atlas.findRegions("enemy/Stachelmonster/tStachelmonsterRechts")),
            new Animation(0.1f, atlas.findRegions("enemy/Stachelmonster/tStachelmonsterHinten")),
            new Animation(0.1f, atlas.findRegions("enemy/Stachelmonster/tStachelmonster")),
            new Animation(0.1f, atlas.findRegions("enemy/Stachelmonster/tStachelmonsterTod"))
    };
    public Animation enemySpikyAttack = new Animation(0.1f, atlas.findRegions("enemy/Stachelmonster/tStachelmonsterAttacke"));
    public Animation[] enemyInsect = new Animation[] {
            flip(new Animation(0.1f, atlas.findRegions("enemy/Insekt/tInsektRight")), true, false),
            new Animation(0.1f, atlas.findRegions("enemy/Insekt/tInsektRight")),
            new Animation(0.1f, atlas.findRegions("enemy/Insekt/tInsektBack")),
            new Animation(0.1f, atlas.findRegions("enemy/Insekt/tInsektVor")),
            new Animation(0.1f, atlas.findRegions("enemy/Insekt/tInsektDead")),
    };
    public Animation[] enemyInsectAttack = new Animation[] {
            flip(new Animation(0.1f, atlas.findRegions("enemy/Insekt/tInsektRightAttack")), true, false),
            new Animation(0.1f, atlas.findRegions("enemy/Insekt/tInsektRightAttack")),
            new Animation(0.1f, atlas.findRegions("enemy/Insekt/tInsektBackAttack")),
            new Animation(0.1f, atlas.findRegions("enemy/Insekt/tInsektFrontAttack")),
    };
    public Animation[] enemyRoot = new Animation[] {
            flip(new Animation(0.1f, atlas.findRegions("enemy/Wurzelgegner/tWurzelgegnerRechts")), true, false),
            new Animation(0.1f, atlas.findRegions("enemy/Wurzelgegner/tWurzelgegnerRechts")),
            new Animation(0.1f, atlas.findRegions("enemy/Wurzelgegner/tWurzelgegnerRueck")),
            new Animation(0.1f, atlas.findRegions("enemy/Wurzelgegner/tWurzelgegnerVor")),
            new Animation(0.1f, atlas.findRegions("enemy/Wurzelgegner/tWurzelgegnerTod")),
    };
    public Animation enemyRootAttack = new Animation(0.075f, atlas.findRegions("enemy/Wurzelgegner/tWurzelgegnerAttacke"));
    public Animation rootAoe = new Animation(0.1f, atlas.findRegions("enemy/Wurzelgegner/Wurzelangriff"));

    public Animation spikeBigProjectile = new Animation(0.1f, atlas.findRegions("projectiles/tSchussDorne"));
    public Animation RootProjectile = new Animation(0.1f, atlas.findRegions("projectiles/Wurzelangriff"));
    public Animation spikeLittleProjectile = new Animation(0.1f, atlas.findRegions("projectiles/tSchussDorneSplitter"));
    public Animation spikeExplosion = new Animation(0.1f, atlas.findRegions("projectiles/tExplosionDorne"));
    public Animation smoke = new Animation(0.1f, atlas.findRegions("particles/tRauch"));
    public Animation playerRoot = new Animation(0.2f, atlas.findRegions("secondary/tRankeSekundaer"));
    public Sprite[] blood = new Sprite[]{
        atlas.createSprite("particles/tBlut1"),
        atlas.createSprite("particles/tBlut2"),
        atlas.createSprite("particles/tBlut3"),
        atlas.createSprite("particles/tBlut4"),
    };

	public Pixmap crosshair = new Pixmap(Gdx.files.internal("crosshair.png"));
	public Texture lifeBar = new Texture(Gdx.files.internal("Grafiken/lifebar.png"));
	public Texture itemSlot = new Texture(Gdx.files.internal("Grafiken/tFeatureUmrandung.png"));
	public Texture startWeapon = new Texture(Gdx.files.internal("Grafiken/tBeginnerwaffe.png"));
	public Texture startWeaponX = new Texture(Gdx.files.internal("Grafiken/tBeginnerwaffeX.png"));
	public Texture insectWeapon = new Texture(Gdx.files.internal("Grafiken/tInsektenwaffe.png"));
	public Texture shroomWeapon = new Texture(Gdx.files.internal("Grafiken/tPilzwaffe.png"));
	public Texture spikeWeapon = new Texture(Gdx.files.internal("Grafiken/tStachelwaffe.png"));
	public Texture rootWeapon = new Texture(Gdx.files.internal("Grafiken/tRankenwaffe.png"));
	public Texture startWeaponBullet = new Texture(Gdx.files.internal("Grafiken/Waffeneffekte/tSchussStandard.png"));
	public TextureRegion startWeaponBulletR = new TextureRegion(startWeaponBullet);

	public Texture healthbarEmpty = texture("Grafiken/tLebensanzeige.png");
	public Texture healthbarFull = texture("Grafiken/tLebensanzeigeVoll.png");
	public Texture lowHealthOverlay = texture("lowHealthOverlay.png");
	public Texture quest = texture("quest.png");
	public Texture questDone = texture("questDone.png");

	public TiledMap LEVEL1 = new TmxMapLoader().load("RaumschiffEbene1.tmx");

    public GfxResources() {
        super("game.atlas");
    }
}
