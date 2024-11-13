package entity;

import java.awt.Rectangle;
import java.util.Random;

import main.GamePanel;

public class Pokemon extends Entity {
    public String name;

    public Pokemon(GamePanel gp, String name, String packageName, int Xoffset, int Yoffset) {
        //calls entity constructor
        super(gp);
        //sets default values
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
        int randomTime = random.nextInt(90) + 30;
        if (updateLockCounter > randomTime) { // 2 seconds
            int i = random.nextInt(100) + 1; // random 1 - 100;

            // sets random direction
            if (i <= 25) {
                direction = "up";
            } else if (i > 25 && i <= 50) {
                direction = "down";
            } else if (i > 50 && i <= 75) {
                direction = "left";
            } else if (i > 75) {
                direction = "right";
            }
            updateLockCounter = 0;
        }
    }
}
