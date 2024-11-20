package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import entity.npc.Doctor_Oak;
import entity.npc.Nurse_Joy;
import object.OBJ_Door;
import object.OBJ_PokeBall;
import object.OBJ_PokeChest;
import object.OBJ_nurseDialogue;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;

public class UI {
    GamePanel gp;
    Timer t;
    Font dialog_16;
    Font dialog_24;
    Font dialog_80;

    public String currentDialog;

    int width;
    int height;

    public UI(GamePanel gp) {
        this.gp = gp;
        t = new Timer(1000, null);
        this.dialog_16 = new Font("Dialog", Font.BOLD, 16); // Custom font for message
        this.dialog_80 = new Font("Dialog", Font.BOLD, 80);
        this.dialog_24 = new Font("Dialog", Font.BOLD, 24);

        width = gp.screenWidth;
        height = gp.screenHeight;
    }

    // draws messages on event, only player can cause these events
    public void draw(Graphics2D g2) {
        if (gp.gameState == gp.playState) {
            drawPlayMessages(g2);
        }
        if (gp.gameState == gp.pauseState) { // pause screen
            drawPauseScreen(g2);
            return;
        }
        if (gp.gameState == gp.dialogState) {
            drawDialogueScreen(g2);
        }
        if (gp.gameState == gp.battleState) {
            drawBattleScreen(g2);
        }

    }

    public void drawPlayMessages(Graphics2D g2) {
        // play state messages
        int objIndex = gp.Checker.checkObject(gp.player, true);
        int npcIndex = gp.Checker.checkEntity(gp.player, gp.mapM.maps.get(gp.currentMap).npc);

        // PLAYER - OBJ MESSAGES
        if (objIndex != 999) { // checks if entity is colliding with obj null

            // pokeball message
            if (gp.mapM.maps.get(gp.currentMap).obj.get(objIndex) instanceof OBJ_PokeBall) {
                drawMessage(g2, "press E to pick up PokeBall", gp.screenWidth / 2,
                        gp.screenHeight / 2 + (4 * gp.tileSize));
            }

            // pokechest message
            if (gp.mapM.maps.get(gp.currentMap).obj.get(objIndex) instanceof OBJ_PokeChest) {
                OBJ_PokeChest pokechest = (OBJ_PokeChest) gp.mapM.maps.get(gp.currentMap).obj.get(objIndex); // downcast
                                                                                                             // to
                                                                                                             // obtain
                                                                                                             // opened
                                                                                                             // attribute
                if (pokechest.opened == false) { // checks if pokechest is opened or not
                    drawMessage(g2, "press E to open PokeChest", gp.screenWidth / 2, // when not opened
                            gp.screenHeight / 2 + (4 * gp.tileSize));
                } else {
                    drawMessage(g2, "PokeChest already opened", gp.screenWidth / 2, // when opened
                            gp.screenHeight / 2 + (4 * gp.tileSize));
                }
            }

            // door message
            if (gp.mapM.maps.get(gp.currentMap).obj.get(objIndex) instanceof OBJ_Door) {
                drawMessage(g2, "press E to open up Door", gp.screenWidth / 2,
                        gp.screenHeight / 2 + (4 * gp.tileSize));
            }

            if (gp.mapM.maps.get(gp.currentMap).obj.get(objIndex) instanceof OBJ_nurseDialogue) {
                drawMessage(g2, "press F to talk to Nurse Joy", gp.screenWidth / 2,
                        gp.screenHeight / 2 + (4 * gp.tileSize));
            }
        }

        // PLAYER - NPC MESSAGES
        if (npcIndex != 999) {

            // doctor oaK message.
            if (gp.mapM.maps.get(gp.currentMap).npc.get(npcIndex) instanceof Doctor_Oak) {
                drawMessage(g2, "press F to talk to Dottor Oak", gp.screenWidth / 2,
                        gp.screenHeight / 2 + (4 * gp.tileSize));
            }

            // nurse joy message
            if (gp.mapM.maps.get(gp.currentMap).npc.get(npcIndex) instanceof Nurse_Joy) {
                drawMessage(g2, "press F to talk to Nurse Joy", gp.screenWidth / 2,
                        gp.screenHeight / 2 + (4 * gp.tileSize));
            }
        }
    }

    public void drawMessage(Graphics2D g2, String message, int screenX, int screenY) {
        FontMetrics m = g2.getFontMetrics(dialog_16);

        // centre text on width
        int messageWidth = m.stringWidth(message);
        screenX = screenX - (messageWidth / 2);

        // centre text on height
        int messageHeight = m.getHeight();
        screenY = screenY - (messageHeight / 2);

        drawSubWindow(g2, screenX - 15, screenY - m.getAscent() - 10, messageWidth + 30, messageHeight + 20);

        g2.setFont(dialog_16);
        g2.setColor(Color.WHITE); // white text color
        g2.drawString(message, screenX, screenY);
    }

    public void drawPauseScreen(Graphics2D g2) {
        String text = " Game Paused ";

        // Draw semi-transparent background
        drawSubWindow(g2, 0, 0, gp.screenWidth, gp.screenHeight);

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

    public void drawDialogueScreen(Graphics2D g2) {

        // continues the dialog on enter press
        if (gp.keyH.enterPressed == true) {
            gp.mapM.maps.get(gp.currentMap).npc
                    .get(gp.player.npcIndex).speak();
            gp.keyH.enterPressed = false;
        }

        int x = gp.tileSize;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 2);
        int height = gp.tileSize * 4;

        drawSubWindow(g2, x, y, width, height);

        g2.setColor(Color.white);
        g2.setFont(dialog_24);

        x += gp.tileSize;
        y += gp.tileSize;
        for (String line : currentDialog.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 150)); // Black with transparency
        g2.fillRect(x, y, width, height);
    }

    // BATTLE ANIMATIONS

    long animationStartTime = 0; // Tempo d'inizio dell'animazione
    boolean isAnimationActive = true; // Stato dell'animazione
    final int animationDuration = 750; // Durata in millisecondi

    public void drawBattleScreen(Graphics2D g2) {
        getBattleImagines(g2);
        battleAnimation(g2);
    }
    
    public void getBattleImagines(Graphics2D g2) {
        UtilityTool ui = new UtilityTool();
        BufferedImage image;

        // Disegna lo sfondo della schermata di battaglia
        image = ui.getBufferedImage("screens/battle_forest.png");
        image = UtilityTool.scaleImage(image, image.getWidth() * 2, image.getHeight() * 2);
        g2.drawImage(image, -128, 0, image.getWidth(), image.getHeight(), null);

        // Disegna l'immagine del Pokémon selvatico
        image = gp.mapM.maps.get(gp.currentMap).pokemons.get(gp.player.pokemonIndex).battleImage; // Pokémon che collide
        g2.drawImage(image, 17 * gp.tileSize, 5 * gp.tileSize, image.getWidth(), image.getHeight(), null);
    }

    public void battleAnimation(Graphics2D g2) {

        if (isAnimationActive) {
            if (animationStartTime == 0) {
                animationStartTime = System.currentTimeMillis();
            }

            long elapsedTime = System.currentTimeMillis() - animationStartTime;

            // Durata per ciascuna animazione (rettangoli e immagine)
            int rectangleAnimationDuration = animationDuration / 2; // Prima metà del tempo
            int imageAnimationDuration = animationDuration / 2;    // Seconda metà del tempo

            if (elapsedTime <= rectangleAnimationDuration) {
                // **Animazione rettangoli**
                double progress = (double) elapsedTime / rectangleAnimationDuration;

                int maxHeight = gp.screenHeight / 2; // Altezza iniziale massima
                int currentHeight = (int) (maxHeight * (1.0 - progress));

                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, gp.screenWidth, currentHeight); // Rettangolo superiore
                g2.fillRect(0, gp.screenHeight - currentHeight, gp.screenWidth, currentHeight); // Rettangolo inferiore
            } else {
                // **Animazione immagine Pokémon**
                double progress = Math.min(1.0, (double) (elapsedTime - rectangleAnimationDuration) / imageAnimationDuration);

                // Ottieni l'immagine del Pokémon
                BufferedImage image = gp.mapM.maps.get(gp.currentMap).pokemons.get(gp.player.pokemonIndex).battleImage;

                // Calcola la posizione dell'immagine
                int finalX = (int) (gp.screenWidth * 0.75) - (image.getWidth() / 2); // 3/4 dello schermo
                int finalY = (gp.screenHeight - image.getHeight()) / 2; // Centro verticale
                int startX = gp.screenWidth; // Partenza fuori dallo schermo a destra
                int currentX = (int) (startX + (finalX - startX) * progress);

                // Disegna l'immagine nella posizione corrente
                g2.drawImage(image, currentX, finalY, image.getWidth(), image.getHeight(), null);

                if (progress >= 1.0) {
                    // L'immagine rimane ferma una volta completato il movimento
                    isAnimationActive = false; // Fine animazione generale, ma l'immagine resta visibile
                }
            }
        } else {
            // Mostra direttamente l'immagine al suo stato finale se l'animazione è terminata
            BufferedImage image = gp.mapM.maps.get(gp.currentMap).pokemons.get(gp.player.pokemonIndex).battleImage;
            int finalX = (int) (gp.screenWidth * 0.75) - (image.getWidth() / 2); // 3/4 dello schermo
            int finalY = (gp.screenHeight - image.getHeight()) / 2; // Centro verticale
            g2.drawImage(image, finalX, finalY, image.getWidth(), image.getHeight(), null);
        }
    }
}
