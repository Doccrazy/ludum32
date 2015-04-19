package de.obvious.ld32.game.ai;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector2;

/** A raycast collision detector used for path smoothing against a {@link TiledGraph}.
 *
 * @param <N> Type of node, either flat or hierarchical, extending the {@link TiledNode} class
 *
 * @author davebaol */
public class TiledRaycastCollisionDetector<N extends TiledNode<N>> implements RaycastCollisionDetector<Vector2> {
	TiledGraph<N> worldMap;

	public TiledRaycastCollisionDetector (TiledGraph<N> worldMap) {
		this.worldMap = worldMap;
	}

	@Override
	public boolean collides (Ray<Vector2> ray) {
	    return collidesLineWidth((int)ray.start.x, (int)ray.start.y, (int)ray.end.x, (int)ray.end.y, 1.5f);
	}

	// See http://members.chello.at/~easyfilter/bresenham.html
	private boolean collidesLineWidth(int x0, int y0, int x1, int y1, float wd) {
        int dx = Math.abs(x1-x0), sx = x0 < x1 ? 1 : -1;
        int dy = Math.abs(y1-y0), sy = y0 < y1 ? 1 : -1;
        int err = dx-dy, e2, x2, y2;                          /* error value e_xy */
        float ed = dx+dy == 0 ? 1 : (float)Math.sqrt((float)dx*dx+(float)dy*dy);

        for (wd = (wd+1)/2; ; ) {                                   /* pixel loop */
            if (hitAA(x0,y0,Math.max(0,255*(Math.abs(err-dx+dy)/ed-wd+1)))) { return true; }
            e2 = err; x2 = x0;
            if (2*e2 >= -dx) {                                           /* x step */
                for (e2 += dy, y2 = y0; e2 < ed*wd && (y1 != y2 || dx > dy); e2 += dx)
                    if (hitAA(x0, y2 += sy, Math.max(0,255*(Math.abs(e2)/ed-wd+1)))) { return true; }
                if (x0 == x1) break;
                e2 = err; err -= dy; x0 += sx;
            }
            if (2*e2 <= dy) {                                            /* y step */
                for (e2 = dx-e2; e2 < ed*wd && (x1 != x2 || dx < dy); e2 += dy)
                    if (hitAA(x2 += sx, y0, Math.max(0,255*(Math.abs(e2)/ed-wd+1)))) { return true; }
                if (y0 == y1) break;
                err += dx; y0 += sy;
            }
       }
       return false;
    }

	private boolean hitAA(int x, int y, float j) {
        return worldMap.getNode(x, y).type != TiledNode.TILE_FLOOR;
    }

    @Override
	public boolean findCollision (Collision<Vector2> outputCollision, Ray<Vector2> inputRay) {
		throw new UnsupportedOperationException();
	}
}
