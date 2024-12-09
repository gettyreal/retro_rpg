package object;

//object stairs
//used to set stairs action
public class OBJ_Stairs extends SuperObject {
    // constructor
    // get as paramentet the action code for different behaviors
    public OBJ_Stairs(String actionCode) {
        this.name = "stairs";
        this.actionCode = actionCode;
        this.collision = false;
        this.pickable = false;
        loadImage("object/null.png");
    }
}
