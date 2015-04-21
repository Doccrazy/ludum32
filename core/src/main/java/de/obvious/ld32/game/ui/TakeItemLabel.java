package de.obvious.ld32.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;

public class TakeItemLabel extends Label {
    private UiRoot root;
    private float stateTime;

    public TakeItemLabel(UiRoot root) {
        super("Press 1 or 2 to keep this ability", new LabelStyle(Resource.FONT.retroSmall, Color.WHITE));
        setAlignment(Align.center);
        setWidth(Constants.SCREEN_WIDTH);
        setY(570);
        this.root = root;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        setVisible(stateTime % 1f < 0.5f && root.getItemSlots().getPendingAbility() != null && root.getWorld().isGameInProgress());
    }
}
