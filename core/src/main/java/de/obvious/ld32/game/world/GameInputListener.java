package de.obvious.ld32.game.world;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import de.obvious.ld32.game.ui.UiRoot;

public class GameInputListener extends InputListener {
    private GameWorld world;
    private UiRoot root;

    public GameInputListener(UiRoot root) {
        this.root = root;
        this.world = root.getWorld();
        reset();
    }

    public void reset() {

    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
    	if(keycode == Keys.SPACE){
    		world.getPlayer().switchAbilities();
    	}
    	if(keycode == Keys.ENTER){
    		world.getPlayer().changeUiText();
    	}

    	return true;
    }
}
