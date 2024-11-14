package map;

import java.util.ArrayList;

import main.GamePanel;
import tile.TileManager;

public class MapManager {
    GamePanel gp;
    public ArrayList<GameMap> maps = new ArrayList<>();

    public MapManager(GamePanel gp) {
        this.gp = gp;
        setMap();
    }

    public void setMap() {
        // OVERWORLD MAP
        addMap("overWorld Map", "tiles/tileset.png", "tilemap", "tiles/collisions.txt");

        // HOUSE 1 MAP
        addMap("House1 Map", "tiles/tileset.png", "homeMap", "tiles/collisions.txt");
    }

    private void addMap(String mapName, String tilesetPath, String baseLayerName, String collisionPath) {
        // Crea una nuova mappa e la aggiunge alla lista maps
        GameMap newMap = new GameMap(gp, mapName);
        maps.add(newMap);

        // Costruisce i percorsi dei layer usando il nome base fornito
        for (int i = 1; i <= 4; i++) {
                String layerPath = "maps/" + baseLayerName + "_layer" + i + ".csv";
                newMap.layers.add(new TileManager(gp, tilesetPath, layerPath, collisionPath));
        }
    }

}
