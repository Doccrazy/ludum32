package de.obvious.ld32.game.actor;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.game.abilities.Ability;
import de.obvious.ld32.game.abilities.InsectAbility;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.base.CollisionListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.ShapeBuilder;

public class EnemyActor extends ShapeActor implements CollisionListener{
	private static final float RADIUS = 0.5f;
    private static final float REF_WIDTH = 50;
    private int lives = 100;
    private BufferedImage lifebar;

    private Map<Integer, Ability> abilities = new HashMap<Integer, Ability>();

	public EnemyActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
		super(world, spawn, spawnIsLeftBottom);
		abilities.put(0, new InsectAbility(world));

	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forStatic(spawn).fixShape(ShapeBuilder.circle(RADIUS));
	}

	@Override
	protected void doAct(float delta) {
		super.doAct(delta);


		if(lives <= 0){
			((GameWorld) world).getPlayer().setAbility(1, abilities.get(0));
			kill();
		}
	}

	@Override
	public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
		batch.draw(Resource.GFX.insect, getX(), getY(), getWidth(), getHeight()*1.5f);
		batch.draw(Resource.GFX.lifeBar,getX(), getY() + 1.6f, getWidth() * lives/100f, getWidth()/10f);

	}

	@Override
	public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
		if(other.getUserData() instanceof BulletActor){
			((BulletActor)other.getUserData()).kill();
			lives -= 10;
		}
		return false;
	}

	@Override
	public void endContact(Body other) {

	}

	@Override
	public void hit(float force) {

	};



}
