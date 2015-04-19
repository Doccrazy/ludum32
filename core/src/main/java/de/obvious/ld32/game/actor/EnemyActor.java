package de.obvious.ld32.game.actor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.AnimDir;
import de.obvious.ld32.game.abilities.Ability;
import de.obvious.ld32.game.actor.action.AiAction;
import de.obvious.ld32.game.actor.action.MeleeState;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.base.CollisionListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.ShapeBuilder;

public abstract class EnemyActor extends ShapeActor implements CollisionListener {

	protected static final float RADIUS = 0.5f;
	protected int lives = 100;
	protected Map<Integer, Ability> abilities = new HashMap<Integer, Ability>();
	protected float alpha = 0;
	protected boolean killed;

	public EnemyActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
	}

	@Override
	public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
		return false;

	}

	@Override
	public void endContact(Body other) {
	}

	@Override
	protected void init() {
		super.init();
		addAction(new AiAction(MeleeState.IDLE));
	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(RADIUS)).damping(0.99f, 0.9f).fixFilter((short) 1, (short) -1);
	}

	@Override
	protected void doAct(float delta) {
		super.doAct(delta);

		if (lives <= 0 && !isKilled()) {
			((GameWorld) world).getPlayer().setAbility(1, abilities.get(0));
			killme();
			task.in(5, (Void) -> kill());
		}
	}

	@Override
	public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
	    boolean litUp = world.rayHandler.pointAtLight(getX(), getY()) || world.rayHandler.pointAtLight(getX() + 2 * RADIUS, getY() + 2 * RADIUS);
	    alpha = MathUtils.clamp(alpha + (litUp ? 0.05f : -0.05f), 0f, 1f);

	    batch.setColor(1, 1, 1, alpha);
		if (lives < 100 && lives > 0)
			batch.draw(Resource.GFX.lifeBar, getX(), getY() + 1.6f, getWidth() * lives / 100f, getWidth() / 10f);
		drawBody(batch);
		batch.setColor(1, 1, 1, 1);
	}

	abstract void drawBody(Batch batch);

	@Override
	public void hit(float force) {

	}

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
	}

    public boolean isKilled() {
        return killed;
    }

    public void damage(int amount){
    	lives -= amount;
    }

}
