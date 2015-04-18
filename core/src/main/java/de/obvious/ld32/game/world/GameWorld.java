package de.obvious.ld32.game.world;

import java.util.Map;

import box2dLight.RayHandler;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.data.GamepadActions;
import de.obvious.shared.game.base.GamepadMovementListener;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.GameState;

public class GameWorld extends Box2dWorld {

	private Map<Integer, GamepadActions> actionMap;
	private boolean partInit;
	private GamepadMovementListener moveListener;

	public GameWorld() {
        super(GameRules.GRAVITY);
        RayHandler.useDiffuseLight(true);
    }

    @Override
    protected void doTransition(GameState newState) {
    }

    @Override
    protected void doUpdate(float delta) {
    }

    public void controllerConfigured(Map<Integer, GamepadActions> actionMap) {
		this.actionMap = actionMap;
    }

}
