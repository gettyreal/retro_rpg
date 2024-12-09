package main;

import tile.Point;
import java.util.ArrayList;
import entity.Entity;
import tile.TileManager;

//collision checker class
//cheks all tipe of collisions
// 1 in game panel for npc pokemon and obj
// 1 in each layer for tile collisions
public class CollisionChecker {
    GamePanel gp; //gamePanel
    TileManager tileM; //tile manager for single lauer.

    //constructor for single layer
    public CollisionChecker(GamePanel gp, TileManager tileM) {
        this.gp = gp;
        this.tileM = tileM;
    }

    //general constructor for entities
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    //method to check if player is colliding with a collision = true tile
    //sets the collision variable of the entity to true if collision is happening
    //takes as parameter the entity which to check collision
    public void checkTile(Entity entity) {
        //takes entity hitbox corners coordinates
        int entityLeftWorldX = entity.worldX + entity.collisionArea.x;
        int entityRightWorldX = entity.worldX + entity.collisionArea.x + entity.collisionArea.width;
        int entityTopWorldY = entity.worldY + entity.collisionArea.y;
        int entityBottomWorldY = entity.worldY + entity.collisionArea.y + entity.collisionArea.height;

        //gets entiy adiacent coordinates
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        //2 tiles for checking collisions
        // to be redone ad tile base movement only allows grid movement
        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                if (checkDefaultSkipConditions(entityLeftCol, entityTopRow, entityRightCol, entityTopRow)) {
                    break;
                }
                tileNum1 = this.tileM.layerMap.get(new Point(entityLeftCol, entityTopRow));
                tileNum2 = this.tileM.layerMap.get(new Point(entityRightCol, entityTopRow));

                if (this.tileM.tileSet.get(tileNum1).collision == true ||
                        this.tileM.tileSet.get(tileNum2).collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                if (checkDefaultSkipConditions(entityLeftCol, entityBottomRow, entityRightCol, entityBottomRow)) {
                    break;
                }
                tileNum1 = this.tileM.layerMap.get(new Point(entityLeftCol, entityBottomRow));
                tileNum2 = this.tileM.layerMap.get(new Point(entityRightCol, entityBottomRow));
                if (this.tileM.tileSet.get(tileNum1).collision == true ||
                        this.tileM.tileSet.get(tileNum2).collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                if (checkDefaultSkipConditions(entityLeftCol, entityTopRow, entityLeftCol, entityBottomRow)) {
                    break;
                }
                tileNum1 = this.tileM.layerMap.get(new Point(entityLeftCol, entityTopRow));
                tileNum2 = this.tileM.layerMap.get(new Point(entityLeftCol, entityBottomRow));
                if (this.tileM.tileSet.get(tileNum1).collision == true ||
                        this.tileM.tileSet.get(tileNum2).collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                if (checkDefaultSkipConditions(entityRightCol, entityTopRow, entityRightCol, entityBottomRow)) {
                    break;
                }
                tileNum1 = this.tileM.layerMap.get(new Point(entityRightCol, entityTopRow));
                tileNum2 = this.tileM.layerMap.get(new Point(entityRightCol, entityBottomRow));
                if (this.tileM.tileSet.get(tileNum1).collision == true ||
                        this.tileM.tileSet.get(tileNum2).collision == true) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    private boolean checkDefaultSkipConditions(int x1, int y1, int x2, int y2) {
        if (UtilityTool.checkNullTile(this.tileM.layerMap, x1, y1) ||
                UtilityTool.checkNullTile(this.tileM.layerMap, x2, y2)) {
            return true;
        }
        return false;
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999; // null obj
        for (int i = 0; i < gp.mapM.maps.get(gp.currentMap).obj.size(); i++) {
            if (gp.mapM.maps.get(gp.currentMap).obj.get(i) != null) {
                // entity solid area position
                entity.collisionArea.x = entity.worldX + entity.collisionArea.x;
                entity.collisionArea.y = entity.worldY + entity.collisionArea.y;
                // obj solid area position
                gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea.x = gp.mapM.maps.get(gp.currentMap).obj
                        .get(i).worldX + gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea.x;
                gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea.y = gp.mapM.maps.get(gp.currentMap).obj
                        .get(i).worldY + gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.collisionArea.y -= entity.speed;
                        if (entity.collisionArea.intersects(gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea)) {
                            if (gp.mapM.maps.get(gp.currentMap).obj.get(i).collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player == true) { // return index to pick up obj, only player can do that
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.collisionArea.y += entity.speed;
                        if (entity.collisionArea.intersects(gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea)) {
                            if (entity.collisionArea
                                    .intersects(gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea)) {
                                if (gp.mapM.maps.get(gp.currentMap).obj.get(i).collision == true) {
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
                        if (entity.collisionArea.intersects(gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea)) {
                            if (entity.collisionArea
                                    .intersects(gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea)) {
                                if (gp.mapM.maps.get(gp.currentMap).obj.get(i).collision == true) {
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
                        if (entity.collisionArea.intersects(gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea)) {
                            if (entity.collisionArea
                                    .intersects(gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea)) {
                                if (gp.mapM.maps.get(gp.currentMap).obj.get(i).collision == true) {
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
                gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea.x = gp.mapM.maps.get(gp.currentMap).obj
                        .get(i).solidAreaDefaultX;
                gp.mapM.maps.get(gp.currentMap).obj.get(i).collisionArea.y = gp.mapM.maps.get(gp.currentMap).obj
                        .get(i).solidAreaDefaultY;
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
        Point p = new Point(entityTileCol, entityTileRow);
        tileNum = this.tileM.layerMap.getOrDefault(p, -1);

        // Verifica se il tile è un bush
        if (tileNum == 6) {
            // Calcola il centro del tile
            int tileCenterX = (entityTileCol * gp.tileSize) + gp.tileSize / 2;
            int tileCenterY = (entityTileRow * gp.tileSize) + gp.tileSize / 2;

            // Definisci una hitbox centrale all'interno del tile (1x1 pixel)
            int bushHitboxLeftX = tileCenterX;
            int bushHitboxRightX = tileCenterX;
            int bushHitboxTopY = tileCenterY;
            int bushHitboxBottomY = tileCenterY;

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
