package entity;

import java.awt.Rectangle;
import java.util.Random;

import main.GamePanel;

public class NPC extends Entity{
    public NPC(GamePanel gp, String name, String packageName, int Xoffset, int Yoffset) {
        //calls entity constructor
        super(gp);
        //sets default values
        this.name = name;
        this.direction = "down";
        this.speed = 1;
        //gets player image
        getEntityImage(packageName);
        
        // gets collision area parameters
        this.Xoffset = Xoffset;
        this.Yoffset = Yoffset;
        collisionArea = new Rectangle(Xoffset, Yoffset, this.down[0].getWidth(), this.down[0].getHeight());
        solidAreaDefaultX = collisionArea.x;
        solidAreaDefaultY = collisionArea.y;
    }

    @Override
    public void setAction() {
        updateLockCounter++;
        Random random = new Random();
        int randomTime = random.nextInt(240) + 180; // Intervallo di 2-3 secondi circa
    
        if (updateLockCounter > randomTime) {
            int i = random.nextInt(100) + 1; // numero casuale tra 1 e 100

            if (i <= 70) {
                direction = "idle";
            } else if (i > 70 && i <= 80) {
                direction = "up";
            } else if (i > 80 && i <= 90) {
                direction = "down";
            } else if (i > 90 && i <= 95) {
                direction = "left";
            } else {
                direction = "right";
            }
    
            updateLockCounter = 0;
        }
    }
}
