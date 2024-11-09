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

    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        for (int i = 0; i < gp.obj.size(); i++) {
            if (gp.obj.get(i) != null) {
                // entity solid area position
                entity.collisionArea.x = entity.worldX + entity.collisionArea.x;
                entity.collisionArea.y = entity.worldY + entity.collisionArea.y;
                // obj solid area position
                gp.obj.get(i).collisionArea.x = gp.obj.get(i).worldX + gp.obj.get(i).collisionArea.x;
                gp.obj.get(i).collisionArea.y = gp.obj.get(i).worldY + gp.obj.get(i).collisionArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.collisionArea.y -= entity.speed;
                        if (entity.collisionArea.intersects(gp.obj.get(i).collisionArea)) {
                            if (gp.obj.get(i).collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player == true) { //return index to pick up obj, only player can do that
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.collisionArea.y += entity.speed;
                        if (entity.collisionArea.intersects(gp.obj.get(i).collisionArea)) {
                            if (entity.collisionArea.intersects(gp.obj.get(i).collisionArea)) {
                                if (gp.obj.get(i).collision == true) {
                                    entity.collisionOn = true;
                                }
                                if (player == true) { //return index to pick up obj, only player can do that
                                    index = i;
                                }
                            }
                        }
                        break;
                    case "left":
                        entity.collisionArea.x -= entity.speed;
                        if (entity.collisionArea.intersects(gp.obj.get(i).collisionArea)) {
                            if (entity.collisionArea.intersects(gp.obj.get(i).collisionArea)) {
                                if (gp.obj.get(i).collision == true) {
                                    entity.collisionOn = true;
                                }
                                if (player == true) { //return index to pick up obj, only player can do that
                                    index = i;
                                }
                            }
                        }
                        break;
                    case "right":
                        entity.collisionArea.x += entity.speed;
                        if (entity.collisionArea.intersects(gp.obj.get(i).collisionArea)) {
                            if (entity.collisionArea.intersects(gp.obj.get(i).collisionArea)) {
                                if (gp.obj.get(i).collision == true) {
                                    entity.collisionOn = true;
                                }
                                if (player == true) { //return index to pick up obj, only player can do that
                                    index = i;
                                }
                            }
                        }
                        break;
                }
                //resets rectangle areas and positions
                entity.collisionArea.x = entity.solidAreaDefaultX;
                entity.collisionArea.y = entity.solidAreaDefaultY;
                gp.obj.get(i).collisionArea.x = gp.obj.get(i).solidAreaDefaultX;
                gp.obj.get(i).collisionArea.y = gp.obj.get(i).solidAreaDefaultY;
            }
        }
        return index;
    }
}
