package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//super class per player mostri npc entita' ecc.

public class Entity {
    public int worldX, worldY; //entity spawn coordinates
    public int speed; //entity speed

    //movement animation
    public BufferedImage[] up = new BufferedImage[4];
    public BufferedImage[] down = new BufferedImage[4];
    public BufferedImage[] left = new BufferedImage[4];
    public BufferedImage[] right = new BufferedImage[4];

    public BufferedImage[] bushUp = new BufferedImage[4];
    public BufferedImage[] bushDown = new BufferedImage[4];
    public BufferedImage[] bushLeft = new BufferedImage[4];
    public BufferedImage[] bushRight = new BufferedImage[4];

    public String direction; //direction of the player

    public int spriteCounter = 0; //index of sprite active on screen
    public int spriteNumber = 0;

    //collisions
    public Rectangle collisionArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean bushIn = false;
}
