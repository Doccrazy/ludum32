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

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.AnimDir;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.data.Emotion;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.abilities.Ability;
import de.obvious.ld32.game.abilities.FireMode;
import de.obvious.ld32.game.actor.action.RageAction;
import de.obvious.ld32.game.ai.Box2dSteeringEntity;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class PlayerActor extends ShapeActor {
    public static final float RADIUS = 0.5f;

	private KeyboardMovementListener movement;
    private boolean moving;
    private Vector2 orientation = new Vector2(1, 1);
    private Vector2 crosshair = new Vector2();
    private Map<Integer, Ability> abilities = new HashMap<Integer, Ability>();
    private float animTime = 0f;
    private Float triggerTime;
    private ConeLight flashlight;
    private Box2dSteeringEntity steering;
    private float health = GameRules.PLAYER_HEALTH;
    private Emotion emotion = Emotion.NEUTRAL;
    private RageAction rage;


    public PlayerActor(Box2dWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
		flashlight = new ConeLight(world.rayHandler, 200, Color.WHITE, 10, getX(), getY(), 0, 40);
		flashlight.setContactFilter((short)1,(short)0,(short)2);
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
        if (movement != null) {
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

    private Vector2 aimDirection() {
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
        body.setLinearVelocity(mv.x * GameRules.PLAYER_VELOCITY, mv.y * GameRules.PLAYER_VELOCITY);
    }

	@Override
	public void draw(Batch batch, float parentAlpha) {
        Vector2 aim = aimDirection();
        boolean reverse = false;
        AnimDir direction;
        if (aim.x < -Math.sqrt(2)/2) {
            direction = AnimDir.LEFT;
            reverse = Math.signum(orientation.x) != Math.signum(aim.x) ? true : false;
        } else if (aim.x > Math.sqrt(2)/2) {
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
        drawWeapon(batch);
        drawHead(batch, direction, reverse, false);
        if (health < GameRules.PLAYER_HEALTH_WOUNDED) {
            drawHead(batch, direction, reverse, true);
        }

        flashlight.setDirection(aimDirection().angle());
        flashlight.setPosition(body.getPosition());



	    /*if (world.getGameState() == GameState.GAME) {
	        batch.draw(Resource.GFX.crosshair, crosshair.x - 0.25f, crosshair.y - 0.25f, 0.25f, 0.25f, 0.5f, 0.5f, 1f, 1f, 0f);
	    }*/
	}

    private void drawTorso(Batch batch, AnimDir direction, boolean reverse) {
        TextureRegion frame;
        Animation anim = Resource.GFX.player[direction.ordinal()];
        anim.setPlayMode(reverse ? PlayMode.REVERSED : PlayMode.NORMAL);
        frame = anim.getKeyFrame(animTime, true);

        float originX = (frame.getRegionWidth() * Constants.PIXEL_SCALE) / 2f;
        batch.draw(frame, getX() + getOriginX() - originX, getY(), originX, getOriginY(),
                frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE, getScaleX(), getScaleY(), 0f);
    }

    private void drawHead(Batch batch, AnimDir direction, boolean reverse, boolean wounded) {
        TextureRegion frame;
        Animation anim = wounded ? Resource.GFX.playerHeadWounded[direction.ordinal()] : Resource.GFX.playerHead.get(emotion)[direction.ordinal()];
        anim.setPlayMode(reverse ? PlayMode.REVERSED : PlayMode.NORMAL);
        frame = anim.getKeyFrame(animTime, true);

        float originX = (frame.getRegionWidth() * Constants.PIXEL_SCALE) / 2f;
        batch.draw(frame, getX() + getOriginX() - originX, getY(), originX, getOriginY(),
                frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE, getScaleX(), getScaleY(), 0f);
    }

    private void drawWeapon(Batch batch) {
        boolean firing = triggerTime != null;
        Animation anim = getWeaponAbility().getWeaponAnimation(firing);
        TextureRegion frame = anim.getKeyFrame(firing ? triggerTime : stateTime, !firing);

        Vector2 aim = aimDirection();
        float scaleY = aim.x < 0 ? -1 : 1;
        batch.draw(frame, getX() + RADIUS, getY() + RADIUS/2f, 0, (frame.getRegionHeight() * Constants.PIXEL_SCALE) / 2f,
                frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE, getScaleX(), getScaleY() * scaleY, aim.angle());
    }

    public Vector2 getCrosshair() {
        return crosshair;
    }

    public void setCrosshair(float x, float y) {
        this.crosshair.set(x, y);
    }

    public void fire(float x, float y, int slotNumber, FireMode mode) {
        Ability ability = abilities.get(slotNumber);
        if (ability != null) {
            ability.trigger(crosshair, mode);
        }
        triggerTime = 0f;
        rage.fight();
        System.out.println("Fire " + slotNumber + " " + x + " " + y);
    }

    public Ability getAbility(int slot) {
        return abilities.get(slot);
    }

    public void setAbility(int slot, Ability ability) {
        abilities.put(slot, ability);
    }

    private Ability getWeaponAbility() {
        return abilities.get(0);
    }

    public Box2dSteeringEntity getSteering() {
        return steering;
    }

    public void damage(float amount) {
        health -= amount;
        System.out.println(health);
        if (health <= 0) {
            kill();
        }
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
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