package de.obvious.ld32.game.ai;

import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class FlatTiledGraph extends DefaultIndexedGraph<FlatTiledNode> implements TiledGraph<FlatTiledNode> {
    private int sizeX, sizeY;
    private TiledMap tiledMap;
    private TiledMapTileLayer mapLayer;

    public boolean diagonal;
    public FlatTiledNode startNode;

    public FlatTiledGraph(TiledMap tiledMap, String layer) {
        this.tiledMap = tiledMap;
        mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layer);

        for (int x = 0; x < mapLayer.getWidth(); x++) {
            for (int y = 0; y < mapLayer.getHeight(); y++) {
                Cell cell = mapLayer.getCell(x, y);
                nodes.add(new FlatTiledNode(this, x, y, cell != null ? FlatTiledNode.TILE_WALL : FlatTiledNode.TILE_FLOOR, 4));
            }
        }

        // Each node has up to 4 neighbors, therefore no diagonal movement is possible
        for (int x = 0; x < mapLayer.getWidth(); x++) {
            int idx = x * mapLayer.getHeight();
            for (int y = 0; y < mapLayer.getHeight(); y++) {
                FlatTiledNode n = nodes.get(idx + y);
                if (x > 0) addConnection(n, -1, 0);
                if (y > 0) addConnection(n, 0, -1);
                if (x < mapLayer.getWidth() - 1) addConnection(n, 1, 0);
                if (y < mapLayer.getHeight() - 1) addConnection(n, 0, 1);
            }
        }
    }

    @Override
    public FlatTiledNode getNode(int x, int y) {
        return nodes.get(x * mapLayer.getHeight() + y);
    }

    @Override
    public FlatTiledNode getNode (int index) {
        return nodes.get(index);
    }

    private void addConnection (FlatTiledNode n, int xOffset, int yOffset) {
        FlatTiledNode target = getNode(n.x + xOffset, n.y + yOffset);
        if (target.type == FlatTiledNode.TILE_FLOOR) n.getConnections().add(new FlatTiledConnection(this, n, target));
    }

    public int getWidth() {
        return mapLayer.getWidth();
    }

    public int getHeight() {
        return mapLayer.getHeight();
    }
}
