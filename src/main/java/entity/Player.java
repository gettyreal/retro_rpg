package entity;

import main.GamePanel;
import main.KeyHandler;

import object.OBJ_PokeBall;
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

    int objIndex = 999; //set obj null value.

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
            String fileName = "player/player_" + indexFile + ".png";
            try {
                if (i < 4) {
                    this.down[indexArray] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
                } else if (i < 8) {
                    this.up[indexArray] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
                } else if (i < 12) {
                    this.left[indexArray] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
                } else {
                    this.right[indexArray] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
                }
                indexFile++;
                indexArray++;
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        pickupObject(objIndex); //removes the obj on e key press if possible
    }

    public void pickupObject(int index) {
        if (index != 999) { // index == 999 null object.
            if (gp.obj.get(index).pickable == true) { // check uf obj is pickable or not
                if (keyH.Epressed == true) {
                    gp.aSetter.removeObject(index);
                    objIndex--; //prevents index out bounds exeption
                    keyH.Epressed = false; //resets the key to prevent all obj to be picked up
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage playerImage = null;

        //gets player srites
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
        //draws current player sprite
        g2.drawImage(playerImage, screenX, screenY, playerImage.getWidth() * 2, playerImage.getHeight() * 2, null);
        drawPickupMessage(g2);
    }

    public void drawPickupMessage(Graphics2D g2) {
        int objIndex = gp.tileM1.cChecker.checkObject(this, true);
        //checks if the index of obj passed is the null obj
        if (gp.OBJChecker.checkObject(this, true) == 999) { //checks if player is colliding with obj
            return;
        }

        if (gp.obj.get(objIndex) instanceof OBJ_PokeBall) {
            gp.userInterface.drawText(g2, "press E to pick up PokeBall", gp.screenWidth / 2, gp.screenHeight / 2 + (4 * gp.tileSize));
        }
        if (gp.obj.get(objIndex) instanceof OBJ_PokeChest) {
            gp.userInterface.drawText(g2, "press E to open PokeChest", gp.screenWidth / 2, gp.screenHeight / 2 + (4 * gp.tileSize));
        }
    }
}
