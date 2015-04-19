package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	void drawBody(Batch batch) {
	    Animation anim = Resource.GFX.enemyShroom[animationDir().ordinal()];
		TextureRegion frame = anim.getKeyFrame(killed ? Math.min(stateTime, anim.getAnimationDuration()) : (isMoving() ? stateTime : 0), !killed);
		batch.draw(frame, getX(), getY(), getWidth(), getHeight() * 1.5f );

	}

	@Override
	public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
		if (other.getUserData() instanceof BulletActor) {
			((BulletActor) other.getUserData()).kill();
			((GameWorld)world).addShake(0.25f);
			lives -= 10;
		}
		return false;
	}

}
