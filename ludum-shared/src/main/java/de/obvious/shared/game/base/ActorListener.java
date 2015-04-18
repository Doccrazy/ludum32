package de.obvious.shared.game.base;

import de.obvious.shared.game.actor.WorldActor;

public interface ActorListener {
	void actorAdded(WorldActor actor);

	void actorRemoved(WorldActor actor);
}
