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
    int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        getTileImage();
        loadMap();
    }

    public void getTileImage() {

        try {
            // Carica l'immagine del tileset
            BufferedImage tileset = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/island-tileset.png"));

            // Dimensioni di ciascuna tile
            int tileWidth = 16;
            int tileHeight = 16;
            int tileSpacing = 1; // Spazio nero di separazione

            // Calcola il numero di tile in base alle dimensioni dell'immagine
            int rows = (tileset.getHeight() + tileSpacing) / (tileHeight + tileSpacing);
            int cols = (tileset.getWidth() + tileSpacing) / (tileWidth + tileSpacing);

            // Array per contenere le immagini delle singole tile
            tile = new Tile[rows * cols];  //specifica dimensioni array tile.

            // Estrai ciascuna tile e salvala nell'array
            int index = 0;
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    int tileX = x * (tileWidth + tileSpacing);
                    int tileY = y * (tileHeight + tileSpacing);
                    tile[index] = new Tile();
                    tile[index].image = tileset.getSubimage(tileX, tileY, tileWidth, tileHeight);
                    index++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("maps/map01.txt"); //import txt file
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //read txt file
            int col = 0;
            int row = 0;
            while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
                String line = br.readLine();
                while (col < gp.maxScreenCol) {
                    String numbers[] = line.split(" "); // splitta la linea in un array di stringhe dallo spazio
                    int num = Integer.parseInt(numbers[col]);
                    System.out.print(num + " ");
                    mapTileNum[col][row] = num; // prende il numero e lo mette nell'array
                    col++;
                }
                if(col == gp.maxScreenCol) {
                    col = 0;
                    row++;
                    System.out.println();
                }
            }

            for (int i = 0; i < gp.maxScreenCol; i++) {
                for (int j = 0; j < gp.maxScreenRow; j++) {
                    System.out.println(mapTileNum[i][j]);
                }
            }
            br.close();
        } catch (Exception e) {
        }
    }

    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            int tileNum = mapTileNum[col][row];
            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            col++;
            x += gp.tileSize;

            if (col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}
