package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import main.GamePanel;
import main.UtilityTool;

//object door
//used to get door interactions
public class OBJ_Door extends SuperObject { // door wont be visible because the image is visible on tilemaps.
    GamePanel gp;

    public BufferedImage[] doorSprites;
    public int spriteIndex = 0;

    Timer spriteTimer;
    int timerIndex = 0;

    // constructor
    // get as paramentet the action code for different behaviors
    public OBJ_Door(String actionCode, GamePanel gp) {
        this.gp = gp;
        loadTimers();
        this.name = "door";
        this.actionCode = actionCode;
        this.collision = false;
        this.pickable = false;
        loadImage("object/null.png");
        loadSprites("object/door1.png", 1);
    }

    private void loadTimers() {
        this.spriteTimer = new Timer(100, e -> {
            if (timerIndex < 3) {
                spriteIndex++;
            } else if (timerIndex < 4) {
                gp.player.movingDisabled = false;
                gp.userInterface.startAnimation();
            } else if (timerIndex == 10) {
                gp.currentMap = 2;
                gp.player.setEntityWorldPosition(4, 9);
                this.spriteTimer.stop();
                spriteIndex = 0;
            }
            this.timerIndex++;
        });
    }

    public void loadSprites(String fileName, int column) {
        this.doorSprites = new BufferedImage[4];
        try {
            // Load the entire image tileset
            BufferedImage tilesetImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));

            // Tile dimensions
            int tileWidth = 32;
            int tileHeight = 32;
            // max tileset dimension
            int totalRow = tilesetImage.getHeight() / tileHeight;

            // Extract tiles and save them
            for (int row = 0; row < totalRow; row++) {
                int tileX = column * tileWidth;
                int tileY = row * tileHeight;

                doorSprites[row] = tilesetImage.getSubimage(tileX, tileY, tileWidth, tileHeight);
                doorSprites[row] = UtilityTool.scaleImage(doorSprites[row], 64, 64);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doorOpenedAnimation() {
        gp.player.movingDisabled = true;
        timerIndex = 0;
        spriteTimer.start();
    }

    public void doorClosedAnimation() {
        timerIndex = 0;
        gp.currentMap = 0;
        gp.player.setEntityWorldPosition(19, 48);
    }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        System.out.println(this.timerIndex);
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            g2.drawImage(this.image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            if (this.actionCode.contains("to")) {
                g2.drawImage(this.doorSprites[spriteIndex], screenX, screenY - gp.tileSize, gp.tileSize, gp.tileSize,null);
            }
        }
    }
}