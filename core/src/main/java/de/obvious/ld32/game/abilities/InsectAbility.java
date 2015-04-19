package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.game.actor.InsectActor;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.actor.WorldActor;
import de.obvious.shared.game.base.CollisionListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class InsectAbility implements Ability {
	private GameWorld world;
	private WorldActor actor;
	private Texture texture;


	public InsectAbility(GameWorld world) {
		this.world = world;
		texture = Resource.GFX.insectWeapon;
	}

	@Override
	public void trigger(Vector2 position, FireMode mode) {
		if(mode == FireMode.PRIMARY){
			world.getPlayer().stopMovement();
			world.getPlayer().getBody().setLinearVelocity(world.getPlayer().aimDirection().nor().scl(30));

			InsectAbilityActor actor = new InsectAbilityActor(world, world.getPlayer().getWeaponMuzzle(), false);
			world.addActor(actor);

		}
	}

	@Override
	public Texture getTexture(FireMode mode) {
		return texture;
	}

    @Override
    public Animation getWeaponAnimation(boolean fire) {
        return Resource.GFX.weaponInsect[fire ? 1 : 0];
    }

    @Override
    public float getCooldown(FireMode mode) {
        return mode == FireMode.PRIMARY ? 1f : Float.MAX_VALUE;
    }
}

class InsectAbilityActor extends ShapeActor implements CollisionListener{

	public InsectAbilityActor(Box2dWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);

	}

	@Override
	protected void init() {
		super.init();
		body.setLinearVelocity(((GameWorld) world).getPlayer().aimDirection().nor().scl(30));
		task.in(0.11f, (Void) -> kill());
	}
	@Override
	public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
		if(other.getUserData() instanceof InsectActor){
			((InsectActor) other.getUserData()).damage(50);
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
		return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(0.40f)).fixSensor();
	}

}
