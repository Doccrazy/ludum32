package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.DamageType;
import de.obvious.ld32.game.abilities.InsectAbility;
import de.obvious.ld32.game.actor.action.InsectAiAction;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.ShapeBuilder;

public class InsectActor extends EnemyActor {
	private boolean attacking;

    public InsectActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
		ability = new InsectAbility(world);
		bloodColor = Color.GREEN;
	}

    @Override
    protected void init() {
        super.init();
        addAction(new InsectAiAction());
    }

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(radius)).damping(0.99f, 0.9f);
	}

	@Override
	void drawBody(Batch batch) {
        Animation anim = attacking && !killed ? Resource.GFX.enemyInsectAttack[animationDir().ordinal()] : Resource.GFX.enemyInsect[animationDir().ordinal()];
        TextureRegion frame = anim.getKeyFrame(killed ? Math.min(stateTime, anim.getAnimationDuration()) : (isMoving() || attacking ? stateTime : 0), !killed);
		batch.draw(frame, getX(), getY(), getWidth(), getHeight() * 1.5f);

	}

	public void attack(float radius, float damage) {

	    attacking = true;
	    body.setActive(false);
	    stateTime = 0;
	    task.in(0.2f, (Void) -> {
	        if (((GameWorld)world).getPlayer().isDead() || killed) {
	            return;
	        }
	        float dist = ((GameWorld)world).getPlayer().getBody().getPosition().cpy().sub(body.getPosition()).len();
	        if (dist < radius) {
	        	Resource.SOUND.rootUp.play();
	            ((GameWorld)world).getPlayer().damage(damage, DamageType.MELEE);
	        }
	    });
	    task.in(Resource.GFX.enemyInsectAttack[0].getAnimationDuration(), (Void) -> {
	        attacking = false;
	        if (!killed) {
	            body.setActive(true);
	        }
	    });
	}

	public boolean isAttacking() {
        return attacking;
    }
}
