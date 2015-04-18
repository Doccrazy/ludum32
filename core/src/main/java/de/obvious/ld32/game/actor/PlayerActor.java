package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.GameRules;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.base.KeyboardMovementListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class PlayerActor extends ShapeActor {
	private KeyboardMovementListener movement;
    private boolean moving;

    public PlayerActor(Box2dWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(0.5f));
	}

    public void setupKeyboardControl() {
        movement = new KeyboardMovementListener();
        addListener(movement);
    }

    @Override
    protected void doAct(float delta) {
        if (movement != null) {
            move(delta);
        }

        super.doAct(delta);
    }

    private void move(float delta) {
        Vector2 mv = movement.getMovement();
        moving = Math.abs(mv.x) > 0 || Math.abs(mv.y) > 0;
        body.setLinearVelocity(mv.x * GameRules.PLAYER_VELOCITY, mv.y * GameRules.PLAYER_VELOCITY);
    }

	@Override
	public void draw(Batch batch, float parentAlpha) {
	    batch.draw(Resource.GFX.player, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
}
