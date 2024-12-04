package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UtilityTool {
    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height,null);
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
        if (image == null) return true; // null image by default

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
}
