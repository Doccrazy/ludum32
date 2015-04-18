package de.obvious.ld32.game.world;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import de.obvious.ld32.game.abilities.FireMode;
import de.obvious.ld32.game.ui.UiRoot;
import de.obvious.shared.game.world.GameState;

public class GameInputListener extends InputListener {
    private GameWorld world;
    private UiRoot root;

    public GameInputListener(UiRoot root) {
        this.root = root;
        this.world = root.getWorld();
        reset();
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (world.getGameState() != GameState.GAME) {
            return false;
        }
        if (button == 0 || button == 1) {
            world.getPlayer().fire(x, y, button, getFireMode(button));
        }
        return true;
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
        if (world.getGameState() != GameState.GAME) {
            return false;
        }
        world.getPlayer().setCrosshair(x, y);
        return true;
    }

    public void reset() {

    }
}
