package object;

public class OBJ_Door extends SuperObject{ //door wont be visible because the image is visible on tilemaps.
    public OBJ_Door() {
        this.name = "door"; 
        this.collision = true;
        loadImage("object/door.png");
    }
}