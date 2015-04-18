package de.obvious.ld32.game.actor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.abilities.Ability;
import de.obvious.ld32.game.abilities.FireMode;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.GameState;
import de.obvious.shared.game.world.ShapeBuilder;

public class PlayerActor extends ShapeActor {
    private static final float RADIUS = 0.5f;
    private static final float REF_WIDTH = 50;

	private KeyboardMovementListener movement;
    private boolean moving;
    private Vector2 crosshair = new Vector2();
    private Map<Integer, Ability> abilities = new HashMap<Integer, Ability>();

    public PlayerActor(Box2dWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
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
        Vector2 d = crosshair.cpy().sub(body.getPosition());
        body.setTransform(body.getPosition(), d.angleRad());
        super.doAct(delta);
    }

    private void move(float delta) {
        Vector2 mv = movement.getMovement();
        moving = Math.abs(mv.x) > 0 || Math.abs(mv.y) > 0;
        body.setLinearVelocity(mv.x * GameRules.PLAYER_VELOCITY, mv.y * GameRules.PLAYER_VELOCITY);
    }

	@Override
	public void draw(Batch batch, float parentAlpha) {
	    //batch.draw(Resource.GFX.player, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight() * 1.5f, getScaleX(), getScaleY(), 0f);
	    TextureRegion frame = Resource.GFX.playerLeft.getKeyFrame(stateTime, true);
	    float scale = 2f * RADIUS / REF_WIDTH;
	    float offsetX = (frame.getRegionWidth() - REF_WIDTH) * scale;
	    batch.draw(Resource.GFX.playerLeft.getKeyFrame(stateTime, true), getX() - offsetX/2f, getY(), getOriginX(), getOriginY(),
	            frame.getRegionWidth() * scale, frame.getRegionHeight() * scale, getScaleX(), getScaleY(), 0f);
	    if (world.getGameState() == GameState.GAME) {
	        batch.draw(Resource.GFX.crosshair, crosshair.x - 0.25f, crosshair.y - 0.25f, 0.25f, 0.25f, 0.5f, 0.5f, 1f, 1f, 0f);
	    }
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
        System.out.println("Fire " + slotNumber + " " + x + " " + y);
    }

    public Ability getAbility(int slot) {
        return abilities.get(slot);
    }

    public void setAbility(int slot, Ability ability) {
        abilities.put(slot, ability);
    }
}
