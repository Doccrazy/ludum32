package de.obvious.ld32.game.actor;

import java.awt.Point;
import java.util.HashMap;

import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import de.obvious.ld32.game.ai.FlatTiledGraph;
import de.obvious.ld32.game.ai.FlatTiledNode;
import de.obvious.ld32.game.ai.TiledManhattanDistance;
import de.obvious.ld32.game.ai.TiledRaycastCollisionDetector;
import de.obvious.ld32.game.ai.TiledSmoothableGraphPath;
import de.obvious.ld32.game.misc.AlarmLight;
import de.obvious.ld32.game.misc.NotTheBestMapRenderer;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.WorldActor;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class TiledMapActor extends WorldActor {

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private HashMap<Point, Body> bodies;
	private HashMap<Point, Light> lights;
	PlayerActor player;
	private Point lastPosition = new Point(0, 0);
	int[] renderedLayers = { 0, 1, 2, 3 };

	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private FlatTiledGraph graph;
	private IndexedAStarPathFinder<FlatTiledNode> pathFinder;
	private PathSmoother<FlatTiledNode, Vector2> pathSmoother;
	private TiledManhattanDistance<FlatTiledNode> heuristic;
	private TiledSmoothableGraphPath<FlatTiledNode> path = new TiledSmoothableGraphPath<FlatTiledNode>();
	private LinePath<Vector2> linePath;

	public TiledMapActor(Box2dWorld world, TiledMap map) {
		super(world);
		this.map = map;
		renderer = new NotTheBestMapRenderer(map, 1f / 50f);
		bodies = new HashMap<Point, Body>();
		lights = new HashMap<Point, Light>();
		player = ((GameWorld) world).getPlayer();

		graph = new FlatTiledGraph(map, "Physic");
		heuristic = new TiledManhattanDistance<FlatTiledNode>();
		pathFinder = new IndexedAStarPathFinder<FlatTiledNode>(graph, true);
		pathSmoother = new PathSmoother<FlatTiledNode, Vector2>(new TiledRaycastCollisionDetector<FlatTiledNode>(graph));

	}

	public LinePath<Vector2> searchPath(Vector2 start, Vector2 end) {
		FlatTiledNode startNode = graph.getNode((int) start.x, (int) start.y);
		FlatTiledNode endNode = graph.getNode((int) end.x, (int) end.y);

		long startTime = TimeUtils.nanoTime();
		path.clear();
		pathFinder.searchNodePath(startNode, endNode, heuristic, path);
		pathSmoother.smoothPath(path);
		float elapsed = (TimeUtils.nanoTime() - startTime) / 1000000f;
		System.out.println("Path finding elapsed time (ms).. = " + elapsed);

		if (path.nodes.size < 2) {
			return null;
		}
		Array<Vector2> waypoints = new Array<>();
		for (FlatTiledNode node : path.nodes) {
			waypoints.add(new Vector2(node.x, node.y));
		}
		LinePath<Vector2> result = new LinePath<>(waypoints, true);
		linePath = result;
		return result;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		renderer.setView((OrthographicCamera) world.stage.getCamera());
		renderer.render(renderedLayers);

		/*shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(world.stage.getCamera().combined);
		shapeRenderer.setColor(Color.RED);
		int nodeCount = path.getCount();
		for (int i = 0; i < nodeCount; i++) {
			FlatTiledNode node = path.nodes.get(i);
			shapeRenderer.rect(node.x * 1, node.y * 1, 1, 1);
		}
		shapeRenderer.end();

		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Line);
		float hw = 1 / 2f;
		if (nodeCount > 0) {
			FlatTiledNode prevNode = path.nodes.get(0);
			for (int i = 1; i < nodeCount; i++) {
				FlatTiledNode node = path.nodes.get(i);
				shapeRenderer.line(node.x * 1 + hw, node.y * 1 + hw, prevNode.x * 1 + hw, prevNode.y * 1 + hw);
				prevNode = node;
			}
		}
		shapeRenderer.end();*/

		batch.begin();
	}

	@Override
	protected void doAct(float delta) {
        if (player.isDead()) {
            return;
        }
		Vector2 playerPosition = player.getBody().getPosition();
		int playerX = (int) playerPosition.x;
		int playerY = (int) playerPosition.y;
		if (lastPosition.x == playerX && lastPosition.y == playerY)
			return;

		checkBodys(1);
		checkBodys(4);

		lastPosition.x = playerX;
		lastPosition.y = playerY;

	}

	private void checkBodys(int layer) {
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(layer);
		Vector2 playerPosition = player.getBody().getPosition();
		int playerX = (int) playerPosition.x;
		int playerY = (int) playerPosition.y;

		for (int y = playerY - 10; y <= playerY + 10; y++) {
			for (int x = playerX - 15; x <= playerX + 15; x++) {
				if (mapLayer.getCell(x, y) == null)
					continue;

				if (layer == 4) {
					if(lights.get(new Point(x,y)) != null)
						continue;
					TiledMapTile tile = mapLayer.getCell(x, y).getTile();
					MapProperties props = tile.getProperties();
					if (props.containsKey("Alarm")) {
						lights.put(new Point(x, y), new AlarmLight(world.rayHandler, 10, Color.RED, 5f, x + 0.5f, y+0.5f, 0, 15));
					}
					if (props.containsKey("Licht")) {
						lights.put(new Point(x,y), new PointLight(world.rayHandler, 10, Color.GRAY, 5f, x +0.5f, y+0.5f));
					}

				} else {
					if (bodies.get(new Point(x, y)) != null)
						continue;
					Body body = BodyBuilder.forStatic(new Vector2(x + 0.5f, y + 0.5f)).fixShape(ShapeBuilder.box(0.5f, 0.5f)).fixFilter((short) 2, (short) -1).build(world);
					bodies.put(new Point(x, y), body);
				}

			}

		}

		// untere Reihe zerstören

		for (int x = playerX - 15; x <= playerX + 15; x++) {
			if (mapLayer.getCell(x, playerY - 11) == null)
				continue;

			if(lights.get(new Point(x, playerY-11)) != null){
				Light tmp = lights.get(new Point(x, playerY-11));
				lights.put(new Point(x, playerY-11), null);
				tmp.remove();
			}

			if (bodies.get(new Point(x, playerY - 11)) != null) {
				Body tmp = bodies.get(new Point(x, playerY - 11));
				bodies.put(new Point(x, playerY - 11), null);
				world.box2dWorld.destroyBody(tmp);
			}

		}

		// obere Reihe zerstören

		for (int x = playerX - 15; x <= playerX + 15; x++) {
			if (mapLayer.getCell(x, playerY + 11) == null)
				continue;

			if(lights.get(new Point(x, playerY+11)) != null){
				Light tmp = lights.get(new Point(x, playerY+11));
				lights.put(new Point(x, playerY+11), null);
				tmp.remove();
			}

			if (bodies.get(new Point(x, playerY + 11)) != null) {
				Body tmp = bodies.get(new Point(x, playerY + 11));
				bodies.put(new Point(x, playerY + 11), null);
				world.box2dWorld.destroyBody(tmp);
			}

		}

		// linke Reihe zerstören

		for (int y = playerY - 10; y <= playerY + 10; y++) {
			if (mapLayer.getCell(playerX - 16, y) == null)
				continue;
			if(lights.get(new Point(playerX - 16, y)) != null){
				Light tmp = lights.get(new Point(playerX - 16, y));
				lights.put(new Point(playerX - 16, y), null);
				tmp.remove();
			}
			if (bodies.get(new Point(playerX - 16, y)) != null) {
				Body tmp = bodies.get(new Point(playerX - 16, y));
				bodies.put(new Point(playerX - 16, y), null);
				world.box2dWorld.destroyBody(tmp);
			}

		}

		// rechte Reihe zerstören

		for (int y = playerY - 10; y <= playerY + 10; y++) {
			if (mapLayer.getCell(playerX + 16, y) == null)
				continue;
			if(lights.get(new Point(playerX + 16, y)) != null){
				Light tmp = lights.get(new Point(playerX + 16, y));
				lights.put(new Point(playerX + 16, y), null);
				tmp.remove();
			}
			if (bodies.get(new Point(playerX + 16, y)) != null) {
				Body tmp = bodies.get(new Point(playerX + 16, y));
				bodies.put(new Point(playerX + 16, y), null);
				world.box2dWorld.destroyBody(tmp);
			}

		}
	}

}
