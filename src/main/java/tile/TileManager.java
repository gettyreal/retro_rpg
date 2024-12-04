package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.imageio.ImageIO;

import main.CollisionChecker;
import main.GamePanel;
import main.UtilityTool;

public class TileManager {
    GamePanel gp;
    public CollisionChecker cChecker;
    public Tile[] tile;

    //hash map with tiles form tilesets.
    //Tile indicates the tile class and Integer is for the position in the big image.
    // this supports larger tilesets with a lot of free space.
    public HashMap<Integer, Tile> tileSet;
    
    //one layer of the maop
    public int mapTileNum[][];

    public TileManager(GamePanel gp, String tilesetFileImg,String mapFileName, String collisionFileName) {
        this.gp = gp;
        cChecker = new CollisionChecker(gp, this);
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        //tileset shit
        getTileImage(tilesetFileImg);
        setTileCollision(collisionFileName);
        System.out.println("breakpoint");
        loadMap(this.mapTileNum, mapFileName);
    }

    public void getTileImage(String fileName) {
        try {
            // Load the entire tileset
            BufferedImage tilesetImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
    
            // Tile dimensions
            int tileWidth = 16;
            int tileHeight = 16;
            int totalCol = tilesetImage.getWidth() / tileWidth;
            int totalRow = tilesetImage.getHeight() / tileHeight;

            this.tileSet = new HashMap<>(); // Initialize HashMap
    
            // Extract tiles and save them
            int tileIndex = 0;
            for (int col = 0; col < totalCol; col++) {
                for (int row = 0; row < totalRow; row++) {
                    int tileX = col * tileWidth;
                    int tileY = row * tileHeight; // Corrected
    
                    Tile tempTile = new Tile();
                    tempTile.image = tilesetImage.getSubimage(tileX, tileY, tileWidth, tileHeight);
                    tempTile.image = UtilityTool.scaleImage(tempTile.image, gp.tileSize, gp.tileSize);
    
                    this.tileSet.put(tileIndex, tempTile); // Use tileIndex as the key
                    tileIndex++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void setTileCollision(String fileName) {
        String line;
        Integer currentTileId = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName));){
            while (br.readLine() != null) {
                line = br.readLine();
                line = line.trim();

                if (line.startsWith("\"id\":")) {
                    String[] splittedLine = line.split(":");
                    currentTileId = Integer.parseInt(splittedLine[1].trim().replace(",", ""));
                }

                 if (line.contains("collisions") && line.contains("true")) {
                    if (currentTileId!= null) {
                        Tile tempTile = tileSet.get(currentTileId);
                        tempTile.collision = true;
                        tileSet.replace(currentTileId, tempTile);
                        currentTileId = null;
                    }
                 }
            }
            br.close();
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
                g2.drawImage(tileSet.get(tileNum).image, screenX, screenY, null);
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
