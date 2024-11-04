package main;

import entity.Entity;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
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
                tileNum1 = gp.tileM.mapStructNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapStructNum[entityRightCol][entityTopRow];
                if (gp.tileM.structures[tileNum1].collision == true ||
                        gp.tileM.structures[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapStructNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapStructNum[entityRightCol][entityBottomRow];
                if (gp.tileM.structures[tileNum1].collision == true ||
                        gp.tileM.structures[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapStructNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapStructNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.structures[tileNum1].collision == true ||
                        gp.tileM.structures[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapStructNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapStructNum[entityRightCol][entityBottomRow];
                if (gp.tileM.structures[tileNum1].collision == true ||
                        gp.tileM.structures[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
        }
    }
}
