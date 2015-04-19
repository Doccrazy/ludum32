package de.obvious.ld32.game.actor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.game.abilities.Ability;
import de.obvious.ld32.game.actor.action.AiAction;
import de.obvious.ld32.game.actor.action.MeleeState;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.base.CollisionListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.ShapeBuilder;

public class EnemyActor extends ShapeActor implements CollisionListener {

	protected static final float RADIUS = 0.5f;
	protected int lives = 100;
	protected Map<Integer, Ability> abilities = new HashMap<Integer, Ability>();

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
		return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(RADIUS)).damping(0.99f, 0.9f);
	}

	@Override
	protected void doAct(float delta) {
		super.doAct(delta);

		if (lives <= 0) {
			((GameWorld) world).getPlayer().setAbility(1, abilities.get(0));
			kill();
		}
	}

	@Override
	public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
		if (lives <= 100)
			batch.draw(Resource.GFX.lifeBar, getX(), getY() + 1.6f, getWidth() * lives / 100f, getWidth() / 10f);

	}

	@Override
	public void hit(float force) {

	}

}
