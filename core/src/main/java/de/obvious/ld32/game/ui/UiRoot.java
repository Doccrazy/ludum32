package de.obvious.ld32.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.game.GameRenderer;
import de.obvious.ld32.game.world.GameInputListener;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.ui.UiBase;

public class UiRoot extends UiBase<GameWorld, GameRenderer, GameInputListener> {
	private UiGamepadListener padInput;
    private Stage uiStage;
    private UiInputListener uiInput;

	public UiRoot(Stage stage, GameWorld world, GameRenderer renderer) {
		super(stage, world, renderer);
		uiStage = stage;
		padInput = new UiGamepadListener(this);
		Controllers.addListener(padInput);

		stage.addActor(new LowHealthOverlay(this, world));
		stage.addActor(new ControllerLabel(this));
		stage.addActor(new UiItemSlots(this, world));
		stage.addActor(new UiText(this));
		stage.addActor(new IntroScreen(this));
		stage.addActor(new VictoryScreen(this));

		add(new QuestLogTable(this)).expandX().left().pad(5);
		row();
		add(new Healthbar(this)).expand().bottom().left().pad(5);

		Gdx.input.setCursorImage(Resource.GFX.crosshair, 16, 16);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		uiInput.act(delta);
		if (getWorld().isGameFinished()) {
		}
	}

	public UiGamepadListener getPadInput() {
		return padInput;
	}

	@Override
	protected InputListener createUiInput() {
	    uiInput = new UiInputListener(this);
        return uiInput;
	}

	@Override
	protected GameInputListener createGameInput() {
	    return new GameInputListener(this);
	}

	public Stage getUiStage() {
	    return uiStage;
	}
}
