package object;

//obj pokeball
//used to get items
public class OBJ_PokeBall extends SuperObject {
    //constructor
    public OBJ_PokeBall() {
        this.name = "pokeBall";
        this.collision = false;
        this.pickable = true;
        loadImage("object/pokeball.png");
    }
}
