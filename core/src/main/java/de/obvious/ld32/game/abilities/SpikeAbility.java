package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.DamageType;
import de.obvious.ld32.game.actor.EnemyActor;
import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.base.CollisionListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class SpikeAbility implements Ability {

	private Texture texture;
	private GameWorld world;

	public SpikeAbility(GameWorld world) {
		this.texture = Resource.GFX.spikeWeapon;
		this.world = world;
	}

	@Override
	public void trigger(Vector2 position, FireMode mode) {
		if (mode == FireMode.PRIMARY) {
			Vector2 d = position.cpy().sub(world.getPlayer().getBody().getPosition());
			Vector2 distance = d.cpy();
			d.nor();
			world.addActor(new SpikeAbilityActor(world, new Vector2(world.getPlayer().getBody().getPosition().x, world.getPlayer().getBody().getPosition().y + PlayerActor.RADIUS / 2), false, d, distance));
		}
	}

	@Override
	public Texture getTexture(FireMode mode) {
		return texture;
	}

	@Override
	public Animation getWeaponAnimation(boolean fire) {
		return Resource.GFX.weaponSpike[fire ? 1 : 0];
	}

	@Override
	public float getCooldown(FireMode mode) {
		return mode == FireMode.PRIMARY ? 3f : Float.MAX_VALUE;
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

class SpikeAbilityActor extends ShapeActor implements CollisionListener {

	private Vector2 velocity;
	private Vector2 dest;
	private GameWorld world;
	private boolean bulletEx;
	private float bulletSpeed = 5;
	private Vector2 distance;
	private float timeToBoom;
	private float time;

	public SpikeAbilityActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom, Vector2 velocity, Vector2 distance) {
		super(world, spawn, spawnIsLeftBottom);
		this.velocity = velocity;
		this.distance = distance;
		this.dest = world.getPlayer().getCrosshair().cpy();
		this.world = world;

		timeToBoom = distance.len() / velocity.cpy().scl(bulletSpeed).len();
		// task.in(10f, (Void) -> kill());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (!bulletEx)
			batch.draw(Resource.GFX.spikeBigProjectile.getKeyFrame(stateTime, true), getX(), getY(), getOriginX(), getOriginY(), 46 / 50f, 7 / 50f, 1, 1, velocity.angle());
		else
			batch.draw(Resource.GFX.spikeExplosion.getKeyFrame(time, false), getX() - 100 / 50f / 2f, getY() - 100 / 50f / 2f, getOriginX(), getOriginY(), 100 / 50f, 100 / 50f, 1, 1, 0);

	}

	@Override
	public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {

		if (other.getUserData() instanceof PlayerActor || other.getUserData() instanceof LittleSpikeAbilityActor)
			return false;

		if (!bulletEx)
			task.in(0.01f, (Void) -> createNewProjectiles(body.getPosition())).then(0.3f, (Void) -> kill());

		return false;
	}

	@Override
	protected void doAct(float delta) {
		super.doAct(delta);
		time += delta;
		if(time >= timeToBoom && !bulletEx){
			task.in(0.01f, (Void) -> createNewProjectiles(dest)).then(0.3f, (Void) -> kill());
			time = 0;
		}

	}

	private void createNewProjectiles(Vector2 position) {
		bulletEx = true;
		Vector2 d = new Vector2(1, 0);

		for (int i = 0; i <= 9; i++) {
			world.addActor(new LittleSpikeAbilityActor(world, position.cpy().add(d.cpy().scl(0.2f)), d.cpy()));
			d.rotate(40);
		}

		body.setLinearVelocity(0, 0);


	}

	@Override
	public void endContact(Body other) {

	}

	@Override
	public void hit(float force) {

	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forDynamic(spawn).asBullet().velocity(velocity.cpy().scl(bulletSpeed)).fixShape(ShapeBuilder.circle(0.1f)).fixSensor();
	}

}

class LittleSpikeAbilityActor extends ShapeActor implements CollisionListener {

	private Vector2 velocity;
	private int speed = 7;
	private int hitCounter;

	public LittleSpikeAbilityActor(Box2dWorld world, Vector2 spawn, Vector2 velocity) {
		super(world, spawn, false);
		this.velocity = velocity;
		task.in(10f, (Void) -> kill());
	}

	@Override
	public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
		if (other.getUserData() instanceof EnemyActor) {
			((EnemyActor) other.getUserData()).damage(10, DamageType.NORMAL);
		}

		if(other.getUserData() instanceof PlayerActor){
			((PlayerActor) other.getUserData()).damage(10, DamageType.NORMAL);
		}

		if(other.getUserData() == null && hitCounter < 2){
			velocity.rotate(180);
			body.setLinearVelocity(velocity);
			hitCounter++;
		}

		if (!other.isBullet() && (other.getUserData() != null || hitCounter >= 2))
			kill();

		return false;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(Resource.GFX.spikeLittleProjectile.getKeyFrame(stateTime, true), getX(), getY(), getOriginX(), getOriginY(), 24 / 50f, 5 / 50f, 1, 1, velocity.angle());
	}

	@Override
	public void endContact(Body other) {
	}

	@Override
	public void hit(float force) {
	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forDynamic(spawn).asBullet().velocity(velocity.scl(speed)).fixShape(ShapeBuilder.circle(0.1f)).fixSensor();
	}

}
