package de.obvious.ld32.game.ai;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

/** A node for a {@link FlatTiledGraph}.
 *
 * @author davebaol */
public class FlatTiledNode extends TiledNode<FlatTiledNode> {
	private FlatTiledGraph graph;

    public FlatTiledNode (FlatTiledGraph graph, int x, int y, int type, int connectionCapacity) {
		super(x, y, type, new Array<Connection<FlatTiledNode>>(connectionCapacity));
        this.graph = graph;
	}

	@Override
	public int getIndex () {
		return x * graph.getHeight() + y;
	}

}