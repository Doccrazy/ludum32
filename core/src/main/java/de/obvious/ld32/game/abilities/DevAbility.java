package de.obvious.ld32.game.abilities;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class DevAbility implements Ability {
    private GameWorld world;
    private Color color;

    public DevAbility(GameWorld world, Color color) {
        this.world = world;
        this.color = color;
    }

    @Override
    public void trigger(Vector2 position, FireMode mode) {
        Vector2 d = position.cpy().sub(world.getPlayer().getBody().getPosition());
        d.nor().scl(5f);
        world.addActor(new DevActor(world, world.getPlayer().getBody().getPosition(), d, color));
    }

}

class DevActor extends ShapeActor {
    private Vector2 velocity;
    private Color color;

    public DevActor(Box2dWorld world, Vector2 spawn, Vector2 velocity, Color color) {
        super(world, spawn, false);
        this.velocity = velocity;
        this.color = color;
    }

    @Override
    protected void init() {
        super.init();
        PointLight light = new PointLight(world.rayHandler, 10, color, 1f, -10, -10);
        light.setXray(true);
        lights.add(light);
    }

    @Override
    protected void doAct(float delta) {
        super.doAct(delta);
        lights.get(0).setPosition(body.getPosition());
        float flicker = (float) Math.sin(Math.PI * 4 * (stateTime % 0.5f))/2f;
        lights.get(0).setDistance(1f + flicker/2);
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).asBullet()
                .velocity(velocity)
                .fixShape(ShapeBuilder.circle(0.1f)).fixSensor();
    }

}