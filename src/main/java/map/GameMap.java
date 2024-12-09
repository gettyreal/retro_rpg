package map;

import java.util.ArrayList;
import entity.Entity;
import main.AssetSetter;
import main.GamePanel;
import object.SuperObject;
import tile.TileManager;

//map class
//used to handle a single map (es. pokecentre interior map)
public class GameMap {
    GamePanel gp; //game panel
    String name; //name of the map
    //arraylist containign layers of the map (in teory the are 3 and costant but got arraylist to future changes)
    public ArrayList<TileManager> layers = new ArrayList<>();
    //map entities and objects
    public ArrayList<Entity> npc = new ArrayList<>(); // npc entity arraylist
    public ArrayList<Entity> pokemons = new ArrayList<>(); // pokemon entitiy arraylist
    public ArrayList<SuperObject> obj = new ArrayList<>(); // obj array list
    // class aSetter to instantiace obj and entities
    public AssetSetter aSetter;

    //constructor
    //takes as parameter gamePanel and map name
    public GameMap(GamePanel gp,String name) {
        aSetter = new AssetSetter(gp, this);
        this.gp = gp;
        this.name = name;
    }
}
