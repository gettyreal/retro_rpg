package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import main.CollisionChecker;
import main.GamePanel;
import main.UtilityTool;

public class TileManager {
    GamePanel gp;
    public CollisionChecker cChecker;
    public Tile[] tile;

    // hash map with tiles form tilesets.
    // Tile indicates the tile class and Integer is for the position in the big
    // image.
    // this supports larger tilesets with a lot of free space.
    public HashMap<Integer, Tile> tileSet;

    // one layer of the maop
    public int mapTileNum[][];
    public HashMap<Point, Integer> layerMap;

    public TileManager(GamePanel gp, String tilesetFileImg, String mapFileName, String collisionFileName) {
        this.gp = gp;
        cChecker = new CollisionChecker(gp, this);
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        // tileset shit
        getTileImage(tilesetFileImg);
        setTileCollision(collisionFileName);
        loadMap(mapFileName);

        System.out.println("breakpoint");
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
            Tile nullTile = new Tile();
            //get first tile as NULL (trasparent) tile
            nullTile.image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/NULL.png"));
            this.tileSet.put(-1, nullTile);

            // Extract tiles and save them
            int tileIndex = 0;
            for (int row = 0; row < totalRow; row++) {
                for (int col = 0; col < totalCol; col++) {
                    int tileX = col * tileWidth;
                    int tileY = row * tileHeight;

                    Tile tempTile = new Tile(); //initialize temptile to get into hashmap
                    tempTile.image = tilesetImage.getSubimage(tileX, tileY, tileWidth, tileHeight);
                    tempTile.image = UtilityTool.scaleImage(tempTile.image, gp.tileSize, gp.tileSize);
                    
                    //adds only if not transparent tile
                    if (!UtilityTool.isNullImage(tempTile.image)) {
                        this.tileSet.put(tileIndex, tempTile);
                    }
                    tileIndex++;
                }
            }
            visualizzaTileSet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTileCollision(String fileName) {
        String line;
        Integer currentTileId = null;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("\"id\":")) {
                    String[] splittedLine = line.split(":");
                    currentTileId = Integer.parseInt(splittedLine[1].trim().replace(",", ""));
                }
                if (line.contains("value") && line.contains("true")) {
                    Tile tempTile = tileSet.get(currentTileId); //new tile obj that referf into the hashmap
                    tempTile.collision = true;
                    currentTileId = null;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String fileName) {
        UtilityTool ut = new UtilityTool();
        this.layerMap = new HashMap<>();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName); // import della mappa
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // reader
            
            int maxCol = ut.getCsvWidth(fileName);
            int maxRow = ut.getCsvHeight(fileName);
            int col = 0;
            int row = 0;
            
            while (col < maxCol && row < maxRow) {
                String line = br.readLine();
                while (col < maxCol) {
                    String numbers[] = line.split(","); // splitta la linea in un array di stringhe dallo spazio
                    int num = Integer.parseInt(numbers[col]);

                    if (num != -1) {
                        layerMap.put(new Point(col, row), num);
                    }
                    col++;
                }
                if (col == maxCol) {
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
            Point p = new Point(worldCol, worldRow);
            int tileNum = layerMap.get(p);
            // int structureNum = mapStructNum[worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                Tile tile = tileSet.get(tileNum);
                g2.drawImage(tile.image, screenX, screenY, null);
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public void visualizzaTileSet() {
        for (Map.Entry<Integer, Tile> entry : tileSet.entrySet()) {
            Tile tempTile = entry.getValue();
            System.out.print("| " + entry.getKey());
            System.out.print(tempTile.collision + "|");
        }
    }
}
