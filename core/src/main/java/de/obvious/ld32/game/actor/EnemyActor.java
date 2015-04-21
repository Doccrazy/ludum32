package de.obvious.ld32.game.actor;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
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
import de.obvious.ld32.data.ZOrder;
import de.obvious.ld32.game.abilities.Ability;
import de.obvious.ld32.game.misc.NewAbilityEvent;
import de.obvious.ld32.game.misc.StoryText;
import de.obvious.ld32.game.misc.UiTextEvent;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.ShapeBuilder;

public abstract class EnemyActor extends ShapeActor implements Damageable {

	protected float radius = 0.49f;
	protected float lives, initialLives = 100;
	protected Ability ability;
	protected float alpha = 0;
	protected boolean killed;
	protected Color bloodColor = Color.GREEN;

	public EnemyActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
	}

	@Override
	protected void init() {
	    super.init();
	    lives = initialLives;
	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(radius)).damping(0.99f, 0.9f).fixFilter((short) 1, (short) -1);
	}

	@Override
	protected void doAct(float delta) {
		super.doAct(delta);
	}

	@Override
	public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
	    boolean litUp = isVisibleForPlayer();
	    alpha = MathUtils.clamp(alpha + (litUp ? 0.05f : -0.05f), 0f, 1f);

		batch.setColor(1, 1, 1, alpha);
		if (lives < initialLives && lives > 0 && !killed)
			batch.draw(Resource.GFX.lifeBar, getX(), getY() + 3.25f * radius, getWidth() * lives / initialLives, getWidth() / 10f);
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
	    if (Math.abs(toOpp.angle(sight)) > ((GameWorld)world).getPlayer().getFlashlightConeDegree()) {
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
            if (fixture.getBody().getUserData() == null && !fixture.isSensor()) {
                hit = true;
                return 0f;
            }
            return 1f;
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

		ArrayList<StoryText> tmp = new ArrayList<StoryText>();
		tmp.add(new StoryText("You killed this monster! NOMNOMNOM", "Weapon"));
		tmp.add(new StoryText("I got a new ability, press TAB to change my passiv ability with my active ability.", "Weapon"));
		if(this instanceof InsectActor){
			Resource.SOUND.insectDead.play();
			tmp.add(new StoryText("Great! Now we can make a scarejump or get better sight and speed up.", "Weapon"));
		}
		if(this instanceof RootActor){
			tmp.add(new StoryText("Oh yeah! Now we can either do a tentacle attack or make sure to stand still while shooting.", "Weapon"));
		}
		if(this instanceof ShroomActor){
			Resource.SOUND.littleShroomExplode.play();
			tmp.add(new StoryText("Haha. Now we can either shoot little poison shrooms or heal you.", "Weapon"));
		}
		if(this instanceof SpikyActor){
			tmp.add(new StoryText("Yeah! We can now shoot a spikegranate or have a thornarmor.", "Weapon"));
		}

		world.postEvent(new UiTextEvent(tmp, true));
		BodyBuilder builder = createBody(pos).velocity(v).fixSensor().fixFilter((short)1, (short)0);
		body = builder.build(world);
        setzOrder(ZOrder.CORPSE);
	}

	public boolean isKilled() {
		return killed;
	}

	@Override
    public void damage(float amount, DamageType type) {
	    if (lives > 0) {
	        lives -= amount;
	        world.addActor(new BloodActor(world, body.getPosition(), bloodColor, amount));
	    }

        if (lives <= 0 && !isKilled()) {
            task.in(0, (Void) -> {
                doKillme(type);
            });
        }
	}

    protected void doKillme(DamageType type) {
        if (ability != null) {
            world.postEvent(new NewAbilityEvent(ability));
        }
        killme();
        task.in(GameRules.CORPSE_DESPAWN, (Void) -> kill());
    }

}
