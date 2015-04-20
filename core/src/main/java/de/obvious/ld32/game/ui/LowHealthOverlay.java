package de.obvious.ld32.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.ld32.game.world.GameWorld;

public class LowHealthOverlay extends Image {
	private PlayerActor player;
	private float stateTime;

	public LowHealthOverlay(UiRoot root, GameWorld world) {
	    super(Resource.GFX.lowHealthOverlay);
		player = world.getPlayer();
	}

	@Override
	public void act(float delta) {
	    super.act(delta);
	    setVisible(player.getHealth() < GameRules.PLAYER_HEALTH * 0.2f);
	    if (isVisible()) {
	        stateTime += delta;
	        setColor(1, 1, 1, 0.3f + (float)Math.sin(stateTime*10) * 0.1f);
	    }
	}

}
