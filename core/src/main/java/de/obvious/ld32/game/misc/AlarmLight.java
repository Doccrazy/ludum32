package de.obvious.ld32.game.misc;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;

public class AlarmLight extends ConeLight{
	private ConeLight secondLight;

	public AlarmLight(RayHandler rayHandler, int rays, Color color, float distance, float x, float y, float directionDegree, float coneDegree) {
		super(rayHandler, rays, color, distance, x, y, directionDegree, coneDegree);
		secondLight = new ConeLight(rayHandler, rays, color, distance, x, y, directionDegree-180, coneDegree);
		setContactFilter((short)1,(short)0,(short)2);
		secondLight.setContactFilter((short)1,(short)0,(short)2);
		setSoftnessLength(0.5f);
		secondLight.setSoftnessLength(0.5f);
	}

	@Override
	public void update() {
		setDirection(direction + 1);
		secondLight.setDirection(direction -180);
		super.update();
	}


	@Override
	public void remove() {
		secondLight.remove();
		super.remove();
	}
}
