package object;

public class OBJ_PokeChest extends SuperObject{
    public boolean opened;

    public OBJ_PokeChest() {
        this.name = "pokeChest";
        this.collision = true;
        this.pickable = false;
        this.opened = false;
        loadImage("object/poke-chest.png");
    }
}
