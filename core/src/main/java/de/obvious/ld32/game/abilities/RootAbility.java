package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.DamageType;
import de.obvious.ld32.game.actor.EnemyActor;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.base.CollisionListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class RootAbility implements Ability {
	private GameWorld world;
	private Texture texture;
	private boolean isRooted = true;

	public RootAbility(GameWorld world) {
		this.world = world;
		texture = Resource.GFX.rootWeapon;
	}

	@Override
	public void trigger(Vector2 position, FireMode mode) {
		if (mode == FireMode.PRIMARY) {
			RootAbilityActor actor = new RootAbilityActor(world, world.getPlayer().getCrosshair());
			world.addActor(actor);
		} else {
			if (isRooted) {
				world.getPlayer().allowMovement(!isRooted);
				world.getPlayer().setRooted(isRooted);
				isRooted = false;
			} else {
				world.getPlayer().allowMovement(!isRooted);
				world.getPlayer().setRooted(isRooted);
				isRooted = true;
			}
		}
	}

	@Override
	public Texture getTexture(FireMode mode) {
		return texture;
	}

	@Override
	public Animation getWeaponAnimation(boolean fire) {
		return Resource.GFX.weaponRoot[fire ? 1 : 0];
	}

	@Override
	public float getCooldown(FireMode mode) {
		return mode == FireMode.PRIMARY ? 1f : 5f;
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void end() {
		if(Boolean.FALSE.equals(world.getPlayer().isRooted()) || world.getPlayer().isRooted() == null)
			return;
		world.getPlayer().allowMovement(!isRooted);
		world.getPlayer().setRooted(isRooted);
		isRooted = true;
	}

}

class RootAbilityActor extends ShapeActor implements CollisionListener {

	private GameWorld world;

	public RootAbilityActor(Box2dWorld world, Vector2 spawn) {
		super(world, spawn, false);
		this.world = (GameWorld) world;
		task.in(1, (Void) -> kill());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(Resource.GFX.RootProjectile.getKeyFrame(stateTime), getX(), getY(), 0, 0, 50 / 50f, 55 / 50f, 1, 1, 0);
	}

	@Override
	public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
		if (other.getUserData() instanceof EnemyActor) {
			((EnemyActor) other.getUserData()).damage(50, DamageType.NORMAL);
		}

		return false;
	}

	@Override
	public void endContact(Body other) {

	}

	@Override
	public void hit(float force) {

	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(0.5f)).fixSensor();
	}

}
