package main;

import entity.Entity;
import tile.TileManager;

public class CollisionChecker {
    GamePanel gp;
    TileManager tileM;

    public CollisionChecker(GamePanel gp, TileManager tileM) {
        this.gp = gp;
        this.tileM = tileM;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.collisionArea.x;
        int entityRightWorldX = entity.worldX + entity.collisionArea.x + entity.collisionArea.width;
        int entityTopWorldY = entity.worldY + entity.collisionArea.y;
        int entityBottomWorldY = entity.worldY + entity.collisionArea.y + entity.collisionArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = this.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = this.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (this.tileM.tile[tileNum1].collision == true ||
                    this.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = this.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = this.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (this.tileM.tile[tileNum1].collision == true ||
                    this.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = this.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = this.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (this.tileM.tile[tileNum1].collision == true ||
                    this.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = this.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = this.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (this.tileM.tile[tileNum1].collision == true ||
                    this.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
        }
    }
}
