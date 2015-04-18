package de.obvious.ld32.game.ui;

import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.obvious.ld32.game.GameRenderer;
import de.obvious.ld32.game.world.GameInputListener;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.ui.UiBase;

public class UiRoot extends UiBase<GameWorld, GameRenderer, GameInputListener> {
	private UiGamepadListener padInput;

	public UiRoot(Stage stage, GameWorld world, GameRenderer renderer) {
		super(stage, world, renderer);
		padInput = new UiGamepadListener(this);
		Controllers.addListener(padInput);

		stage.addActor(new ControllerLabel(this));
		stage.addActor(new UiItemSlots(this, world));
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (getWorld().isGameFinished()) {
		}
	}

	public UiGamepadListener getPadInput() {
		return padInput;
	}

	@Override
	protected InputListener createUiInput() {
		return new UiInputListener(this);
	}

	@Override
	protected GameInputListener createGameInput() {
	    return new GameInputListener(this);
	}
}
