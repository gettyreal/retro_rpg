package main;

import java.util.ArrayList;

import entity.Entity;
import tile.TileManager;

public class CollisionChecker {
    GamePanel gp;
    TileManager tileM;

    public CollisionChecker(GamePanel gp, TileManager tileM) {
        this.gp = gp;
        this.tileM = tileM;
    }

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
        int index = 999; // null obj
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
                            if (player == true) { // return index to pick up obj, only player can do that
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
                                if (player == true) { // return index to pick up obj, only player can do that
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
                                if (player == true) { // return index to pick up obj, only player can do that
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
                gp.obj.get(i).collisionArea.x = gp.obj.get(i).solidAreaDefaultX;
                gp.obj.get(i).collisionArea.y = gp.obj.get(i).solidAreaDefaultY;
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
        int entityTileCol = entityCenterX / gp.tileSize;
        int entityTileRow = entityCenterY / gp.tileSize;

        int tileNum;

        switch (entity.direction) {
            case "up":
                entityTileRow = (entityCenterY - entity.speed) / gp.tileSize;
                break;
            case "down":
                entityTileRow = (entityCenterY + entity.speed) / gp.tileSize;
                break;
            case "left":
                entityTileCol = (entityCenterX - entity.speed) / gp.tileSize;
                break;
            case "right":
                entityTileCol = (entityCenterX + entity.speed) / gp.tileSize;
                break;
        }

        // Prendi il numero del tile
        tileNum = this.tileM.mapTileNum[entityTileCol][entityTileRow];

        // Verifica se il tile è un bush
        if (tileNum >= 260 && tileNum <= 275 || tileNum >= 343 && tileNum <= 362) {
            // Calcola il centro del tile
            int tileCenterX = (entityTileCol * gp.tileSize) + gp.tileSize / 2;
            int tileCenterY = (entityTileRow * gp.tileSize) + gp.tileSize / 2;

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

    public void checkPlayer(Entity entity) {
        // entity solid area position
        entity.collisionArea.x = entity.worldX + entity.collisionArea.x;
        entity.collisionArea.y = entity.worldY + entity.collisionArea.y;
        // obj solid area position
        gp.player.collisionArea.x = gp.player.worldX + gp.player.collisionArea.x;
        gp.player.collisionArea.y = gp.player.worldY + gp.player.collisionArea.y;

        switch (entity.direction) {
            case "up":
                entity.collisionArea.y -= entity.speed;
                if (entity.collisionArea.intersects(gp.player.collisionArea)) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entity.collisionArea.y += entity.speed;
                if (entity.collisionArea.intersects(gp.player.collisionArea)) {
                    if (entity.collisionArea.intersects(gp.player.collisionArea)) {
                        entity.collisionOn = true;
                    }
                }
                break;
            case "left":
                entity.collisionArea.x -= entity.speed;
                if (entity.collisionArea.intersects(gp.player.collisionArea)) {
                    if (entity.collisionArea.intersects(gp.player.collisionArea)) {
                        entity.collisionOn = true;
                    }
                }
                break;
            case "right":
                entity.collisionArea.x += entity.speed;
                if (entity.collisionArea.intersects(gp.player.collisionArea)) {
                    if (entity.collisionArea.intersects(gp.player.collisionArea)) {
                        entity.collisionOn = true;
                    }
                }
                break;
        }
        // resets rectangle areas and positions
        entity.collisionArea.x = entity.solidAreaDefaultX;
        entity.collisionArea.y = entity.solidAreaDefaultY;
        gp.player.collisionArea.x = gp.player.solidAreaDefaultX;
        gp.player.collisionArea.y = gp.player.solidAreaDefaultY;
    }
}
