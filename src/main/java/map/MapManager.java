package map;

import java.util.ArrayList;

import main.GamePanel;
import tile.TileManager;

//class to handle single maps
//each map has 3 layers
// layer 1 = ground
// layer 2 = under the player tiles
// layer 3 = above the player tiles
public class MapManager {
    GamePanel gp; //game panel 
    public ArrayList<GameMap> maps = new ArrayList<>(); //array list of all maps in the game

    //constructor
    public MapManager(GamePanel gp) {
        this.gp = gp;
        setMap();
    }

    //method to instantiate single maps
    //called in costructor
    public void setMap() {
        // OVERWORLD MAP, ID = 0
        addMap("overWorld Map", "Tilesets/Outside.png", "world map", "collisions/outdoors.tsj");
        //BIRCH LAB, ID = 1
        addMap("birch map", "Tilesets/Interior general.png", "birch lab", "collisions/Interior general.tsj");
        //PLAYERS HOUSE, ID = 2
        addMap("player house", "Tilesets/Interior general.png", "player house", "collisions/Interior general.tsj");
    }

    //method to add a single map
    //take as paramenter 
    // - map name = name of the map
    // - tilesetPath = file path of the tileset
    // - baselayer name = base name of each layer in the map
    // - collisionPah = file Path of the collision json correlated to the tileset
    private void addMap(String mapName, String tilesetPath, String baseLayerName, String collisionPath) {
        // creates a new map
        GameMap newMap = new GameMap(gp, mapName);
        //adds it to the array list of all maps
        maps.add(newMap);

        //load the 3 layers of the single map
        for (int i = 1; i <= 3; i++) {
                String layerPath = "maps/" + baseLayerName + "_layer" + i + ".csv";
                newMap.layers.add(new TileManager(gp, tilesetPath, layerPath, collisionPath));
        }   
    }

}
