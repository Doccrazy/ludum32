package de.obvious.ld32.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import de.obvious.ld32.core.Resource;
import de.obvious.shared.game.world.GameState;

public class VictoryScreen extends Image {
    private UiRoot root;

    public VictoryScreen(UiRoot root) {
        super(Resource.GFX.screenVictory);
        this.root = root;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setVisible(root.getWorld().getGameState() == GameState.VICTORY);
    }
}
