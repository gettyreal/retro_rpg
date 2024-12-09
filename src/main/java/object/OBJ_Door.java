package object;

//object door
//used to get door interactions
public class OBJ_Door extends SuperObject{ //door wont be visible because the image is visible on tilemaps.
    //constructor
    //get as paramentet the action code for different behaviors
    public OBJ_Door(String actionCode) {
        this.name = "door"; 
        this.actionCode = actionCode;
        this.collision = false;
        this.pickable = false;
        loadImage("object/null.png");
    }
}