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
        loadMap("maps/map01.txt");
        //loadMap("maps/vegetation-map.txt");
    }

    public void getTileImage() {
        try {
            // Carica l'immagine unica con tutte le tile in fila
            BufferedImage tileset = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/island_tileSet.png"));
    
            // Dimensioni di ciascuna tile
            int tileWidth = 16;
            int tileHeight = 16;
            int totalTiles = 24; // Numero totale di tile
    
            // Inizializza l'array per contenere le immagini delle singole tile
            tile = new Tile[totalTiles];
    
            // Estrai ciascuna tile e salvala nell'array
            for (int i = 0; i < totalTiles; i++) {
                int tileX = i * tileWidth;
                tile[i] = new Tile();
                tile[i].image = tileset.getSubimage(tileX, 0, tileWidth, tileHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void loadMap(String fileName) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName); //import txt file
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //read txt file
            int col = 0;
            int row = 0;
            while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
                String line = br.readLine();
                while (col < gp.maxScreenCol) {
                    String numbers[] = line.split(" "); // splitta la linea in un array di stringhe dallo spazio
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num; // prende il numero e lo mette nell'array
                    col++;
                }
                if(col == gp.maxScreenCol) {
                    col = 0;
                    row++;
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
