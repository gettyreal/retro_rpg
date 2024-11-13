package entity.pokemon;

import java.awt.Rectangle;
import java.util.Random;

import entity.Entity;
import entity.Pokemon;
import main.GamePanel;

public class Cubone extends Entity implements Pokemon {
    public Cubone(GamePanel gp) {
        super(gp);
        this.direction = "down";
        this.speed = 1;
        getEntityImage("pokemon/cubone/cubone_");

        Xoffset = 8;
        Yoffset = 12;
        this.collisionArea = new Rectangle(Xoffset, Yoffset, this.down[0].getWidth(), this.down[0].getHeight());
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
