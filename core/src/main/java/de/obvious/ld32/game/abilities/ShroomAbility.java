package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
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
	    world.addActor(new ShroomLingActor(world, position, true));
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
	    return mode == FireMode.PRIMARY ? 2f : Float.MAX_VALUE;
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
