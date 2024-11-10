package object;

public class OBJ_PokeBall extends SuperObject {
    public OBJ_PokeBall() {
        this.name = "pokeBall";
        this.collision = false;
        this.pickable = true;
        loadImage("object/pokeball.png");
    }
}
