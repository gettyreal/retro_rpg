package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

//super class per player mostri npc entita' ecc.

public abstract class Entity {
    GamePanel gp;

    public int worldX, worldY; // entity spawn coordinates
    public int speed; // entity speed

    // movement animation
    public BufferedImage[] up = new BufferedImage[4];
    public BufferedImage[] down = new BufferedImage[4];
    public BufferedImage[] left = new BufferedImage[4];
    public BufferedImage[] right = new BufferedImage[4];

    public BufferedImage[] bushUp = new BufferedImage[4];
    public BufferedImage[] bushDown = new BufferedImage[4];
    public BufferedImage[] bushLeft = new BufferedImage[4];
    public BufferedImage[] bushRight = new BufferedImage[4];

    public String direction; // direction of the player

    public int spriteCounter = 0; // index of sprite active on screen
    public int spriteNumber = 0;

    // collisions
    public Rectangle collisionArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean bushIn = false;

    // for update interval
    public int updateLockCounter = 0;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    // gets the entity image in the resources package
    // receive the package path intended as package + filename equal for all
    // imagines
    public void getEntityImage(String packagePath) {
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

    public void getEntityBushImage(String packagePath) {
        int indexFile = 0;
        int indexArray = 0;
        for (int i = 0; i < 16; i++) {
            if (indexArray == 4) {
                indexArray = 0;
            }
            // loads all player imagines
            String bushName = packagePath + indexFile + ".png";
            if (i < 4) {
                loadImage(this.bushDown, indexArray, bushName);
            } else if (i < 8) {
                loadImage(this.bushUp, indexArray, bushName);
            } else if (i < 12) {
                loadImage(this.bushLeft, indexArray, bushName);
            } else {
                loadImage(this.bushRight, indexArray, bushName);
            }
            indexFile++;
            indexArray++;
        }
    }

    public void loadImage(BufferedImage[] array, int indexArray, String fileName) {
        try {
            array[indexArray] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
            array[indexArray] = UtilityTool.scaleImage(array[indexArray],
                    array[indexArray].getWidth() * 2, array[indexArray].getHeight() * 2); // scale img
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // sets default behavior of entity class
    // each single entity behaves differently so nothing is inside
    public void setAction() {
    }

    // updates the entity
    // set default update cicle of entiy
    // much the same for all entities exept for player
    public void update() {
        setAction();

        // checks entity collision
        collisionOn = false;
        gp.tileM1.cChecker.checkTile(this);
        gp.tileM2.cChecker.checkTile(this);
        gp.tileM3.cChecker.checkTile(this);
        gp.tileM4.cChecker.checkTile(this);

        // move the entity
        if (collisionOn == false) {
            switch (direction) {
                case "up":
                    this.worldY -= this.speed;
                    break;
                case "down":
                    this.worldY += this.speed;
                    break;
                case "left":
                    this.worldX -= this.speed;
                    break;
                case "right":
                    this.worldX += this.speed;
                    break;
            }
        }

        // updates the sprite of the entity
        this.spriteCounter++;
        if (spriteCounter > 10) { // ogni 10 update cambia sprite
            if (spriteNumber == 3) { // se le sprite sono finite l'animazione riparte
                spriteNumber = 0;
            } else {
                spriteNumber++; // aumenta la sprite
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage Image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (this.direction) {
                case "up":
                    Image = this.up[spriteNumber];
                    break;
                case "down":
                    Image = this.down[spriteNumber];
                    break;
                case "left":
                    Image = this.left[spriteNumber];
                    break;
                case "right":
                    Image = this.right[spriteNumber];
                    break;
            }

            g2.drawImage(Image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
