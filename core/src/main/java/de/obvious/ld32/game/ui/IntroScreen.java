package de.obvious.ld32.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import de.obvious.ld32.core.Resource;
import de.obvious.shared.game.world.GameState;

public class IntroScreen extends Image {
    private UiRoot root;

    public IntroScreen(UiRoot root) {
        super(Resource.GFX.screenIntro);
        this.root = root;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setVisible(root.getWorld().getGameState() == GameState.INIT);
    }
}
