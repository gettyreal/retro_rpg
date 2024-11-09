package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_PokeBall extends SuperObject {
    public OBJ_PokeBall() {
        this.name = "pokeBall";
        this.collision = false;
        this.pickable = true;
        loadImage("object/pokeball.png");
    }

    private void loadImage(String filename) {
        try {
            this.image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
