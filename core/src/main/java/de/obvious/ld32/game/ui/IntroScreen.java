package de.obvious.ld32.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.shared.game.world.GameState;

public class IntroScreen extends Image {
    private UiRoot root;
    private float stateTime;

    public IntroScreen(UiRoot root) {
        super(Resource.GFX.screenIntro);
        this.root = root;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        setVisible(root.getWorld().getGameState() == GameState.INIT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (stateTime < Resource.GFX.obviousIndustries.getAnimationDuration()) {
            TextureRegion frame = Resource.GFX.obviousIndustries.getKeyFrame(stateTime);
            batch.draw(Resource.GFX.white, 0, 0, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, 1, 1, 0);
            batch.draw(frame, Constants.SCREEN_WIDTH/2 - frame.getRegionWidth()/2, Constants.SCREEN_HEIGHT/2 - frame.getRegionHeight()/2,
                    0, 0, frame.getRegionWidth(), frame.getRegionHeight(), 1, 1, 0);
        } else {
            super.draw(batch, parentAlpha);
        }
    }
}
