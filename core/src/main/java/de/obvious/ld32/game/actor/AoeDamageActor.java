package de.obvious.ld32.game.actor;

import java.util.HashSet;
import java.util.Set;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.data.Constants;
import de.obvious.ld32.data.DamageType;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.base.CollisionListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class AoeDamageActor extends ShapeActor implements CollisionListener {
    private float radius;
    private float damagePerSec;
    public boolean hitting;
    private float duration;
    private DamageType type;
    private Animation animation;
    private Color lightColor;
    private boolean friendly;
    private float warningTime;
    private boolean enlarge;
    private Set<Damageable> actorsInArea = new HashSet<>();

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
        if (stateTime - warningTime > duration) {
            kill();
        }
        if (stateTime > warningTime) {
            for (Damageable a : actorsInArea) {
                a.damage(damagePerSec * delta, type);
            }
        }
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(radius)).fixSensor();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (stateTime > warningTime) {
            TextureRegion frame = animation.getKeyFrame(stateTime - warningTime, true);
            if (enlarge) {
                float addition = radius*0.25f;
                batch.draw(frame, getX() - addition, getY() - addition, getOriginX() + addition, getOriginY() + addition,
                        getWidth() + addition*2, getHeight() + addition*2, getScaleX(), getScaleY(), 0);
            } else {
                float originX = (frame.getRegionWidth() * Constants.PIXEL_SCALE) / 2f;
                batch.draw(frame, getX() + getOriginX() - originX, getY(), originX, getOriginY(), frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE, getScaleX(),
                        getScaleY(), 0f);
            }
        }
    }

    @Override
    public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
        if ((friendly && other.getUserData() instanceof EnemyActor)
                || other.getUserData() instanceof PlayerActor) {
            actorsInArea.add((Damageable)other.getUserData());
        }
        return false;
    }

    @Override
    public void endContact(Body other) {
        actorsInArea.remove(other.getUserData());
    }

    @Override
    public void hit(float force) {
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    public void setWarningTime(float warningTime) {
        this.warningTime = warningTime;
    }

    public void setEnlarge(boolean enlarge) {
        this.enlarge = enlarge;
    }
}