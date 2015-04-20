package de.obvious.ld32.game.actor;

import java.util.HashMap;
import java.util.Map;

import box2dLight.ConeLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.AnimDir;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.data.DamageType;
import de.obvious.ld32.data.Emotion;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.abilities.Ability;
import de.obvious.ld32.game.abilities.FireMode;
import de.obvious.ld32.game.actor.action.RageAction;
import de.obvious.ld32.game.ai.Box2dSteeringEntity;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class PlayerActor extends ShapeActor {
	public static final float RADIUS = 0.49f;

	private KeyboardMovementListener movement;
	private boolean moving;
	private Vector2 orientation = new Vector2(1, 1);
	private Vector2 crosshair = new Vector2();
	private Map<Integer, Ability> abilities = new HashMap<Integer, Ability>();
	private float animTime = 0f;
	private Float triggerTime;
	private float rootTime = 0;
	private ConeLight flashlight;
	private Box2dSteeringEntity steering;
	private float health = GameRules.PLAYER_HEALTH;
	private Emotion emotion = Emotion.NEUTRAL;
	private RageAction rage;
	private boolean allowMovement = true;
	private float lastFireTime;
	private float playerSpeed = GameRules.PLAYER_VELOCITY;
	private Boolean rooted = null;

	public PlayerActor(Box2dWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
		flashlight = new ConeLight(world.rayHandler, 200, Color.WHITE, 10, getX(), getY(), 0, GameRules.PLAYER_HALF_FOV);
		flashlight.setContactFilter((short) 1, (short) 0, (short) 2);
		flashlight.setSoftnessLength(1f);
		lights.add(flashlight);
	}

	@Override
	protected void init() {
		super.init();
		steering = new Box2dSteeringEntity(body, true, RADIUS);
		addAction(rage = new RageAction());
	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(RADIUS));
	}

	public void setupKeyboardControl() {
		movement = new KeyboardMovementListener();
		addListener(movement);
	}

	@Override
	protected void doAct(float delta) {
		rootTime += delta;
		abilities.get(1).update(delta);
		if (movement != null && allowMovement) {
			move(delta);
		}
		Vector2 d = aimDirection();
		body.setTransform(body.getPosition(), d.angleRad());
		if (moving) {
			animTime += delta;
		}
		if (triggerTime != null) {
			triggerTime += delta;
			if (triggerTime > getWeaponAbility().getWeaponAnimation(true).getAnimationDuration()) {
				triggerTime = null;
			}
		}
		emotion = rage.isEnraged() ? Emotion.ANGRY : Emotion.NEUTRAL;
		super.doAct(delta);
	}

	private Vector2 _aimTmp = new Vector2();

	public Vector2 aimDirection() {
		_aimTmp.set(crosshair);
		return _aimTmp.sub(body.getPosition()).nor();
	}

	private void move(float delta) {
		Vector2 mv = movement.getMovement();
		moving = Math.abs(mv.x) > 0 || Math.abs(mv.y) > 0;
		if (moving) {
			orientation.x = Math.signum(mv.x);
			orientation.y = Math.signum(mv.y);
		}
		body.setLinearVelocity(mv.x * playerSpeed, mv.y * playerSpeed);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Vector2 aim = aimDirection();
		boolean reverse = false;
		AnimDir direction;
		if (aim.x < -Math.sqrt(2) / 2) {
			direction = AnimDir.LEFT;
			reverse = Math.signum(orientation.x) != Math.signum(aim.x) ? true : false;
		} else if (aim.x > Math.sqrt(2) / 2) {
			direction = AnimDir.RIGHT;
			reverse = Math.signum(orientation.x) != Math.signum(aim.x) ? true : false;
		} else if (aim.y > 0) {
			direction = AnimDir.UP;
			reverse = Math.signum(orientation.y) != Math.signum(aim.y) ? true : false;
		} else {
			direction = AnimDir.DOWN;
			reverse = Math.signum(orientation.y) != Math.signum(aim.y) ? true : false;
		}

		drawTorso(batch, direction, reverse);
		drawSecondary(batch);

		drawWeapon(batch);
		drawHead(batch, direction, reverse, false);
		if (health < GameRules.PLAYER_HEALTH_WOUNDED) {
			drawHead(batch, direction, reverse, true);
		}

		flashlight.setDirection(aimDirection().angle());
		flashlight.setPosition(body.getPosition());

		/*
		 * if (world.getGameState() == GameState.GAME) { batch.draw(Resource.GFX.crosshair, crosshair.x - 0.25f, crosshair.y - 0.25f, 0.25f, 0.25f, 0.5f, 0.5f, 1f, 1f, 0f); }
		 */
	}

	private void drawTorso(Batch batch, AnimDir direction, boolean reverse) {
		TextureRegion frame;
		Animation anim = Resource.GFX.player[direction.ordinal()];
		anim.setPlayMode(reverse ? PlayMode.REVERSED : PlayMode.NORMAL);
		frame = anim.getKeyFrame(animTime, true);

		float originX = (frame.getRegionWidth() * Constants.PIXEL_SCALE) / 2f;
		batch.draw(frame, getX() + getOriginX() - originX, getY(), originX, getOriginY(), frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE, getScaleX(),
				getScaleY(), 0f);
	}

	private void drawSecondary(Batch batch) {
		if(rooted == null)
			return;
		Animation anim = Resource.GFX.playerRoot;
		anim.setPlayMode(rooted ? PlayMode.NORMAL : PlayMode.REVERSED);
		TextureRegion frame;
		frame = anim.getKeyFrame(rootTime);
		float originX = (frame.getRegionWidth() * Constants.PIXEL_SCALE) / 2f;
		if (!anim.isAnimationFinished(rootTime) || rooted)
			batch.draw(frame, getX() + getOriginX() - originX, getY(), originX, getOriginY(), frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE,
					getScaleX(), getScaleY(), 0f);
	}

	private void drawHead(Batch batch, AnimDir direction, boolean reverse, boolean wounded) {
		TextureRegion frame;
		Animation anim = wounded ? Resource.GFX.playerHeadWounded[direction.ordinal()] : Resource.GFX.playerHead.get(emotion)[direction.ordinal()];
		anim.setPlayMode(reverse ? PlayMode.REVERSED : PlayMode.NORMAL);
		frame = anim.getKeyFrame(animTime, true);

		float originX = (frame.getRegionWidth() * Constants.PIXEL_SCALE) / 2f;
		batch.draw(frame, getX() + getOriginX() - originX, getY(), originX, getOriginY(), frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE, getScaleX(),
				getScaleY(), 0f);
	}

	private void drawWeapon(Batch batch) {
		boolean firing = triggerTime != null;
		Animation anim = getWeaponAbility().getWeaponAnimation(firing);
		TextureRegion frame = anim.getKeyFrame(firing ? triggerTime : stateTime, !firing);

		Vector2 aim = aimDirection();
		float scaleY = aim.x < 0 ? -1 : 1;
		batch.draw(frame, getX() + getOriginX(), getY() + RADIUS / 2f, 0, (frame.getRegionHeight() * Constants.PIXEL_SCALE) / 2f, frame.getRegionWidth() * Constants.PIXEL_SCALE,
				frame.getRegionHeight() * Constants.PIXEL_SCALE, getScaleX(), getScaleY() * scaleY, aim.angle());
	}

	public Vector2 getCrosshair() {
		return crosshair;
	}

	public void setCrosshair(float x, float y) {
		this.crosshair.set(x, y);
	}

	public void fire(int slotNumber, FireMode mode) {
		Ability ability = abilities.get(slotNumber);
		if (ability == null || isCooldown(slotNumber, mode)) {
			return;
		}
		ability.trigger(crosshair, mode);
		lastFireTime = stateTime;
		triggerTime = 0f;
		rage.fight();
		System.out.println("Fire " + slotNumber + " " + crosshair.x + " " + crosshair.y);
	}

	public Ability getAbility(int slot) {
		return abilities.get(slot);
	}

	public void setAbility(int slot, Ability ability) {
		Ability tmp = abilities.put(slot, ability);
		if (tmp != null)
			tmp.end();
	}

	private Ability getWeaponAbility() {
		return abilities.get(0);
	}

	public void switchAbilities(){
		if(Boolean.FALSE.equals(rooted) || rooted == null){
			Ability tmp = abilities.get(0);
			abilities.get(1).end();
			abilities.put(0, abilities.get(1));
			abilities.put(1, tmp);

		}
	}

	public Box2dSteeringEntity getSteering() {
		return steering;
	}

    public void damage(float amount, DamageType type) {
        health -= amount;
        ((GameWorld)world).addShake(Math.min(amount, 50f) / 50f);
        if (health <= 0) {
            kill();
        }
    }

    public float getHealth() {
		return health;
	}

	public Emotion getEmotion() {
		return emotion;
	}

	public void setEmotion(Emotion emotion) {
		this.emotion = emotion;
	}

	public void stopMovement() {
		allowMovement = false;
		body.setLinearVelocity(0, 0);
		task.in(0.1f, (Void) -> allowMovement = true);
	}

	public Vector2 getWeaponMuzzle() {
		Vector2 pos = body.getPosition().cpy();
		pos.y += RADIUS / 2f;
		return pos.add(aimDirection().scl(0.8f));
	}

	public void destroyMe(Body body, float time) {
		task.in(time, (Void) -> world.box2dWorld.destroyBody(body));
	}

	public boolean isCooldown(int slotNumber, FireMode mode) {
		Ability ability = abilities.get(slotNumber);
		if (ability == null) {
			return true;
		}
		if(Boolean.TRUE.equals(rooted))
			return lastFireTime + ability.getCooldown(mode)/2f > stateTime;
		return lastFireTime + ability.getCooldown(mode) > stateTime;

	}

	public void setFlashlightConeDegree(float degree) {
		flashlight.setConeDegree(degree);
	}

	public void setSpeedBonus(float bonus) {
		playerSpeed = GameRules.PLAYER_VELOCITY + bonus;
	}

	public void allowMovement(boolean allowIt){
		allowMovement = allowIt;
		body.setLinearVelocity(0, 0);
	}

	public void setRooted(boolean rooted) {
		rootTime = 0f;
		this.rooted = rooted;
	}

	public Boolean isRooted(){
		return rooted;
	}

}

class AnimOrientation {
	Animation anim;
	boolean flip;

	public AnimOrientation(Animation anim, boolean flip) {
		super();
		this.anim = anim;
		this.flip = flip;
	}
}