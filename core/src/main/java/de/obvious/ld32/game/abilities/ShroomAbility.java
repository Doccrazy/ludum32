package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.WorldActor;

public class ShroomAbility implements Ability{
	private GameWorld world;
	private WorldActor actor;

	public ShroomAbility(GameWorld world, WorldActor actor) {
		this.world = world;
		this.actor = actor;

	}

	@Override
	public void trigger(Vector2 position, FireMode mode) {

	}

}
