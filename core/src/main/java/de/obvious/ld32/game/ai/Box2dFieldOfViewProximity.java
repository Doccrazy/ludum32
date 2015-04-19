package de.obvious.ld32.game.ai;

import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

/** A {@code Box2dFieldOfViewProximity} is a {@link Proximity} that queries the world for all fixtures that potentially overlap the
 * arc area of the circle having the specified detection radius and whose center is the owner position.
 *
 * @author davebaol */
public class Box2dFieldOfViewProximity extends Box2dSquareAABBProximity {

	private static final Vector2 toSteerable = new Vector2();
	private static final Vector2 ownerOrientation = new Vector2();

	float angle;
	float coneThreshold;

	public Box2dFieldOfViewProximity (Steerable<Vector2> owner, World world, float detectionRadius, float angle) {
		super(owner, world, detectionRadius);
		setAngle(angle);
	}

	public float getAngle () {
		return angle;
	}

	public void setAngle (float angle) {
		this.angle = angle;
		this.coneThreshold = (float)Math.cos(angle * 0.5f);
	}

	@Override
    @SuppressWarnings("unchecked")
	protected Steerable<Vector2> getSteerable (Fixture fixture) {
		return (Steerable<Vector2>)fixture.getBody().getUserData();
	}

	@Override
	protected void prepareAABB (AABB aabb) {
		super.prepareAABB(aabb);

		// Transform owner orientation to a Vector2
		owner.angleToVector(ownerOrientation, owner.getOrientation());
	}

	@Override
	protected boolean accept (Steerable<Vector2> steerable) {
		toSteerable.set(steerable.getPosition()).sub(owner.getPosition());

		// The bounding radius of the current body is taken into account
		// by adding it to the radius proximity
		float range = detectionRadius + steerable.getBoundingRadius();

		float toSteerableLen2 = toSteerable.len2();

		// Make sure the steerable is within the range.
		// Notice we're working in distance-squared space to avoid square root.
		if (toSteerableLen2 < range * range) {

			// Accept the steerable if it is within the field of view of the owner.
			return (ownerOrientation.dot(toSteerable) > coneThreshold);
		}

		// Reject the steerable
		return false;
	}

}
