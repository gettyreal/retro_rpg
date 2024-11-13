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
    Graphics2D g2;
    Font dialog_16;
    Font dialog_80;

    public UI(GamePanel gp) {
        this.gp = gp;
        this.dialog_16 = new Font("Dialog", Font.BOLD, 16); // Custom font for message
        this.dialog_80 = new Font("Dialog", Font.BOLD, 80);
    }

    // draws messages on event, only player can cause these events
    public void drawMessage(Graphics2D g2) {
        if (gp.gameState == gp.pauseState) { // pause screen
            drawPauseScreen(g2);
        }

        // play state messages
        int objIndex = gp.Checker.checkObject(gp.player, true); // return a valid integer only if the player collides
                                                                // with the obj
        // checks if the index of obj passed is the null obj
        if (gp.Checker.checkObject(gp.player, true) == 999) { // checks if entity is colliding with obj null
            return;
        }

        if (gp.obj.get(objIndex) instanceof OBJ_PokeBall) {
            drawPlayerMessage(g2, "press E to pick up PokeBall", gp.screenWidth / 2,
                    gp.screenHeight / 2 + (4 * gp.tileSize));
        }
        if (gp.obj.get(objIndex) instanceof OBJ_PokeChest) {
            OBJ_PokeChest pokechest = (OBJ_PokeChest) gp.obj.get(objIndex); // downcast to obtain opened attribute
            if (pokechest.opened == false) { // checks if pokechest is opened or not
                drawPlayerMessage(g2, "press E to open PokeChest", gp.screenWidth / 2,
                        gp.screenHeight / 2 + (4 * gp.tileSize));
            } else {
                drawPlayerMessage(g2, "PokeChest already opened", gp.screenWidth / 2,
                        gp.screenHeight / 2 + (4 * gp.tileSize));
            }
        }
        if (gp.obj.get(objIndex) instanceof OBJ_Door) {
            drawPlayerMessage(g2, "press E to open up Door", gp.screenWidth / 2,
                    gp.screenHeight / 2 + (4 * gp.tileSize));
        }
    }

    public void drawPlayerMessage(Graphics2D g2, String message, int screenX, int screenY) {
        this.g2 = g2;

        FontMetrics m = g2.getFontMetrics(dialog_16);

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

        g2.setFont(dialog_16);
        g2.setColor(Color.WHITE); // white text color
        g2.drawString(message, screenX, screenY);
    }

    public void drawPauseScreen(Graphics2D g2) {
        String text = "Game Paused";
        
        // Draw semi-transparent background
        g2.setColor(new Color(0, 0, 0, 150)); // Black with transparency
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Set font and color for the text
        g2.setFont(dialog_80);
        g2.setColor(Color.WHITE);
        
        // Center the text horizontally and vertically
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        int y = gp.screenHeight / 2;
    
        // Draw the "Game Paused" message
        g2.drawString(text, x, y);
    }
    
}
