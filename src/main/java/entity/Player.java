package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_PokeChest;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    int objIndex = 999; // set obj null value.

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues(); // set spawn coordinates
        getPlayerImage();

        this.collisionArea = new Rectangle(7, 31, 20, 15);
        solidAreaDefaultX = collisionArea.x;
        solidAreaDefaultY = collisionArea.y;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
    }

    public void setDefaultValues() {
        this.worldX = 21 * gp.tileSize;
        this.worldY = 21 * gp.tileSize;
        this.speed = 2;
        this.direction = "up";
    }

    public void getPlayerImage() {
        int indexFile = 0;
        int indexArray = 0;
        for (int i = 0; i < 16; i++) {
            if (indexArray == 4) {
                indexArray = 0;
            }
            // loads all player imagines
            String fileName = "player/player_" + indexFile + ".png";
            String bushName = "player/bush_" + indexFile + ".png";
            if (i < 4) {
                loadImage(this.down, indexArray, fileName);
                loadImage(this.bushDown, indexArray, bushName);
            } else if (i < 8) {
                loadImage(this.up, indexArray, fileName);
                loadImage(this.bushUp, indexArray, bushName);
            } else if (i < 12) {
                loadImage(this.left, indexArray, fileName);
                loadImage(this.bushLeft, indexArray, bushName);
            } else {
                loadImage(this.right, indexArray, fileName);
                loadImage(this.bushRight, indexArray, bushName);
            }
            indexFile++;
            indexArray++;
        }
    }

    // load single image and scales it
    private void loadImage(BufferedImage[] array, int indexArray, String fileName) {
        try {
            array[indexArray] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
            array[indexArray] = UtilityTool.scaleImage(array[indexArray],
                    array[indexArray].getWidth() * 2, array[indexArray].getHeight() * 2); // scale img
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyH.checkMovement()) {
            // move the player by playerSpeed
            if (keyH.upPressed == true) {
                this.direction = "up";
            } else if (keyH.downPressed == true) {
                this.direction = "down";
            } else if (keyH.leftPressed == true) {
                this.direction = "left";
            } else if (keyH.rightPressed == true) {
                this.direction = "right";
            }

            // check collision
            collisionOn = false;

            // check collision on all layers
            gp.tileM1.cChecker.checkTile(this);
            gp.tileM2.cChecker.checkTile(this);
            gp.tileM3.cChecker.checkTile(this);
            gp.tileM4.cChecker.checkTile(this);

            // check if player is in bush
            // debug System.out.println(gp.tileM2.cChecker.checkInBush(this));
            bushIn = gp.tileM2.cChecker.checkInBush(this); // used 2nd layer because bushes are on 2nd layer.

            // check objcets collison + gets the value of the obj colliding
            objIndex = gp.OBJChecker.checkObject(this, true);

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
        pickupObject(objIndex); // removes the obj on e key press if possible
    }

    public void pickupObject(int index) {
        if (index != 999) { // index == 999 null object.
            if (keyH.Epressed == true) {
                if (gp.obj.get(index) instanceof OBJ_PokeChest) {
                    gp.obj.get(index).loadImage("object/poke-chest-open.png"); // changes imagine after opening chest
                    OBJ_PokeChest pokeChest = (OBJ_PokeChest) gp.obj.get(index); // down cast to modify opened boolean
                    pokeChest.opened = true; // put pokechst opened on true after user input
                }

                // it needs to be the last cause objindex--;
                if (gp.obj.get(index).pickable == true) {
                    gp.aSetter.removeObject(index);
                    objIndex--; // prevents index out bounds exeption
                }

                keyH.Epressed = false; // resets the key
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage playerImage = null;

        // gets player srites
        if (!bushIn) { // if not in bush gets player normal sprites
            switch (this.direction) {
                case "up":
                    playerImage = this.up[spriteNumber];
                    break;
                case "down":
                    playerImage = this.down[spriteNumber];
                    break;
                case "left":
                    playerImage = this.left[spriteNumber];
                    break;
                case "right":
                    playerImage = this.right[spriteNumber];
                    break;
            }
        } else { // if in bush gets player bush sprites
            switch (this.direction) {
                case "up":
                    playerImage = this.bushUp[spriteNumber];
                    break;
                case "down":
                    playerImage = this.bushDown[spriteNumber];
                    break;
                case "left":
                    playerImage = this.bushLeft[spriteNumber];
                    break;
                case "right":
                    playerImage = this.bushRight[spriteNumber];
                    break;
            }
        }
        // draws current player sprite
        g2.drawImage(playerImage, screenX, screenY, null);
    }
}
