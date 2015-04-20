package de.obvious.ld32.game.actor.action;

import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;

import de.obvious.ld32.game.actor.EnemyActor;
import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.Tasker;

public abstract class BaseAiAction extends Action {
    protected float stateTime;
    private Vector2 lastPlayerPos = new Vector2();
    private Vector2 lastActorPos = new Vector2();
    private LinePath<Vector2> cachedPath;
    protected Tasker task = new Tasker();

    @Override
    public final boolean act(float delta) {
        stateTime += delta;
        task.update(delta);
        if (getPlayer() == null || getPlayer().isDead() || getActor() == null || getActor().isDead() || getActor().isKilled()) {
            return true;
        }
        return doAct(delta);
    }

    protected abstract boolean doAct(float delta);

    protected void followPlayer(float dist, float speed) {
        pathToPlayer();
        if (cachedPath != null && distToPlayer() > dist) {
            Vector2 firstTarget = cachedPath.getSegments().first().getEnd();
            Vector2 nav = firstTarget.cpy().add(0.5f, 0.5f).sub(getActor().getBody().getPosition().cpy()).nor();
            getActor().getBody().setLinearVelocity(nav.scl(speed));
        } else {
            getActor().getBody().setLinearVelocity(0, 0);
        }
    }

    protected LinePath<Vector2> pathToPlayer() {
        if (getActor() == null) {
            return null;
        }

        Vector2 pp = new Vector2();
        pp.set(getWorld().getPlayer().getBody().getPosition());
        pp.x = (int)(pp.x);
        pp.y = (int)(pp.y);

        Vector2 ap = new Vector2();
        ap.set(getActor().getBody().getPosition());
        ap.x = (int)(ap.x);
        ap.y = (int)(ap.y);

        if (!lastPlayerPos.equals(pp) || !lastActorPos.equals(ap)) {
            cachedPath = getWorld().getLevel().searchPath(getActor().getBody().getPosition(), pp);
            lastPlayerPos.set(pp);
            lastActorPos.set(ap);
        }
        return cachedPath;
    }

    protected float distToPlayer() {
        Vector2 v = vecToPlayer();
        if (v == null) {
            return 0;
        }
        return v.len();
    }

    protected Vector2 vecToPlayer() {
        if (getActor() == null) {
            return null;
        }
        Vector2 pp = new Vector2(getWorld().getPlayer().getBody().getPosition());
        return pp.sub(getActor().getBody().getPosition());
    }

    protected boolean playerLos() {
        return cachedPath != null && cachedPath.getSegments().size == 1;
    }

    @Override
    public EnemyActor getActor() {
        return (EnemyActor) super.getActor();
    }

    protected PlayerActor getPlayer() {
        if (getActor() != null) {
            return getWorld().getPlayer();
        }
        return null;
    }

    protected GameWorld getWorld() {
        return getActor() == null ? null : (GameWorld)getActor().getWorld();
    }
}
