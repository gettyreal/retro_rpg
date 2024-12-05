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
    UtilityTool ut = new UtilityTool();

    // hash map with tiles form tilesets.
    // Tile indicates the tile class and Integer is for the position in the big
    // image.
    // this supports larger tilesets with a lot of free space.
    public HashMap<Integer, Tile> tileSet;

    // hash map with one layer of the map
    //only stores non transparent blocks
    // to be added : only stored player 25 x 25 adiacent area.
    public HashMap<Point, Integer> layerMap;

    int maxCol;
    int maxRow;

    public TileManager(GamePanel gp, String tilesetFileImg, String mapFileName, String collisionFileName) {
        //class shit
        this.gp = gp;
        cChecker = new CollisionChecker(gp, this);

        // tileset shit
        getTileImage(tilesetFileImg);
        setTileCollision(collisionFileName);

        //map shit
        loadMap(mapFileName);
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
            // get first tile as NULL (trasparent) tile
            nullTile.image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/NULL.png"));
            nullTile.image = UtilityTool.scaleImage(nullTile.image, gp.tileSize, gp.tileSize);
            this.tileSet.put(-1, nullTile);

            // Extract tiles and save them
            int tileIndex = 0;
            for (int row = 0; row < totalRow; row++) {
                for (int col = 0; col < totalCol; col++) {
                    int tileX = col * tileWidth;
                    int tileY = row * tileHeight;

                    Tile tempTile = new Tile(); // initialize temptile to get into hashmap
                    tempTile.image = tilesetImage.getSubimage(tileX, tileY, tileWidth, tileHeight);
                    tempTile.image = UtilityTool.scaleImage(tempTile.image, gp.tileSize, gp.tileSize);

                    // adds only if not transparent tile
                    if (!UtilityTool.isNullImage(tempTile.image)) {
                        this.tileSet.put(tileIndex, tempTile);
                    }
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
                    Tile tempTile = tileSet.get(currentTileId); // new tile obj that referf into the hashmap
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
        this.layerMap = new HashMap<>();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName); // import della mappa
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // reader

            maxCol = ut.getCsvWidth(fileName);
            maxRow = ut.getCsvHeight(fileName);
            int col = 0;
            int row = 0;

            while (col < maxCol && row < maxRow) {
                String line = br.readLine();
                while (col < maxCol) {
                    String numbers[] = line.split(","); // splitta la linea in un array di stringhe dallo spazio
                    int num = Integer.parseInt(numbers[col]);

                    if (tileSet.containsKey(num) && num != -1) {
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
        for (int worldRow = 0; worldRow < maxRow; worldRow++) {
            for (int worldCol = 0; worldCol < maxCol; worldCol++) {
                Point p = new Point(worldCol, worldRow);
                int tileNum = layerMap.getOrDefault(p, -1); // Get the tile number, default to -1 if not found
                Tile tile = tileSet.get(tileNum);

                //preconditions for skipping iteration
                if (tileNum == -1) { //skips transparent tile.
                    continue;
                }
                if (tile == null) {// Ensure the tile exists in the tileSet
                    continue; // Skip invalid tiles
                }

                // Calculate world and screen positions
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                // Only draw tiles within the visible area
                if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                        worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                        worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                        worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                    // Draw the tile image
                    g2.drawImage(tile.image, screenX, screenY, null);
                }
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
