package de.obvious.ld32.game.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import de.obvious.ld32.game.abilities.FireMode;
import de.obvious.shared.core.Debug;
import de.obvious.shared.game.world.GameState;

public class UiInputListener extends InputListener {
	private UiRoot root;
	private Vector2 mousePos = new Vector2();
	private boolean[] buttonDown = new boolean[2];

    public UiInputListener(UiRoot root) {
        this.root = root;
	}

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (root.getWorld().getGameState() != GameState.GAME) {
            return false;
        }
        if (button == 0 || button == 1) {
            mousePos.set(x, y);
            setCrosshair();
            buttonDown[button] = true;
            root.getWorld().getPlayer().fire(button, getFireMode(button));
        }
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (root.getWorld().getGameState() != GameState.GAME) {
            return;
        }
        if (button == 0 || button == 1) {
            buttonDown[button] = false;
        }
    }

    private FireMode getFireMode(int button) {
        switch (button) {
        case 0: return FireMode.PRIMARY;
        case 1: return FireMode.ALTERNATE;
        default: throw new IllegalArgumentException(String.valueOf(button));
        }
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        mouseMoved(event, x, y);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        if (root.getWorld().getGameState() != GameState.GAME) {
            return false;
        }
        mousePos.set(x, y);
        setCrosshair();
        return true;
    }

    private void setCrosshair() {
        Vector2 coord = mousePos.cpy();
        root.getUiStage().stageToScreenCoordinates(coord);
        root.getWorld().stage.screenToStageCoordinates(coord);
        root.getWorld().getPlayer().setCrosshair(coord.x, coord.y);
    }

	@Override
    public boolean keyDown(InputEvent event, int keycode) {
		if (keycode == Keys.ENTER
				&& (root.getWorld().getGameState() == GameState.INIT)) {
			//root.getWorld().transition(GameState.INIT);
			//root.getWorld().transition(GameState.PRE_GAME);
		}
		if (Debug.ON) {
			if (keycode == Keys.Z) {
				root.getRenderer().setZoomDelta(1f);
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(InputEvent event, int keycode) {
        if (Debug.ON) {
            if (keycode == Keys.Z) {
                root.getRenderer().setZoomDelta(-2f);
            }
        }
        return false;
	}

	public void act(float delta) {
	    if (root.getWorld().getGameState() == GameState.GAME) {
            setCrosshair();
            if (buttonDown[0]) {
                root.getWorld().getPlayer().fire(0, getFireMode(0));
            }
            if (buttonDown[1]) {
                root.getWorld().getPlayer().fire(1, getFireMode(1));
            }
        }
	}
}
