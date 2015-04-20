package de.obvious.ld32.game.actor;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.data.DamageType;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.base.CollisionListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

class AoeDamageActor extends ShapeActor implements CollisionListener {
    private float radius;
    private float damagePerSec;
    public boolean hitting;
    private float duration;
    private DamageType type;
    private Animation animation;
    private Color lightColor;

    public AoeDamageActor(Box2dWorld world, Vector2 spawn, float radius, float damagePerSec, float duration,
            DamageType type, Animation animation, Color lightColor) {
        super(world, spawn, false);
        this.radius = radius;
        this.damagePerSec = damagePerSec;
        this.duration = duration;
        this.type = type;
        this.animation = animation;
        this.lightColor = lightColor;
    }

    @Override
    protected void init() {
        super.init();
        if (lightColor != null) {
            PointLight light = new PointLight(world.rayHandler, 10, lightColor, radius * 1.25f, 0, 0);
            light.attachToBody(body);
            light.setXray(true);
            lights.add(light);
        }
    }

    @Override
    protected void doAct(float delta) {
        super.doAct(delta);
        if (stateTime > duration) {
            kill();
        }
        if (hitting) {
            ((GameWorld)world).getPlayer().damage(damagePerSec * delta, type);
        }
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(radius)).fixSensor();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame = animation.getKeyFrame(stateTime, true);
        batch.draw(frame, getX() - radius*0.25f, getY() - radius*0.25f, getOriginX() + radius*0.25f, getOriginY() + radius*0.25f,
                getWidth() + radius*0.5f, getHeight() + radius*0.5f, getScaleX(), getScaleY(), 0);
    }

    @Override
    public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
        if (other.getUserData() instanceof PlayerActor) {
            hitting = true;
        }
        return false;
    }

    @Override
    public void endContact(Body other) {
        if (other.getUserData() instanceof PlayerActor) {
            hitting = false;
        }
    }

    @Override
    public void hit(float force) {
    }

}