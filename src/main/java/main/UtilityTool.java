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

public class UtilityTool {
    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }

    public BufferedImage getBufferedImage(String fileName) {
        BufferedImage tempImg = null;
        try {
            tempImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempImg;
    }

    public static boolean isNullImage(BufferedImage image) {
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

    public static boolean checkNullTile(HashMap<Point, Integer> layer,int worldCol, int worldRow) {
        Point p = new Point(worldCol, worldRow);
        int tileNum = layer.getOrDefault(p, -1); // Get the tile number, default to -1 if not found

        //checks if tile is not null
        if (tileNum != -1) { 
            return false; //if not null return false
        }
        return true; //if null returns true.
    }
}
