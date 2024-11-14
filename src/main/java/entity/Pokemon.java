package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Pokemon extends Entity {
    public Pokemon(GamePanel gp, String name, String packageName, int Xoffset, int Yoffset) {
        // calls entity constructor
        super(gp);
        // sets default values
        this.name = name;
        this.direction = "up";
        this.speed = 1;
        // gets player image
        getEntityImage(packageName);

        // gets collision area parameters
        this.Xoffset = Xoffset;
        this.Yoffset = Yoffset;
        solidAreaDefaultX = collisionArea.x;
        solidAreaDefaultY = collisionArea.y;
    }

    @Override
    public void loadImage(BufferedImage[] array, int indexArray, String fileName) {
        try {
            // Carica l'immagine
            BufferedImage originalImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));

            // Scala l'immagine
            BufferedImage scaledImage = UtilityTool.scaleImage(originalImage,
                    originalImage.getWidth() * 3, originalImage.getHeight() * 3);

            // Imposta l'immagine scalata nell'array
            array[indexArray] = scaledImage;
            
            collisionArea = new Rectangle(Xoffset, Yoffset, scaledImage.getWidth(), scaledImage.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
