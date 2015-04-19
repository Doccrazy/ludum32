package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.game.abilities.InsectAbility;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.ShapeBuilder;

public class InsectActor extends EnemyActor {

	public InsectActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
		abilities.put(0, new InsectAbility(world));

	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forStatic(spawn).fixShape(ShapeBuilder.circle(radius));
	}


	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(Resource.GFX.insect, getX(), getY(), getWidth(), getHeight() * 1.5f);
		super.draw(batch, parentAlpha);
	}

	@Override
	public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
		if (other.getUserData() instanceof BulletActor) {
			((BulletActor) other.getUserData()).kill();
			lives -= 10;
		}
		return false;
	}

	@Override
	public void endContact(Body other) {

	}

	@Override
	public void hit(float force) {

	};

}