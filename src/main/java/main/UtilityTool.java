package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import javax.imageio.ImageIO;
import tile.Point;

//class Utility
//a bunch of usefull methods to handle imagines and csv files
public class UtilityTool {
    // method to scale a image
    // take as parametet the original image and scaled width and height.
    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        return scaledImage;
    }

    // method to get a image from a file
    // gets as image the file path
    public BufferedImage getBufferedImage(String fileName) {
        BufferedImage tempImg = null;
        try {
            tempImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempImg;
    }

    // method to check if a image is fully transparent
    // gets as parameter the image to be checked
    public static boolean isTransparentImage(BufferedImage image) {
        if (image == null)
            return true; // null image by default

        // Loop through every pixel
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);

                // Extract the alpha value (most significant 8 bits)
                int alpha = (pixel >> 24) & 0xFF;

                if (alpha != 0) {
                    return false; // Found a non-transparent pixel
                }
            }
        }
        return true;
    }

    // method do get the width dimension of a csv file
    // used to get map layers dimension
    // gets as parameter the file filePath
    public int getCsvWidth(String fileName) {
        int width = 0;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName); // import della mappa
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // reader
            String line = br.readLine();
            if (line != null) {
                String[] columm = line.split(",");
                width = columm.length;
            }
        } catch (Exception e) {
        }
        return width;
    }

    // method do get the width dimension of a csv file
    // used to get map layers dimension
    // gets as parameter the file filePath
    public int getCsvHeight(String fileName) {
        int height = 0;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName); // import della mappa
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // reader
            while (br.readLine() != null) {
                height++;
            }
        } catch (Exception e) {
        }
        return height;
    }

    //method to check if a tile is not in the map
    //is if in the map return false
    //if not returns true
    //takes as paramenters 
    public static boolean checkNullTile(HashMap<Point, Integer> layer, int worldCol, int worldRow) {
        Point p = new Point(worldCol, worldRow);
        int tileNum = layer.getOrDefault(p, -1); // Get the tile number, default to -1 if not found

        // checks if tile is not null
        if (tileNum != -1) {
            return false; // if not null return false
        }
        return true; // if null returns true.
    }

    //method to check if a image is equal as another one
    //takes as parameter 2 imagines
    //used in tilemanager if a tile is equal to NULL.png tile
    public static boolean checkEqualImage(BufferedImage img1, BufferedImage img2) {
        // Check if the dimensions are the same
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }

        // Compare pixel-by-pixel
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
