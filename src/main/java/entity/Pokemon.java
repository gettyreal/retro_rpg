package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Pokemon extends Entity {
    public Pokemon(GamePanel gp, String name, String packageName, String BattleImagePath, int Xoffset, int Yoffset) {
        // calls entity constructor
        super(gp);
        // sets default values
        this.name = name;
        this.direction = "up";
        this.speed = 1;
        // gets pokemon imagines
        getEntityImage(packageName);
        setEntityBattleImage(BattleImagePath);

        // gets collision area parameters
        this.Xoffset = Xoffset;
        this.Yoffset = Yoffset;
        solidAreaDefaultX = collisionArea.x;
        solidAreaDefaultY = collisionArea.y;
    }

    public void setEntityBattleImage(String fileName) {
        fileName = "pokemon/" + fileName;
        try {
            this.battleImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
            this.battleImage = UtilityTool.scaleImage(battleImage, battleImage.getWidth() * 3, battleImage.getHeight() * 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getEntityImage(String packagePath) {
        packagePath = "pokemon/" + packagePath;
        int indexFile = 0;
        int indexArray = 0;
        for (int i = 0; i < 16; i++) {
            if (indexArray == 4) {
                indexArray = 0;
            }
            // loads all player imagines
            String fileName = packagePath + indexFile + ".png";
            if (i < 4) {
                loadImage(this.down, indexArray, fileName);
            } else if (i < 8) {
                loadImage(this.up, indexArray, fileName);
            } else if (i < 12) {
                loadImage(this.left, indexArray, fileName);
            } else {
                loadImage(this.right, indexArray, fileName);
            }
            indexFile++;
            indexArray++;
        }
    }

    @Override
    public void loadImage(Sprite[] array, int indexArray, String fileName) {
        try {
            // Carica l'immagine
            BufferedImage originalImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));

            // Scala l'immagine
            BufferedImage scaledImage = UtilityTool.scaleImage(originalImage,
                    originalImage.getWidth() * 3, originalImage.getHeight() * 3);

            // Imposta l'immagine scalata nell'array
            array[indexArray].body = scaledImage;
            
            collisionArea = new Rectangle(Xoffset, Yoffset, scaledImage.getWidth(), scaledImage.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
