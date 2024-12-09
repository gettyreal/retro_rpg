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

//class to handle single layer maps
public class TileManager {
    GamePanel gp; // game panel
    public CollisionChecker cChecker; // collision checker for each layer
    UtilityTool ut = new UtilityTool(); // usefull functions for handling tilesets and imagines

    // hash map with tiles form tilesets.
    // Tile indicates the tile class and Integer is for the position in the big
    // image.
    // this supports larger tilesets with a lot of free space.
    public HashMap<Integer, Tile> tileSet;

    // hash map with one layer of the map
    // only stores non transparent blocks
    // to be added : only stored player 25 x 25 adiacent area.
    public HashMap<Point, Integer> layerMap;

    // max layer dimensions
    int maxCol;
    int maxRow;

    // constructor
    public TileManager(GamePanel gp, String tilesetFileImg, String mapFileName, String collisionFileName) {
        // class shit
        this.gp = gp;
        cChecker = new CollisionChecker(gp, this);

        // loads layer tileset and gets if tile collision = true
        getTileImage(tilesetFileImg);
        setTileCollision(collisionFileName);

        // load the layer map based on the tileset
        loadMap(mapFileName);
    }

    // method to get all imagines from a tileset
    // gets as parameter the tileset file path (in the resources/Tilesets folder)
    public void getTileImage(String fileName) {
        try {
            // Load the entire image tileset
            BufferedImage tilesetImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));

            // Tile dimensions
            int tileWidth = 32;
            int tileHeight = 32;
            // max tileset dimension
            int totalCol = tilesetImage.getWidth() / tileWidth;
            int totalRow = tilesetImage.getHeight() / tileHeight;

            // instantiate the tileset
            this.tileSet = new HashMap<>(); // Initialize HashMap

            // get first tile as EMPTY (trasparent) tile
            Tile emptyTile = new Tile();
            emptyTile.image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Tilesets/EMPTY.png"));
            emptyTile.image = UtilityTool.scaleImage(emptyTile.image, gp.tileSize, gp.tileSize);
            this.tileSet.put(-1, emptyTile);

            // get NULL tile for skipping null iteration
            Tile nullTile = new Tile();
            nullTile.image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Tilesets/NULL.png"));
            nullTile.image = UtilityTool.scaleImage(nullTile.image, gp.tileSize, gp.tileSize);

            // Extract tiles and save them
            int tileIndex = 0; // index for each tile
            for (int row = 0; row < totalRow; row++) {
                for (int col = 0; col < totalCol; col++) {
                    // x and y position of the tile in the tileset
                    int tileX = col * tileWidth;
                    int tileY = row * tileHeight;

                    Tile tempTile = new Tile(); // initialize temptile to get into hashmap
                    tempTile.image = tilesetImage.getSubimage(tileX, tileY, tileWidth, tileHeight);
                    tempTile.image = UtilityTool.scaleImage(tempTile.image, gp.tileSize, gp.tileSize);

                    // adds only if not null tile
                    if (!UtilityTool.checkEqualImage(tempTile.image, nullTile.image)) {
                        this.tileSet.put(tileIndex, tempTile);
                    }
                    tileIndex++; // increase tile index
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method to set tile collision if needed
    // gets as parameter collision json file path (in the resources/collision
    // folder)
    // checks in collision for each tileset collision json
    // if propretiies collision is true in the json gets the tileNum and sets it to
    // true in the hashmap
    public void setTileCollision(String fileName) {
        String line; // for json in-line reading
        Integer currentTileId = null; // tile id to get the exact tile in the tileset
        try {
            // line reader
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // read lines untill end of file
            while ((line = br.readLine()) != null) {
                line = line.trim(); // òine formatting

                // gets the id of the tile for proprieties handling
                if (line.startsWith("\"id\":")) {
                    String[] splittedLine = line.split(":");
                    currentTileId = Integer.parseInt(splittedLine[1].trim().replace(",", ""));
                }

                // if value = true in the line sets the collision variable to true
                if (line.contains("value") && line.contains("true")) {
                    Tile tempTile = tileSet.get(currentTileId); // new tile obj that referf into the hashmap
                    tempTile.collision = true;
                    currentTileId = null;
                }
            }
            br.close(); // closes line reader
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method to load layer map
    // gets ad parameter the layer file path (in the resources/maps folder)
    public void loadMap(String fileName) {
        this.layerMap = new HashMap<>(); // instatiate layer hashmap
        try {
            // layer map name
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // max map dimension
            maxCol = ut.getCsvWidth(fileName);
            maxRow = ut.getCsvHeight(fileName);
            // indexes for tiles coordinates
            int col = 0;
            int row = 0;

            // load map
            while (col < maxCol && row < maxRow) {
                String line = br.readLine(); // line reading
                while (col < maxCol) { // check each row
                    String numbers[] = line.split(","); // splitta la linea in un array di stringhe dallo spazio
                    int num = Integer.parseInt(numbers[col]);

                    // adds tile to map only if the arent null (-1) and tilemun in in the tileset
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
            br.close(); // close reader
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method to draw the layers
    public void draw(Graphics2D g2) {
        for (int worldRow = 0; worldRow < maxRow; worldRow++) {
            for (int worldCol = 0; worldCol < maxCol; worldCol++) {
                Point p = new Point(worldCol, worldRow);
                int tileNum = layerMap.getOrDefault(p, -1); // Get the tile number, default to -1 if not found
                Tile tile = tileSet.get(tileNum);

                // preconditions for skipping iteration
                if (tileNum == -1) { // skips transparent tile.
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

    // method to visualize the entire tileset on console
    // user for debugging only
    public void visualizzaTileSet() {
        for (Map.Entry<Integer, Tile> entry : tileSet.entrySet()) {
            Tile tempTile = entry.getValue();
            System.out.print("| " + entry.getKey());
            System.out.print(tempTile.collision + "|");
        }
    }
}
