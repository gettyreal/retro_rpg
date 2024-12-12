package entity;

import java.awt.image.BufferedImage;

import main.UtilityTool;

public class Sprite {
    BufferedImage body;
    BufferedImage head;

    public Sprite(BufferedImage body, BufferedImage head) {
        this.body = body;
        this.head = head;
    }

    public void scaleSprites() {
        this.head = UtilityTool.scaleImage(this.head, this.head.getWidth() * 2, this.head.getHeight() * 2);                
        this.body = UtilityTool.scaleImage(this.body, this.body.getWidth() * 2, this.body.getHeight() * 2);
    }
}
