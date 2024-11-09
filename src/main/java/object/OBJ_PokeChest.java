package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_PokeChest extends SuperObject{
    public OBJ_PokeChest() {
        this.name = "pokeChest";
        this.collision = true;
        loadImage("object/poke-chest.png");
    }

    private void loadImage(String filename) {
        try {
            this.image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
