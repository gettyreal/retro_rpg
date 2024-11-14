package map;

import java.util.ArrayList;

import entity.Entity;
import main.AssetSetter;
import main.GamePanel;
import object.SuperObject;
import tile.TileManager;

public class GameMap {
    GamePanel gp;
    String name;
    public ArrayList<TileManager> layers = new ArrayList<>();
    // game entities and objects
    public ArrayList<Entity> npc = new ArrayList<>(); // npc entity arraylist
    public ArrayList<Entity> pokemons = new ArrayList<>(); // pokemon entitiy arraylist
    public ArrayList<SuperObject> obj = new ArrayList<>(); // obj array list
    public AssetSetter aSetter;; // instantiace obj and entities

    public GameMap(GamePanel gp,String name) {
        aSetter = new AssetSetter(gp, this);
        this.gp = gp;
        this.name = name;
    }
}
