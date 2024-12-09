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

    OBJ_Door linkedDoor;
    int transferX;
    int transferY;
    int transnferMap;

    public BufferedImage[] doorSprites;
    public int spriteIndex = 0;

    Timer doorOpenedTimer;
    Timer doorClosedTimer;
    int timerIndex = 0;

    // constructors
    public OBJ_Door(String actionCode, GamePanel gp, String doorTileName, int doorTileColums) {
        this.gp = gp;
        loadTimers();
        this.name = "door";
        this.actionCode = actionCode;
        this.collision = false;
        this.pickable = false;
        loadImage("object/null.png");
        String filePath = "object/" + doorTileName + ".png";
        loadSprites(filePath, 1);
    }

    public OBJ_Door(String actionCode, GamePanel gp, OBJ_Door link) {
        this.gp = gp;
        this.linkedDoor = link;
        loadTimers();
        this.name = "door";
        this.actionCode = actionCode;
        this.collision = false;
        this.pickable = false;
        loadImage("object/null.png");
    }

    public void setTransferCoordinates(int indexMap, int x, int y) {
        this.transferX = x;
        this.transferY = y;
        this.transnferMap = indexMap;
    }

    private void loadTimers() {
        this.doorOpenedTimer = new Timer(100, e -> {
            if (timerIndex < 3) {
                spriteIndex++;
            } else if (timerIndex < 4) {
                gp.player.movingDisabled = false;
                gp.userInterface.startAnimation();
            } else if (timerIndex == 10) {
                gp.currentMap = this.transnferMap;
                gp.player.setEntityWorldPosition(transferX, transferY);
                this.doorOpenedTimer.stop();
            }
            this.timerIndex++;
        });

        this.doorClosedTimer = new Timer(100, e -> {
            if (timerIndex == 0) {
                gp.userInterface.startAnimation();
            }else if (timerIndex == 7) {
                gp.currentMap = this.transnferMap;
                gp.player.setEntityWorldPosition(transferX, transferY);
            } else if(timerIndex == 10) {
                gp.player.movingDisabled = false;
            } else if(timerIndex > 10 && timerIndex < 14) {
                linkedDoor.spriteIndex--;
            } else if(timerIndex == 14){
                this.doorClosedTimer.stop();
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

    public void openAnimation() {
        gp.player.movingDisabled = true;
        timerIndex = 0;
        doorOpenedTimer.start();
    }

    public void closeAnimation() {
        gp.player.movingDisabled = true;
        timerIndex = 0;
        doorClosedTimer.start();
    }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
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