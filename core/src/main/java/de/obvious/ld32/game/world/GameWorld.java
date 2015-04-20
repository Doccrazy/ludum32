package de.obvious.ld32.game.world;

import java.util.Map;

import box2dLight.RayHandler;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.data.GamepadActions;
import de.obvious.ld32.game.abilities.ShroomAbility;
import de.obvious.ld32.game.abilities.StartAbility;
import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.ld32.game.actor.RootActor;
import de.obvious.ld32.game.actor.TiledMapActor;
import de.obvious.shared.game.base.GamepadMovementListener;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.GameState;

public class GameWorld extends Box2dWorld {

	private Map<Integer, GamepadActions> actionMap;
	private boolean partInit;
	private GamepadMovementListener moveListener;
    private PlayerActor player;
    private TiledMapActor level;
    private float shakeLevel, shakeDegrade;
    private Music currentMusic;

	public GameWorld() {
        super(GameRules.GRAVITY);
        RayHandler.useDiffuseLight(true);
    }

    @Override
    protected void doTransition(GameState newState) {
        switch (newState) {
        case INIT:
            player = new PlayerActor(this, new Vector2(106, 84), true);
            player.setAbility(0, new StartAbility(this, new Color(1f, 1f, 0f, 1f)));  //TODO
            player.setAbility(1, new ShroomAbility(this));  //TODO
            player.setupKeyboardControl();
            level = new TiledMapActor(this, Resource.GFX.LEVEL1);
            addActor(level);
            addActor(player);
            //addActor(new InsectActor(this, new Vector2(100, 107), true));
            //addActor(new InsectActor(this, new Vector2(107, 107), true));
            //addActor(new ShroomActor(this, new Vector2(95, 95), true));
            //addActor(new SpikyActor(this, new Vector2(100, 90), true));
            addActor(new RootActor(this, new Vector2(90, 90), true));
            startMusic(Resource.MUSIC.gameShortSlow2);
            break;
        case GAME:
            stage.setKeyboardFocus(player);
            break;
        default:
        }
    }

    @Override
    protected void doUpdate(float delta) {
        switch(getGameState()) {
        case INIT:
            transition(GameState.PRE_GAME);
            break;
        case PRE_GAME:
            if (getStateTime() > 0.5f) {
                transition(GameState.GAME);
            }
            break;
        case GAME:
            if (player.isDead()) {
                transition(GameState.DEFEAT);
            }
        default:
        }
        shakeLevel = Math.max(shakeLevel - delta*shakeDegrade, 0);
    }

    public void controllerConfigured(Map<Integer, GamepadActions> actionMap) {
		this.actionMap = actionMap;
    }

    public PlayerActor getPlayer() {
        return player;
    }

    public TiledMapActor getLevel() {
        return level;
    }

    public void startMusic(Music music){
    	if(currentMusic != null)
    	currentMusic.stop();
    	currentMusic = music;
    	currentMusic.setLooping(false);
    	currentMusic.play();

    }

    public void addShake(float percent) {
        shakeLevel = Math.min(shakeLevel + percent, 1f);
        shakeDegrade = shakeLevel;
    }

    public float getShakeLevel() {
        return shakeLevel;
    }
}
