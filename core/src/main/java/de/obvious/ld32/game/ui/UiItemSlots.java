package de.obvious.ld32.game.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.game.abilities.Ability;
import de.obvious.ld32.game.abilities.FireMode;
import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.ld32.game.misc.NewAbilityEvent;
import de.obvious.ld32.game.world.GameWorld;

public class UiItemSlots extends Widget {

	private UiRoot root;
	private Map<Integer, Ability> abilities = new HashMap<Integer, Ability>();
	private GameWorld world;
	private Camera camera;
	private PlayerActor player;
    private Ability pendingAbility;
    private float abilityTime;

	public UiItemSlots(UiRoot root, GameWorld world) {
		this.root = root;
		this.world = world;
		camera = world.stage.getCamera();
		player = world.getPlayer();

	}

	@Override
	public void act(float delta) {
	    super.act(delta);
	    world.pollEvents(NewAbilityEvent.class, (NewAbilityEvent event) -> {
	        pendingAbility = event.getAbility();
	        abilityTime = 7f;
	    });
	    abilityTime = Math.max(0, abilityTime - delta);
	    if (abilityTime <= 0) {
	        pendingAbility = null;
	    }
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		if (world.isGameInProgress()) {
			batch.draw(Resource.GFX.itemSlot, Constants.SCREEN_WIDTH / 2 - 130, Constants.SCREEN_HEIGHT - 120, 100, 100);
			if (player.getAbility(0) != null) {
				batch.setColor(1, 1, 1, player.isCooldown(0, FireMode.PRIMARY) ? 0.5f : 1);
				batch.draw(player.getAbility(0).getTexture(FireMode.PRIMARY), Constants.SCREEN_WIDTH / 2 - 130, Constants.SCREEN_HEIGHT - 120, 100, 100);
				batch.setColor(1, 1, 1, 1);
			}
			batch.draw(Resource.GFX.itemSlot, Constants.SCREEN_WIDTH / 2 + 30, Constants.SCREEN_HEIGHT - 120, 100, 100);
			if (player.getAbility(1) != null) {
				batch.setColor(1, 1, 1, player.isCooldown(1, FireMode.ALTERNATE) ? 0.5f : 1);
				batch.draw(player.getAbility(1).getTexture(FireMode.ALTERNATE), Constants.SCREEN_WIDTH / 2 + 30, Constants.SCREEN_HEIGHT - 120, 100, 100);
				batch.setColor(1, 1, 1, 1);
			}
			batch.draw(Resource.GFX.tab, Constants.SCREEN_WIDTH / 2 - 25, Constants.SCREEN_HEIGHT - 100, 50, 50);

			if (pendingAbility != null) {
			    batch.setColor(1, 1, 1, abilityTime % 0.5f < 0.25f ? 1 : 0);
	            batch.draw(Resource.GFX.itemSlot, Constants.SCREEN_WIDTH / 2 - 50, Constants.SCREEN_HEIGHT - 220, 100, 100);
                batch.draw(pendingAbility.getTexture(FireMode.PRIMARY), Constants.SCREEN_WIDTH / 2 - 50, Constants.SCREEN_HEIGHT - 220, 100, 100);
                batch.setColor(1, 1, 1, 1);
			}
		}
		super.draw(batch, parentAlpha);

	}

	public Ability getPendingAbility() {
	    return pendingAbility;
	}

    public void clearPendingAbility() {
        pendingAbility = null;
    }
}
