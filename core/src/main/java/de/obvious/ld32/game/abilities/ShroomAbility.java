package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.actor.ShroomLingActor;
import de.obvious.ld32.game.world.GameWorld;

public class ShroomAbility implements Ability {
	private GameWorld world;
	private Texture texture;

	public ShroomAbility(GameWorld world) {
		this.world = world;
		texture = Resource.GFX.shroomWeapon;
	}

	@Override
	public void trigger(Vector2 position, FireMode mode) {
	    if (mode == FireMode.PRIMARY) {
	        world.addActor(new ShroomLingActor(world, position, true));
	    } else {
	        world.getPlayer().addAction(new HealAction(world.getPlayer(), 50f, 3f));
	    }
	}

	@Override
	public Texture getTexture(FireMode mode) {
		return texture;
	}

	@Override
	public Animation getWeaponAnimation(boolean fire) {
		return Resource.GFX.weaponShroom[fire ? 1 : 0];
	}

	@Override
	public float getCooldown(FireMode mode) {
        return GameRules.COOLDOWN_SHROOM[mode.ordinal()];
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void end() {
		// TODO Auto-generated method stub

	}


}
