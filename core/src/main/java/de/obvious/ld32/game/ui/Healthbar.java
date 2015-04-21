package de.obvious.ld32.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.shared.game.world.GameState;

public class Healthbar extends Widget {
	private UiRoot root;

	public Healthbar(UiRoot root) {
		this.root = root;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (root.getWorld().getGameState() != GameState.GAME) {
		    return;
		}
		boolean dead = root.getWorld().getPlayer().getHealth() <= 0;
		float fill = MathUtils.clamp(getPlayer().getHealth() / GameRules.PLAYER_HEALTH, 0, 1);
		int w = (int) (getWidth()*fill);
		batch.draw(Resource.GFX.healthbarEmpty,
				getX(), getY(), getWidth(), getHeight(), 0, 0, (int)(getWidth()), (int)getHeight(), false, false);
		batch.draw(Resource.GFX.healthbarFull,
				getX(), getY(), w, getHeight(), 0, 0, w, (int)getHeight(), false, false);
	}

	private PlayerActor getPlayer() {
		return root.getWorld().getPlayer();
	}

	@Override
	public float getPrefWidth () {
		return Resource.GFX.healthbarFull.getWidth();
	}

	@Override
	public float getPrefHeight () {
		return Resource.GFX.healthbarFull.getHeight();
	}

	@Override
	public float getMinWidth() {
		return 0;
	}

	@Override
	public float getMinHeight() {
		return 0;
	}
}
