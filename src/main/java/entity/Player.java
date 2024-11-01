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
        int indexRow = 0;
        for (int i = 0; i < 4; i++) {
            loadRow(indexRow, i);
            indexRow += 64;
            System.out.println();
        }
    }

    private void loadRow(int indexRow, int index) {
        for (int i = 0; i < 8; i++) {
            String filename = "player/tile_" + indexRow + "_" + ((i + 1) * 64) + ".png";
            try {
                switch (index) {
                    case 0:
                        this.up[i] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));
                        break;
                    case 1:
                        this.left[i] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));
                        break;
                    case 2:
                        this.down[i] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));
                        break;
                    case 3:
                        this.right[i] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));
                        break;
                }
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
            if (spriteCounter > 7) { // ogni 10 update cambia sprite
                if (spriteNumber == 7) { // se le sprite sono finite l'animazione riparte
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
        g2.drawImage(playerImage, this.x, this.y, playerImage.getWidth(), playerImage.getHeight(), null);
    }
}
