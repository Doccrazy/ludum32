package de.obvious.ld32.game.actor.action;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.utils.rays.ParallelSideRayConfiguration;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.obvious.ld32.game.actor.EnemyActor;
import de.obvious.ld32.game.ai.AiUtils;
import de.obvious.ld32.game.ai.Box2dRaycastCollisionDetector;
import de.obvious.ld32.game.ai.Box2dSteeringEntity;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.Box2dActor;

public class AiAction extends Action {
    private StateMachine<AiAction> stateMachine = new DefaultStateMachine<>(this, null);
    private Pursue<Vector2> pursue;
    private Wander<Vector2> wander;
    private Box2dSteeringEntity steering;
    private State<AiAction> initialState;

    public AiAction(State<AiAction> initialState) {
        this.initialState = initialState;
    }

    @Override
    public void setActor(Actor actor) {
        super.setActor(actor);
        if (actor != null) {
            setupSteering((Box2dActor)actor);
        }
    }

    private void setupSteering(Box2dActor actor) {
        float radius = actor.getWidth()/2f;

        steering = new Box2dSteeringEntity(actor.getBody(), true, radius);
        steering.setMaxAngularAcceleration(50f);
        steering.setMaxAngularSpeed(10f);
        steering.setMaxLinearAcceleration(500f);
        steering.setMaxLinearSpeed(3f);

        ParallelSideRayConfiguration<Vector2> rayConfig = new ParallelSideRayConfiguration<Vector2>(steering, 2, radius);
        RaycastCollisionDetector<Vector2> raycastCollisionDetector = new Box2dRaycastCollisionDetector(actor.getWorld().box2dWorld);
        RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance = new RaycastObstacleAvoidance<Vector2>(steering, rayConfig,
                raycastCollisionDetector, 1);

        pursue = new Pursue<>(steering, ((GameWorld)actor.getWorld()).getPlayer().getSteering())
                .setMaxPredictionTime(0.5f)
                .setEnabled(false);

        wander = new Wander<>(steering)
                .setWanderRadius(2f)
                .setWanderRate(50f)
                .setEnabled(false);

        PrioritySteering<Vector2> prioritySteeringSB = new PrioritySteering<Vector2>(steering, 0.0001f) //
                .add(raycastObstacleAvoidance) //
                .add(pursue)
                .add(wander);
        steering.setSteeringBehavior(prioritySteeringSB);

        stateMachine.changeState(initialState);

        /*final Face<Vector2> faceSB = new Face<>(steering, player) //
                .setTimeToTarget(0.1f) //
                .setAlignTolerance(0.001f) //
                .setDecelerationRadius(MathUtils.degreesToRadians * 180);
        final Pursue<Vector2> pursue = new Pursue<>(steering, player)
                .setMaxPredictionTime(0.5f);*/

    }

    @Override
    public boolean act(float delta) {
        if (AiUtils.getPlayer(getActor()).isDead() || ((EnemyActor)getActor()).isKilled()) {
            return true;
        }
        stateMachine.update();
        steering.update(delta);

        /*Vector2 pp = new Vector2();
        pp.set(((GameWorld) world).getPlayer().getBody().getPosition());
        pp.x = Math.round(pp.x);
        pp.y = Math.round(pp.y);
        if (!lastPlayerPos.equals(pp)) {
            LinePath<Vector2> path = ((GameWorld) world).getLevel().searchPath(body.getPosition(), pp);
            if (path != null) {
                FollowPath<Vector2, LinePathParam> followPath = new FollowPath<>(steering, path)
                        .setArrivalTolerance(1f)
                        .setTarget(player)
                        .setTimeToTarget(0.1f)
                        .setDecelerationRadius(3f)
                        .setPredictionTime(1f);
                steering.setSteeringBehavior(followPath);
            }
            lastPlayerPos.set(pp);
        }*/

        return false;
    }

    public StateMachine<AiAction> getStateMachine() {
        return stateMachine;
    }

    public Box2dSteeringEntity getSteering() {
        return steering;
    }

    public void enableWander(boolean enable) {
        wander.setEnabled(enable);
    }

    public void enablePursue(boolean enable) {
        pursue.setEnabled(enable);
    }

    @Override
    public Box2dActor getActor() {
        return (Box2dActor) super.getActor();
    }

    public void startAttack() {
        System.out.println("Attack");
    }

    public void endAttack() {
        System.out.println("Stop");
    }
}
