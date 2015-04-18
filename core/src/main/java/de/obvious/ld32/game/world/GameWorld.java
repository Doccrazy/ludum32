package de.obvious.ld32.game.world;

import java.util.Map;

import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.data.GamepadActions;
import de.obvious.ld32.game.abilities.DevAbility;
import de.obvious.ld32.game.actor.EnemyActor;
import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.ld32.game.actor.TiledMapActor;
import de.obvious.shared.game.base.GamepadMovementListener;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.GameState;

public class GameWorld extends Box2dWorld {

	private Map<Integer, GamepadActions> actionMap;
	private boolean partInit;
	private GamepadMovementListener moveListener;
    private PlayerActor player;

	public GameWorld() {
        super(GameRules.GRAVITY);
        RayHandler.useDiffuseLight(true);
        transition(GameState.PRE_GAME);
        transition(GameState.GAME);
    }

    @Override
    protected void doTransition(GameState newState) {
        switch (newState) {
        case INIT:
            player = new PlayerActor(this, new Vector2(100, 50), true);
            player.setAbility(0, new DevAbility(this, new Color(1f, 1f, 0f, 1f)));  //TODO
            player.setAbility(1, new DevAbility(this, new Color(1f, 0f, 0f, 1f)));  //TODO
            player.setupKeyboardControl();
            addActor(new TiledMapActor(this));
            addActor(player);
            addActor(new EnemyActor(this, new Vector2(100, 55), true));
            break;
        case GAME:
            stage.setKeyboardFocus(player);
            break;
        }
    }

    @Override
    protected void doUpdate(float delta) {
        switch(getGameState()) {
        case PRE_GAME:
            if (getStateTime() > 0.5f) {
                transition(GameState.GAME);
            }
            break;
        default:
        }
    }

    public void controllerConfigured(Map<Integer, GamepadActions> actionMap) {
		this.actionMap = actionMap;
    }

    public PlayerActor getPlayer() {
        return player;
    }

}
