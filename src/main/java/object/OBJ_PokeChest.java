package object;

//object pokechest
//used to get items out of it
public class OBJ_PokeChest extends SuperObject{
    public boolean opened; //opened check if pokechest is opened, can only be opened once

    //constructor
    public OBJ_PokeChest() {
        this.name = "pokeChest";
        this.collision = true;
        this.pickable = false;
        this.opened = false;
        loadImage("object/poke-chest.png");
    }
}
