package de.obvious.ld32.game.actor;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.DamageType;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.base.CollisionListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class BulletActor extends ShapeActor implements CollisionListener {

	private Vector2 velocity;
	private Color color;

	public BulletActor(Box2dWorld world, Vector2 spawn, Vector2 velocity, Color color) {
		super(world, spawn, false);
		this.velocity = velocity;
		this.color = color;

		task.in(5f, (Void) -> kill());
	}

	@Override
	protected void init() {
		super.init();
		PointLight light = new PointLight(world.rayHandler, 10, color, 0.01f, -10, -10);
		light.setXray(true);
		lights.add(light);
	}

	@Override
	protected void doAct(float delta) {
		super.doAct(delta);
		lights.get(0).setPosition(body.getPosition());
		float flicker = (float) Math.sin(Math.PI * 4 * (stateTime % 0.5f)) / 2f;
		lights.get(0).setDistance(1f + flicker / 2);
	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forDynamic(spawn).asBullet().velocity(velocity.scl(3)).fixShape(ShapeBuilder.circle(0.1f)).fixSensor();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(Resource.GFX.startWeaponBulletR, getX(), getY(), getOriginX(), getOriginY(), 1f / 8, 1f, 1f, 1f, velocity.angle() + 90, true);
		super.draw(batch, parentAlpha);
	}

    @Override
    public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
        if (other.getUserData() instanceof EnemyActor) {
            ((EnemyActor) other.getUserData()).damage(10, DamageType.NORMAL);
        }

        if(!other.isBullet() && !(other.getUserData() instanceof PlayerActor))
        	kill();
        return false;
    }

    @Override
    public void endContact(Body other) {
    }

    @Override
    public void hit(float force) {
    }
}