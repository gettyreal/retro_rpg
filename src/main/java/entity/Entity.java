package entity;

import java.awt.image.BufferedImage;

//super class per player mostri npc entita' ecc.

public class Entity {
    public int x, y; //entity spawn coordinates
    public int speed; //entity speed

    //movement animation
    public BufferedImage[] up = new BufferedImage[8];
    public BufferedImage[] down = new BufferedImage[8];
    public BufferedImage[] left = new BufferedImage[8];
    public BufferedImage[] right = new BufferedImage[8];
    public String direction; //direction of the player

    public int spriteCounter = 0; //index of sprite active on screen
    public int spriteNumber = 0;
}
