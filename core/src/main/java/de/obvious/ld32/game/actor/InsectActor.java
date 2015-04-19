package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.game.abilities.InsectAbility;
import de.obvious.ld32.game.actor.action.AiAction;
import de.obvious.ld32.game.actor.action.MeleeState;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.ShapeBuilder;

public class InsectActor extends EnemyActor {


	public InsectActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
		abilities.put(0, new InsectAbility(world));

	}

	@Override
	protected void init() {
	    super.init();
	    addAction(new AiAction(MeleeState.IDLE));
	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(radius)).damping(0.99f, 0.9f);
	}

	@Override
	void drawBody(Batch batch) {
        Animation anim = Resource.GFX.enemyInsect[animationDir().ordinal()];
        TextureRegion frame = anim.getKeyFrame(killed ? Math.min(stateTime, anim.getAnimationDuration()) : (isMoving() ? stateTime : 0), !killed);
		batch.draw(frame, getX(), getY(), getWidth(), getHeight() * 1.5f);

	}

	@Override
	protected void doAct(float delta) {
		super.doAct(delta);
	}

}
