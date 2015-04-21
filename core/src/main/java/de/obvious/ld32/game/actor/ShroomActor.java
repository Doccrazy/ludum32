package de.obvious.ld32.game.actor;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.data.DamageType;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.abilities.ShroomAbility;
import de.obvious.ld32.game.actor.action.ShroomAiAction;
import de.obvious.ld32.game.misc.StoryText;
import de.obvious.ld32.game.misc.UiTextEvent;
import de.obvious.ld32.game.world.GameWorld;

public class ShroomActor extends EnemyActor{
    private int stunned = 0;

	public ShroomActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
		ability = new ShroomAbility(world);
        bloodColor = Color.PINK;
	}

    @Override
    protected void init() {
        super.init();
        addAction(new ShroomAiAction());
    }

	@Override
	void drawBody(Batch batch) {
	    TextureRegion frame;
        if (stunned == 1) {
            frame = Resource.GFX.enemyShroomStunned.getKeyFrame(stateTime);
        } else if (stunned == 2) {
            frame = Resource.GFX.enemyShroomShake.getKeyFrame(stateTime, true);
        } else if (stunned == 3) {
            frame = Resource.GFX.enemyShroomStunned.getKeyFrame(Math.max(0, Resource.GFX.enemyShroomStunned.getAnimationDuration() - stateTime), false);
        } else {
            Animation anim = Resource.GFX.enemyShroom[animationDir().ordinal()];
            frame = anim.getKeyFrame(killed ? Math.min(stateTime, anim.getAnimationDuration()) : (isMoving() ? stateTime : 0), !killed);
        }
		batch.draw(frame, getX(), getY(), getWidth(), getHeight() * 1.5f );

		if (isStunned() && lives < initialLives) {
		    frame = Resource.GFX.enemyShroomHeal.getKeyFrame(stateTime, true);
	        float originX = (frame.getRegionWidth() * Constants.PIXEL_SCALE) / 2f;
		    batch.draw(frame, getX() + getOriginX() - originX, getY(), originX, getOriginY(), frame.getRegionWidth() * Constants.PIXEL_SCALE,
		            frame.getRegionHeight() * Constants.PIXEL_SCALE, getScaleX(), getScaleY(), 0f);
		}

        Animation anim = Resource.GFX.enemyShroom[animationDir().ordinal()];
		float blowTime = anim.getAnimationDuration() * 0.75f;
		if (killed && stateTime > blowTime) {
		    float progress = Interpolation.exp10Out.apply(Math.min(stateTime - blowTime, anim.getAnimationDuration()) / anim.getAnimationDuration());
		    for (int i = 0; i < 5; i++) {
		        Vector2 pos = new Vector2(getX(), getY());
		        Vector2 d = new Vector2(1, 0);
		        d.rotate(i* 50);
		        pos.add(d.scl(0.4f * progress));
		        batch.draw(Resource.GFX.enemyShroomParts[i], pos.x, pos.y, getWidth(), getHeight() * 1.5f );
	        }
		}
	}

	@Override
	protected void doAct(float delta) {
	    super.doAct(delta);
	    if (isStunned()) {
	        lives = Math.min(initialLives, lives + GameRules.SHROOM_HEAL_PS * delta);
	        if (lives >= initialLives && stunned < 3) {
	            unstun();
	        }
	    }
	}

	public void stun() {
	    if (isStunned()) {
	        return;
	    }
	    body.setLinearVelocity(0, 0);
	    stunned = 1;
	    stateTime = 0;
	    ArrayList<StoryText> tmp = new ArrayList<StoryText>();
	    tmp.add(new StoryText("That little shit is just stunned...", "Weapon"));
	    tmp.add(new StoryText("Lets get back later with another ability", "Weapon"));
	    world.postEvent(new UiTextEvent(tmp, true));
	    task.in(Resource.GFX.enemyShroomStunned.getAnimationDuration(), (Void) -> stunned = 2);
	}

	public void unstun() {
		Resource.SOUND.rootUp.play();
	    stunned = 3;
	    stateTime = 0;
        task.in(Resource.GFX.enemyShroomStunned.getAnimationDuration(), (Void) -> stunned = 0);
	}

	public boolean isStunned() {
	    return stunned > 0;
	}

	@Override
	protected void doKillme(DamageType type) {
	    if (type == DamageType.MELEE) {
	        super.doKillme(type);
	    } else {
	        stun();
	    }
	}
}
