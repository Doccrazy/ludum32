package de.obvious.ld32.game.world;

import java.util.HashMap;
import java.util.Map;

import box2dLight.RayHandler;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.data.GamepadActions;
import de.obvious.ld32.data.QuestStatus;
import de.obvious.ld32.data.QuestType;
import de.obvious.ld32.game.abilities.ShroomAbility;
import de.obvious.ld32.game.abilities.StartAbility;
import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.ld32.game.actor.RootActor;
import de.obvious.ld32.game.actor.TiledMapActor;
import de.obvious.ld32.game.misc.UpdateQuestEvent;
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
    private Map<QuestType, QuestStatus> quests = new HashMap<>();

	public GameWorld() {
		super(GameRules.GRAVITY);
		RayHandler.useDiffuseLight(true);
	}

	@Override
	protected void doTransition(GameState newState) {
		switch (newState) {
		case INIT:
			player = new PlayerActor(this, new Vector2(106, 84), true);
			player.setAbility(0, new StartAbility(this, new Color(1f, 1f, 0f, 1f))); // TODO
			player.setAbility(1, new ShroomAbility(this)); // TODO
			player.setupKeyboardControl();
			level = new TiledMapActor(this, Resource.GFX.LEVEL1);
			addActor(level);
			addActor(player);
			// addActor(new InsectActor(this, new Vector2(100, 107), true));
			// addActor(new InsectActor(this, new Vector2(107, 107), true));
			// addActor(new ShroomActor(this, new Vector2(95, 95), true));
			// addActor(new SpikyActor(this, new Vector2(100, 90), true));
			addActor(new RootActor(this, new Vector2(90, 90), true));

			break;
		case GAME:
            startMusic(Resource.MUSIC.gameShortSlow2);
            player.startFlashlight();
			stage.setKeyboardFocus(player);
			break;
		default:
		}
	}

	@Override
	protected void doUpdate(float delta) {
		switch (getGameState()) {
		case INIT:
			break;
		case PRE_GAME:
		    float lightLevel = Math.min(0.2f, 0.2f * (getStateTime() / 2f));
            rayHandler.setAmbientLight(lightLevel, lightLevel, lightLevel, 1);
			if (getStateTime() > 2f) {
				transition(GameState.GAME);
			}
			break;
		case GAME:
			if (player.isDead()) {
				transition(GameState.DEFEAT);
			}
			boolean allDone = true;
			for (QuestType q : QuestType.values()) {
			    if (quests.get(q) != QuestStatus.END) {
			        allDone = false;
			    }
			}
			if (allDone) {
			    transition(GameState.VICTORY);
			}
		default:
		}
		shakeLevel = Math.max(shakeLevel - delta * shakeDegrade, 0);
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

	public void startMusic(Music music) {
		if (currentMusic != null)
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

    public Map<QuestType, QuestStatus> getQuests() {
        return quests;
    }

    public void progressQuest(QuestType type, QuestStatus status) {
        if (quests.get(type) == null || quests.get(type).ordinal() < status.ordinal()) {
            quests.put(type, status);
            postEvent(new UpdateQuestEvent(type));
        }
    }
}
