package entity;

import java.awt.Rectangle;

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
}
