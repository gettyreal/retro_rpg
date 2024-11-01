package entity;

import main.GamePanel;
import main.KeyHandler;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues(); // set spawn coordinates
        getPlayerImage();
    }

    public void setDefaultValues() {
        this.x = 100;
        this.y = 100;
        this.speed = 2;
        this.direction = "up";
    }

    public void getPlayerImage() {
        loadFiles();
    }

    private void loadFiles() {
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
                } else if(i < 8) {
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
                this.y -= this.speed;
            } else if (keyH.downPressed == true) {
                this.direction = "down";
                this.y += this.speed;
            } else if (keyH.leftPressed == true) {
                this.direction = "left";
                this.x -= this.speed;
            } else if (keyH.rightPressed == true) {
                this.direction = "right";
                this.x += this.speed;
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
    }

    public void draw(Graphics2D g2) {
        BufferedImage playerImage = null;
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
        g2.drawImage(playerImage, this.x, this.y, playerImage.getWidth()*2, playerImage.getHeight()*2, null);
    }
}
