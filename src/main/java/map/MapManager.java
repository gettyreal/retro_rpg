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
        addMap("overWorld Map", "Tilesets/Outside.png", "world map", "collisions/outdoors.tsj");
        //BIRCH LAB
        addMap("birch map", "Tilesets/Interior general.png", "birch lab", "collisions/Interior general.tsj");
        //PLAYERS HOUSE
        addMap("player house", "Tilesets/Interior general.png", "player house", "collisions/Interior general.tsj");
    }

    private void addMap(String mapName, String tilesetPath, String baseLayerName, String collisionPath) {
        // Crea una nuova mappa e la aggiunge alla lista maps
        GameMap newMap = new GameMap(gp, mapName);
        maps.add(newMap);

        for (int i = 1; i <= 3; i++) {
                String layerPath = "maps/" + baseLayerName + "_layer" + i + ".csv";
                newMap.layers.add(new TileManager(gp, tilesetPath, layerPath, collisionPath));
        }   
    }

}
