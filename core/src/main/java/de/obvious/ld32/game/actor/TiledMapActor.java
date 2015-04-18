package de.obvious.ld32.game.actor;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.shared.game.actor.WorldActor;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class TiledMapActor extends WorldActor {

	private OrthogonalTiledMapRenderer renderer;

	public TiledMapActor(Box2dWorld world) {
		super(world);
		renderer = new OrthogonalTiledMapRenderer(Resource.GFX.LEVEL1, 1f / 50f);
		createBodys();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		renderer.setView((OrthographicCamera) world.stage.getCamera());

		renderer.render();
		batch.begin();
	}

	@Override
	protected void doAct(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {

		}
	}

	private void createBodys() {
		TiledMap map = Resource.GFX.LEVEL1;
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get("Physic");

		ArrayList<Body> bodies = new ArrayList<Body>();

		for (int y = 0; y <= 10; y++) {
			for (int x = 0; x <= 10; x++) {
				if (mapLayer.getCell(x, y) != null) {

					Body body = BodyBuilder.forStatic(new Vector2(x + 1, y + 1)).fixShape(ShapeBuilder.box(1, 1)).build(world);

					bodies.add(body);
				}

			}
		}
	}

}
