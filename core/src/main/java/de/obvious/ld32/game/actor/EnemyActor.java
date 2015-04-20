package de.obvious.ld32.game.actor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.AnimDir;
import de.obvious.ld32.data.DamageType;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.abilities.Ability;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.ShapeBuilder;

public abstract class EnemyActor extends ShapeActor {

	protected float radius = 0.49f;
	protected int lives = 100;
	protected Map<Integer, Ability> abilities = new HashMap<Integer, Ability>();
	protected float alpha = 0;
	protected boolean killed;

	public EnemyActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(radius)).damping(0.99f, 0.9f).fixFilter((short) 1, (short) -1);
	}

	@Override
	protected void doAct(float delta) {
		super.doAct(delta);

		if (lives <= 0 && !isKilled()) {
			((GameWorld) world).getPlayer().setAbility(1, abilities.get(0));
			killme();
			task.in(GameRules.CORPSE_DESPAWN, (Void) -> kill());
		}
	}

	@Override
	public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
	    boolean litUp = isVisibleForPlayer();
	    alpha = MathUtils.clamp(alpha + (litUp ? 0.05f : -0.05f), 0f, 1f);

	    batch.setColor(1, 1, 1, alpha);
		if (lives < 100 && lives > 0)
			batch.draw(Resource.GFX.lifeBar, getX(), getY() + 3.25f*radius, getWidth() * lives / 100f, getWidth() / 10f);
		drawBody(batch);
		batch.setColor(1, 1, 1, 1);
	}

	private boolean isVisibleForPlayer() {
	    if (((GameWorld)world).getPlayer().isDead()) {
	        return false;
	    }

	    //distance from player
	    Body playerBody = ((GameWorld)world).getPlayer().getBody();
	    if (body.getPosition().dst2(playerBody.getPosition()) > 10*10) {
	        return false;
	    }

	    //player FOV
	    Vector2 toOpp = body.getPosition().cpy().sub(playerBody.getPosition());
	    Vector2 sight = new Vector2(1, 0).rotateRad(playerBody.getAngle());
	    if (Math.abs(toOpp.angle(sight)) > GameRules.PLAYER_HALF_FOV) {
	        return false;
	    }

	    //raycast for obstruction
	    RayListener listener = new RayListener();
        world.box2dWorld.rayCast(listener, playerBody.getPosition(), body.getPosition());
        return !listener.hit;
    }

	class RayListener implements RayCastCallback {
	    boolean hit;
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if (fixture.getBody() == body) {
                return -1f;
            }
            hit = true;
            return 0f;
        }
	}

    abstract void drawBody(Batch batch);

	protected AnimDir animationDir() {
	    if (killed) {
	        return AnimDir.DEAD;
	    }
	    if (!isMoving()) {
	        return AnimDir.DOWN;
	    }
	    Vector2 v = body.getLinearVelocity();
	    if (v.x > Math.abs(v.y)) {
	        return AnimDir.RIGHT;
	    } else if (v.x < -Math.abs(v.y)) {
	        return AnimDir.LEFT;
	    } else if (v.y > Math.abs(v.x)) {
	        return AnimDir.UP;
	    } else {
	        return AnimDir.DOWN;
	    }
	}

	protected boolean isMoving() {
	    return body.getLinearVelocity().len2() > 0.2f;
	}

	protected void killme() {
	    killed = true;
	    stateTime = 0;
	    Vector2 pos = body.getPosition();
	    Vector2 v = body.getLinearVelocity();
	    v.limit(1f);
	    world.box2dWorld.destroyBody(body);

	    BodyBuilder builder = createBody(pos).velocity(v).fixSensor();
	    body = builder.build(world);
	}

    public boolean isKilled() {
        return killed;
    }

    public void damage(int amount, DamageType type){
    	lives -= amount;
    }

}
