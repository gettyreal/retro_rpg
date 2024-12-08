package object;

public class OBJ_Stairs extends SuperObject{
    public OBJ_Stairs(String actionCode) {
        this.name = "stairs";
        this.actionCode = actionCode;
        this.collision = false;
        this.pickable = false;
        loadImage("object/null.png");
    }
}
