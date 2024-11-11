package main;

import object.OBJ_Door;
import object.OBJ_PokeBall;
import object.OBJ_PokeChest;
import object.SuperObject;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    //adds obj into the game
    public void setObject() {
        addObject(new OBJ_PokeChest(), 27, 23);
        addObject(new OBJ_Door(), 24, 22);
        addObject(new OBJ_PokeBall(), 5, 10);
        addObject(new OBJ_PokeBall(), 5, 19);
        addObject(new OBJ_PokeBall(), 10, 24);
        addObject(new OBJ_PokeBall(), 17, 30);
        addObject(new OBJ_PokeBall(), 37, 11);
        addObject(new OBJ_PokeBall(), 45, 42);
        addObject(new OBJ_PokeBall(), 20, 45);
    }

    //adds a new object into the game
    //specify into the superovject parameter which subclass to pass 
    //example of usage = gp.obj.set(0, new OBJ_PokeChest());
    public void addObject(SuperObject obj, int tileColumm, int tileRow) {
        obj.worldX = tileColumm * gp.tileSize;
        obj.worldY = tileRow * gp.tileSize;
        gp.obj.add(obj);
    }

    //removes a object from the game
    //specify the index of the object to remove.
    public void removeObject(int index) {
        if (index >= 0 && index < gp.obj.size()) {
            gp.obj.remove(index);
        }
    }
    
}
