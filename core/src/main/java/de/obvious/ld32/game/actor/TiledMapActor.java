package de.obvious.ld32.game.actor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.WorldActor;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class TiledMapActor extends WorldActor {

	private OrthogonalTiledMapRenderer renderer;
	private HashMap<Point, Body> bodies;
	PlayerActor player;
	private Point lastPosition = new Point(0, 0);

	public TiledMapActor(Box2dWorld world) {
		super(world);
		renderer = new OrthogonalTiledMapRenderer(Resource.GFX.LEVEL1, 1f / 50f);
		bodies = new HashMap<Point, Body>();
		player = ((GameWorld) world).getPlayer();
//		createBodys();
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
		checkBodys();
	}

	private void checkBodys() {
		TiledMap map = Resource.GFX.LEVEL1;
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get("Physic");
		Vector2 playerPosition = player.getBody().getPosition();
		int playerX = (int) playerPosition.x;
		int playerY = (int) playerPosition.y;

		if (lastPosition.x == playerX && lastPosition.y == playerY)
			return;

		lastPosition.x = playerX;
		lastPosition.y = playerY;
		for (int y = playerY - 10; y <= playerY + 10; y++) {
			for (int x = playerX - 15; x <= playerX + 15; x++) {
				System.out.println("tile");
				if (mapLayer.getCell(x, y) == null)
					continue;
				if (bodies.get(new Point(x, y)) != null)
					continue;
				Body body = BodyBuilder.forStatic(new Vector2(x+0.5f , y+0.5f )).fixShape(ShapeBuilder.box(0.5f,  0.5f)).build(world);
				bodies.put(new Point(x, y), body);

			}

		}



		//untere Reihe zerstören

		for (int x = playerX - 15; x <= playerX + 15; x++){
			if (mapLayer.getCell(x, playerY - 11 ) == null)
				continue;
			if (bodies.get(new Point(x, playerY - 11)) != null){
				Body tmp = bodies.get(new Point(x, playerY - 11));
				 bodies.put(new Point(x,  playerY - 11), null );
				 world.box2dWorld.destroyBody(tmp);
			}

		}

		//obere Reihe zerstören

		for (int x = playerX - 15; x <= playerX + 15; x++){
			if (mapLayer.getCell(x, playerY + 11 ) == null)
				continue;
			if (bodies.get(new Point(x, playerY + 11)) != null){
				Body tmp = bodies.get(new Point(x, playerY + 11));
				 bodies.put(new Point(x,  playerY + 11), null );
				 world.box2dWorld.destroyBody(tmp);
			}

		}

		//linke Reihe zerstören

		for (int y = playerY - 10; y <= playerY + 10; y++){
			if (mapLayer.getCell(playerX - 16, y) == null)
				continue;
			if (bodies.get(new Point(playerX - 16, y)) != null){
				Body tmp = bodies.get(new Point(playerX - 16, y));
				 bodies.put(new Point(playerX - 16, y), null );
				 world.box2dWorld.destroyBody(tmp);
			}

		}

		//rechte Reihe zerstören

		for (int y = playerY - 10; y <= playerY + 10; y++){
			if (mapLayer.getCell(playerX + 16, y) == null)
				continue;
			if (bodies.get(new Point(playerX + 16, y)) != null){
				Body tmp = bodies.get(new Point(playerX + 16, y));
				 bodies.put(new Point(playerX + 16, y), null );
				 world.box2dWorld.destroyBody(tmp);
			}

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
