package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.UtilityTool;

public class NPC extends Entity {
    public NPC(GamePanel gp, String name, String packageName) {
        // calls entity constructor
        super(gp);
        // sets default values
        this.name = name;
        this.direction = "down";
        this.movingDisabled = true;
        this.speed = 4;
        // gets player image
        getEntityImage(packageName);

        // gets collision area parameters
        this.Xoffset = 0;
        this.Yoffset = 0;
        collisionArea = new Rectangle(Xoffset, Yoffset, 64, 64);
        solidAreaDefaultX = collisionArea.x;
        solidAreaDefaultY = collisionArea.y;
    }

    /*
     * - image formatting parameters
     * - 6px line in between sprites
     * - single sprite 32x42
     */

    @Override
    public void getEntityImage(String packagePath) {
        UtilityTool ui = new UtilityTool();
        BufferedImage npcSprite = ui.getBufferedImage("characters/NPC/" + packagePath + ".png");
        int x = 0;
        int y = 6;
        int spriteHeight = 42;
        int spriteWidth = 32;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                switch (i) {
                    case 0:
                        this.down[j] = npcSprite.getSubimage(x, y, spriteWidth, spriteHeight);
                        this.down[j] = UtilityTool.scaleImage(this.down[j], this.down[j].getWidth() * 2, this.down[j].getHeight() * 2);
                        break;
                    case 1:
                        this.left[j] = npcSprite.getSubimage(x, y, spriteWidth, spriteHeight);
                        //this.left[j] = UtilityTool.scaleImage(this.left[j], spriteWidth * 2, spriteHeight * 2);
                        break;
                    case 2:
                        this.right[j] = npcSprite.getSubimage(x, y, spriteWidth, spriteHeight);
                        //this.right[j] = UtilityTool.scaleImage(this.right[j], spriteWidth * 2, spriteHeight * 2);
                        break;
                    case 3:
                        this.up[j] = npcSprite.getSubimage(x, y, spriteWidth, spriteHeight);
                        //this.up[j] = UtilityTool.scaleImage(this.up[j], spriteWidth * 2, spriteHeight * 2);
                        break;
                }
                x += spriteWidth;
            }
            x = 0;
            y += spriteHeight + 6;
        }
    }
}
