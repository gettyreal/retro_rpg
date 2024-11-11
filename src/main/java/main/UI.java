package main;

import java.awt.Graphics2D;

import object.OBJ_Door;
import object.OBJ_PokeBall;
import object.OBJ_PokeChest;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;

public class UI {
    GamePanel gp;
    Font arial_16;

    public UI(GamePanel gp) {
        this.gp = gp;
        this.arial_16 = new Font("Arial", Font.BOLD, 16); // Custom font for message
    }

    //draws messages on event, only player can cause these events
    public void drawMessage(Graphics2D g2) {
        int objIndex = gp.OBJChecker.checkObject(gp.player, true); //return a valid integer only if the player collides with the obj
        //checks if the index of obj passed is the null obj
        if (gp.OBJChecker.checkObject(gp.player, true) == 999) { //checks if entity is colliding with obj null
            return;
        }

        if (gp.obj.get(objIndex) instanceof OBJ_PokeBall) {
            draw(g2, "press E to pick up PokeBall", gp.screenWidth / 2, gp.screenHeight / 2 + (4 * gp.tileSize));
        }
        if (gp.obj.get(objIndex) instanceof OBJ_PokeChest) {
            OBJ_PokeChest pokechest = (OBJ_PokeChest)gp.obj.get(objIndex); //downcast to obtain opened attribute
            if (pokechest.opened == false) { //checks if pokechest is opened or not
                draw(g2, "press E to open PokeChest", gp.screenWidth / 2, gp.screenHeight / 2 + (4 * gp.tileSize));   
            } else {
                draw(g2, "PokeChest already opened", gp.screenWidth / 2, gp.screenHeight / 2 + (4 * gp.tileSize));
            }
        }
        if (gp.obj.get(objIndex) instanceof OBJ_Door) {
            draw(g2, "press E to open up Door", gp.screenWidth / 2, gp.screenHeight / 2 + (4 * gp.tileSize));            
        }
    }

    public void draw(Graphics2D g2, String message, int screenX, int screenY) {
        FontMetrics m = g2.getFontMetrics(arial_16);

        // centre text on width
        int messageWidth = m.stringWidth(message);
        screenX = screenX - (messageWidth / 2);

        // centre text on height
        int messageHeight = m.getHeight();
        screenY = screenY - (messageHeight / 2);

        Color backgroundColor = new Color(0, 0, 0, 128); // Nero semi-trasparente (valore alpha 128 su 255)
        g2.setColor(backgroundColor);

        // Disegna il rettangolo semi-trasparente attorno al testo
        g2.fillRoundRect(screenX - 15, screenY - m.getAscent() - 10, messageWidth + 30, messageHeight + 20, 30, 45);

        g2.setFont(arial_16);
        g2.setColor(Color.WHITE); // white text color
        g2.drawString(message, screenX, screenY);
    }
}
