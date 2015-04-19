package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.game.abilities.ShroomAbility;
import de.obvious.ld32.game.world.GameWorld;

public class ShroomActor extends EnemyActor{

	public ShroomActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
		abilities.put(0, new ShroomAbility(world));

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(Resource.GFX.shroom, getX(), getY(), getWidth(), getHeight() * 1.5f );

	}

	@Override
	public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
		if (other.getUserData() instanceof BulletActor) {
			((BulletActor) other.getUserData()).kill();
			lives -= 10;
		}
		return false;
	}

}
