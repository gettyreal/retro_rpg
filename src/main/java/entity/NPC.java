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
        this.imageOffset = 20;
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
        int headHeight = 10;
        int spriteHeight = 42;
        int spriteWidth = 32;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                switch (i) {
                    case 0:
                        Sprite downSprite = new Sprite(
                                npcSprite.getSubimage(x, y + headHeight, spriteWidth, spriteHeight - headHeight),
                                npcSprite.getSubimage(x, y, spriteWidth, headHeight));
                        this.down[j] = downSprite;
                        this.down[j].scaleSprites();
                        break;
                    case 1:
                        Sprite leftSprite = new Sprite(
                                npcSprite.getSubimage(x, y + headHeight, spriteWidth, spriteHeight - headHeight),
                                npcSprite.getSubimage(x, y, spriteWidth, headHeight));
                        this.left[j] = leftSprite;
                        this.left[j].scaleSprites();
                        break;
                    case 2:
                        Sprite rightSprite = new Sprite(
                                npcSprite.getSubimage(x, y + headHeight, spriteWidth, spriteHeight - headHeight),
                                npcSprite.getSubimage(x, y, spriteWidth, headHeight));
                        this.right[j] = rightSprite;
                        this.right[j].scaleSprites();
                        break;
                    case 3:
                        Sprite upSprite = new Sprite(
                                npcSprite.getSubimage(x, y + headHeight, spriteWidth, spriteHeight - headHeight),
                                npcSprite.getSubimage(x, y, spriteWidth, headHeight));
                        this.up[j] = upSprite;
                        this.up[j].scaleSprites();
                        break;
                }
                x += spriteWidth;
            }
            x = 0;
            y += spriteHeight + 6;
        }
    }
}
