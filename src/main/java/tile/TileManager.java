package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.CollisionChecker;
import main.GamePanel;
import main.UtilityTool;

public class TileManager {
    GamePanel gp;
    public CollisionChecker cChecker;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp, String tilesetFileImg,String mapFileName, String collisionFileName) {
        this.gp = gp;
        cChecker = new CollisionChecker(gp, this);
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage(this.tile, tilesetFileImg);
        loadMap(this.mapTileNum, mapFileName);
        setTileCollision(collisionFileName);
    }

    public void getTileImage(Tile[] tilesheet, String fileName) {
        try {
            //carica intero tileset
            BufferedImage tileset = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));

            // Dimensioni di ciascuna tile
            int tileWidth = 16;
            int tileHeight = 16;
            int totalTiles = tileset.getWidth() / tileWidth; // total tiles

            tile = new Tile[totalTiles];

            //estrazione dal tileset e salvataggio
            for (int i = 0; i < totalTiles; i++) {
                int tileX = i * tileWidth;
                tile[i] = new Tile();
                tile[i].image = tileset.getSubimage(tileX, 0, tileWidth, tileHeight);
                tile[i].image = UtilityTool.scaleImage(tile[i].image, gp.tileSize, gp.tileSize); //scales the image
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTileCollision(String fileName) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            String indexes[] = line.split(" ");
            for (int i = 0; i < tile.length; i++) {
                if (Integer.parseInt(indexes[i]) == 1) {
                    tile[i].collision = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(int[][] mapIndex,String fileName) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName); // import della mappa
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // reader
            int col = 0;
            int row = 0;
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(","); // splitta la linea in un array di stringhe dallo spazio
                    int num = Integer.parseInt(numbers[col]);
                    if(num == -1)
                        num = 48; //mette trasparente al posto che nullo (il tile trasparente piu vicino e' 48 sorry for bad dev)
                    mapIndex[col][row] = num; // prende il numero e lo mette nell'array
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];
            //int structureNum = mapStructNum[worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
