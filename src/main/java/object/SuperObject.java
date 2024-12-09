package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import main.GamePanel;
import main.UtilityTool;

//class superObject to determine single object subclass
public class SuperObject{
    public BufferedImage image; //object image
    public String name; //object name
    public String actionCode; //object action code to determine specific action
    public boolean collision = false; //object collision
    public boolean pickable = false; //pickable to determin if object is pickable or not
    public int worldX, worldY;  //object coordinates
    public Rectangle collisionArea = new Rectangle(0, 0, 48, 48); //object hitbox
    //hit box proprieties
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    //default method to draw object on map
    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            g2.drawImage(this.image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    //default method to load the object image
    public void loadImage(String filename) {
        try {
            this.image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));
            UtilityTool.scaleImage(image, 64, 64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
