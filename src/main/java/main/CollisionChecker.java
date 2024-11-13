package main;

import java.util.ArrayList;

import entity.Entity;
import tile.TileManager;

public class CollisionChecker {
    GamePanel target;
    TileManager tileM;

    public CollisionChecker(GamePanel gp, TileManager tileM) {
        this.target = gp;
        this.tileM = tileM;
    }

    public CollisionChecker(GamePanel gp) {
        this.target = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.collisionArea.x;
        int entityRightWorldX = entity.worldX + entity.collisionArea.x + entity.collisionArea.width;
        int entityTopWorldY = entity.worldY + entity.collisionArea.y;
        int entityBottomWorldY = entity.worldY + entity.collisionArea.y + entity.collisionArea.height;

        int entityLeftCol = entityLeftWorldX / target.tileSize;
        int entityRightCol = entityRightWorldX / target.tileSize;
        int entityTopRow = entityTopWorldY / target.tileSize;
        int entityBottomRow = entityBottomWorldY / target.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / target.tileSize;
                tileNum1 = this.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = this.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (this.tileM.tile[tileNum1].collision == true ||
                        this.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / target.tileSize;
                tileNum1 = this.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = this.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (this.tileM.tile[tileNum1].collision == true ||
                        this.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / target.tileSize;
                tileNum1 = this.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = this.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (this.tileM.tile[tileNum1].collision == true ||
                        this.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / target.tileSize;
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
        int index = 999; // null obj
        for (int i = 0; i < target.obj.size(); i++) {
            if (target.obj.get(i) != null) {
                // entity solid area position
                entity.collisionArea.x = entity.worldX + entity.collisionArea.x;
                entity.collisionArea.y = entity.worldY + entity.collisionArea.y;
                // obj solid area position
                target.obj.get(i).collisionArea.x = target.obj.get(i).worldX + target.obj.get(i).collisionArea.x;
                target.obj.get(i).collisionArea.y = target.obj.get(i).worldY + target.obj.get(i).collisionArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.collisionArea.y -= entity.speed;
                        if (entity.collisionArea.intersects(target.obj.get(i).collisionArea)) {
                            if (target.obj.get(i).collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player == true) { // return index to pick up obj, only player can do that
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.collisionArea.y += entity.speed;
                        if (entity.collisionArea.intersects(target.obj.get(i).collisionArea)) {
                            if (entity.collisionArea.intersects(target.obj.get(i).collisionArea)) {
                                if (target.obj.get(i).collision == true) {
                                    entity.collisionOn = true;
                                }
                                if (player == true) { // return index to pick up obj, only player can do that
                                    index = i;
                                }
                            }
                        }
                        break;
                    case "left":
                        entity.collisionArea.x -= entity.speed;
                        if (entity.collisionArea.intersects(target.obj.get(i).collisionArea)) {
                            if (entity.collisionArea.intersects(target.obj.get(i).collisionArea)) {
                                if (target.obj.get(i).collision == true) {
                                    entity.collisionOn = true;
                                }
                                if (player == true) { // return index to pick up obj, only player can do that
                                    index = i;
                                }
                            }
                        }
                        break;
                    case "right":
                        entity.collisionArea.x += entity.speed;
                        if (entity.collisionArea.intersects(target.obj.get(i).collisionArea)) {
                            if (entity.collisionArea.intersects(target.obj.get(i).collisionArea)) {
                                if (target.obj.get(i).collision == true) {
                                    entity.collisionOn = true;
                                }
                                if (player == true) { // return index to pick up obj, only player can do that
                                    index = i;
                                }
                            }
                        }
                        break;
                }
                // resets rectangle areas and positions
                entity.collisionArea.x = entity.solidAreaDefaultX;
                entity.collisionArea.y = entity.solidAreaDefaultY;
                target.obj.get(i).collisionArea.x = target.obj.get(i).solidAreaDefaultX;
                target.obj.get(i).collisionArea.y = target.obj.get(i).solidAreaDefaultY;
            }
        }
        return index;
    }

    public boolean checkInBush(Entity entity) {
        // Calcola le coordinate del rettangolo di collisione dell'entità nel mondo
        int entityLeftX = entity.worldX + entity.collisionArea.x;
        int entityRightX = entityLeftX + entity.collisionArea.width;
        int entityTopY = entity.worldY + entity.collisionArea.y;
        int entityBottomY = entityTopY + entity.collisionArea.height;

        // Trova la posizione del tile in cui si trova il centro dell'entità
        int entityCenterX = entity.worldX + entity.collisionArea.x + entity.collisionArea.width / 2;
        int entityCenterY = entity.worldY + entity.collisionArea.y + entity.collisionArea.height / 2;
        int entityTileCol = entityCenterX / target.tileSize;
        int entityTileRow = entityCenterY / target.tileSize;

        int tileNum;

        switch (entity.direction) {
            case "up":
                entityTileRow = (entityCenterY - entity.speed) / target.tileSize;
                break;
            case "down":
                entityTileRow = (entityCenterY + entity.speed) / target.tileSize;
                break;
            case "left":
                entityTileCol = (entityCenterX - entity.speed) / target.tileSize;
                break;
            case "right":
                entityTileCol = (entityCenterX + entity.speed) / target.tileSize;
                break;
        }

        // Prendi il numero del tile
        tileNum = this.tileM.mapTileNum[entityTileCol][entityTileRow];

        // Verifica se il tile è un bush
        if (tileNum >= 260 && tileNum <= 275 || tileNum >= 343 && tileNum <= 362) {
            // Calcola il centro del tile
            int tileCenterX = (entityTileCol * target.tileSize) + target.tileSize / 2;
            int tileCenterY = (entityTileRow * target.tileSize) + target.tileSize / 2;

            // Definisci una hitbox centrale all'interno del tile (esempio: 5x5 pixel)
            int bushHitboxSize = 36; // minimum size done by hand.
            int halfBushHitbox = bushHitboxSize / 2;

            int bushHitboxLeftX = tileCenterX - halfBushHitbox;
            int bushHitboxRightX = tileCenterX + halfBushHitbox;
            int bushHitboxTopY = tileCenterY - halfBushHitbox;
            int bushHitboxBottomY = tileCenterY + halfBushHitbox;

            // Verifica se il rettangolo di collisione dell'entità interseca la hitbox
            // centrale del bush
            boolean collision = entityRightX >= bushHitboxLeftX &&
                    entityLeftX <= bushHitboxRightX &&
                    entityBottomY >= bushHitboxTopY &&
                    entityTopY <= bushHitboxBottomY;

            if (collision) {
                return true;
            }
        }

        return false;
    }

    // npc / pokemons collision
    public int checkEntity(Entity entity, ArrayList<Entity> target) {
        int index = 999; // null obj
        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                // entity solid area position
                entity.collisionArea.x = entity.worldX + entity.collisionArea.x;
                entity.collisionArea.y = entity.worldY + entity.collisionArea.y;
                // obj solid area position
                target.get(i).collisionArea.x = target.get(i).worldX + target.get(i).collisionArea.x;
                target.get(i).collisionArea.y = target.get(i).worldY + target.get(i).collisionArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.collisionArea.y -= entity.speed;
                        if (entity.collisionArea.intersects(target.get(i).collisionArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "down":
                        entity.collisionArea.y += entity.speed;
                        if (entity.collisionArea.intersects(target.get(i).collisionArea)) {
                            if (entity.collisionArea.intersects(target.get(i).collisionArea)) {
                                entity.collisionOn = true;
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.collisionArea.x -= entity.speed;
                        if (entity.collisionArea.intersects(target.get(i).collisionArea)) {
                            if (entity.collisionArea.intersects(target.get(i).collisionArea)) {
                                entity.collisionOn = true;
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.collisionArea.x += entity.speed;
                        if (entity.collisionArea.intersects(target.get(i).collisionArea)) {
                            if (entity.collisionArea.intersects(target.get(i).collisionArea)) {
                                entity.collisionOn = true;
                                index = i;
                            }
                        }
                        break;
                }
                // resets rectangle areas and positions
                entity.collisionArea.x = entity.solidAreaDefaultX;
                entity.collisionArea.y = entity.solidAreaDefaultY;
                target.get(i).collisionArea.x = target.get(i).solidAreaDefaultX;
                target.get(i).collisionArea.y = target.get(i).solidAreaDefaultY;
            }
        }
        return index;
    }
}
