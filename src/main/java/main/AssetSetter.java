package main;

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
        } else {
            System.out.println("Index out of bounds.");
        }
    }
    
}
