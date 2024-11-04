package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    GamePanel gp;
    Tile[] tile;
    public Tile[] structures;
    int mapTileNum[][];
    public int mapStructNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        mapStructNum = new int[gp.maxWorldCol][gp.maxWorldCol];
        tile = new Tile[48];
        structures = new Tile[87];
        getTileImage(this.tile, "tiles/tileset.png");
        getTileImage(this.structures, "tiles/structures.png");
        loadMap(this.mapTileNum ,"maps/worldMap01");
        loadMap(this.mapStructNum, "maps/structureMap01");
        setTileCollision();
    }

    public void getTileImage(Tile[] tilesheet, String fileName) {
        try {
            //carica intero tileset
            BufferedImage tileset = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));

            // Dimensioni di ciascuna tile
            int tileWidth = 16;
            int tileHeight = 16;
            int totalTiles = tileset.getWidth() / tileWidth; // total tiles

            //estrazione dal tileset e salvataggio
            for (int i = 0; i < totalTiles; i++) {
                int tileX = i * tileWidth;
                tilesheet[i] = new Tile();
                tilesheet[i].image = tileset.getSubimage(tileX, 0, tileWidth, tileHeight);
            }
        } catch (IOException e) {
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
                    String numbers[] = line.split(" "); // splitta la linea in un array di stringhe dallo spazio
                    int num = Integer.parseInt(numbers[col]);
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

    public void setTileCollision() {
        for(int i = 1; i < structures.length; i++) {
            structures[i].collision = true;
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];
            int structureNum = mapStructNum[worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                g2.drawImage(structures[structureNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
