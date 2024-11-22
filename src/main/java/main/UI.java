package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Timer;

import entity.npc.Doctor_Oak;
import entity.npc.Nurse_Joy;
import object.OBJ_Door;
import object.OBJ_PokeBall;
import object.OBJ_PokeChest;
import object.OBJ_nurseDialogue;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.BasicStroke;
import java.awt.Color;

public class UI {
    GamePanel gp;
    UtilityTool ut;

    // timers
    Timer dialogueTimer;
    Timer titleTimer;
    Timer dotTimer;

    // fonts
    Font dialog_16;
    Font dialog_24;
    Font dialog_80;
    Font pokemon_font;
    Font pokemon_16;
    Font pokemon_14;
    Font pokemon_64;

    // Colors
    Color black = Color.BLACK;
    Color white = Color.white;
    Color boxOutline1 = new Color(66, 117, 160);
    Color boxOutline2 = new Color(162, 208, 232);
    Color textColor = new Color(101, 101, 101);
    Color textShadow = new Color(212, 212, 212);

    // Screen states
    public int titleScreenState = 0;

    // animation states
    private boolean showStartText = true;
    long animationStartTime = 0; // Tempo d'inizio dell'animazione
    boolean isAnimationActive = true; // Stato dell'animazione
    final int animationDuration = 3750; // Durata in millisecondi
    long elapsedTime;

    // divs propreties
    int width;
    int height;
    int dotHeight;
    int dotOffset;
    int dotCount;

    // text propreties
    // dialog states
    public String currentDialog;
    public String currentString = "";
    int charIndex = 0;
    int yOffset = 0;

    Doctor_Oak dr;
    BufferedImage down_arrow;

    public UI(GamePanel gp) {
        this.gp = gp;
        ut = new UtilityTool();

        dr = new Doctor_Oak(gp);
        down_arrow = ut.getBufferedImage("screens/down_arrow.png");
        down_arrow = UtilityTool.scaleImage(down_arrow, down_arrow.getWidth() * 3, down_arrow.getHeight() * 3);

        // timers.
        titleTimer = new Timer(500, event -> {
            showStartText = !showStartText;
            gp.repaint();
        });
        titleTimer.start();
        dotTimer = new Timer(100, event -> {
            dotCount++;
            if (dotCount <= 5) {
                dotOffset--;
            } else {
                dotOffset++;
            }
            if (dotCount == 10) {
                dotCount = 0;
            }
            gp.repaint();
        });
        dotTimer.start();
        dialogueTimer = new Timer(40, event -> {
            currentString += currentDialog.charAt(charIndex);
            charIndex++;
            if (charIndex == currentDialog.length()) {
                charIndex = 0;
                dialogueTimer.stop();
            }
        });

        // fonts
        this.dialog_16 = new Font("Dialog", Font.BOLD, 16); // Custom font for message
        this.dialog_80 = new Font("Dialog", Font.BOLD, 80);
        this.dialog_24 = new Font("Dialog", Font.BOLD, 24);

        try {
            InputStream is = getClass().getResourceAsStream("/font/Pokemon_Classic.ttf");
            this.pokemon_font = Font.createFont(Font.TRUETYPE_FONT, is);
            this.pokemon_16 = pokemon_font.deriveFont(Font.BOLD, 16f);
            this.pokemon_14 = pokemon_font.deriveFont(Font.BOLD, 14f);
            this.pokemon_64 = pokemon_font.deriveFont(Font.BOLD, 64f);
            is.close();
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // attributes
        width = gp.screenWidth;
        height = gp.screenHeight;
    }

    // draws messages on event, only player can cause these events
    public void draw(Graphics2D g2) {
        if (gp.gameState == gp.titleState) {
            drawTitleScreen(g2);
        }
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

    public void drawTitleScreen(Graphics2D g2) {
        BufferedImage titleScreen = ut.getBufferedImage("screens/title_screen.png");
        titleScreen = UtilityTool.scaleImage(titleScreen, (int) (titleScreen.getWidth() * 3.5),
                (int) (titleScreen.getHeight() * 3.5));
        BufferedImage oakDialogue = ut.getBufferedImage("screens/intro_dialogue.png");

        if (titleScreenState == 0) {
            // draws backround gamescreen
            g2.drawImage(titleScreen, -8, 32, titleScreen.getWidth(), titleScreen.getHeight(), null);

            // draws "press any key to start"
            String text = "Press any Key to Start";
            g2.setFont(pokemon_16);
            g2.setColor(white);
            int x = 128;
            int y = 508;
            if (showStartText) {
                g2.drawString(text, x, y);
            }
        }
        if (titleScreenState == 1) {
            if (elapsedTime < 1750) {
                g2.drawImage(titleScreen, -8, 32, titleScreen.getWidth(), titleScreen.getHeight(), null);
            } else {
                g2.drawImage(oakDialogue, 0, -92, oakDialogue.getWidth(), oakDialogue.getHeight(), null);
            }
            if (elapsedTime > 3500) { //start oak speech at the end of animation.
                dr.speak();
                dialogueTimer.start();
                titleScreenState = 2;
            }
            fadeAnimation(g2);
        }
        if (titleScreenState == 2) {
            g2.drawImage(oakDialogue, 0, -92, oakDialogue.getWidth(), oakDialogue.getHeight(), null);

            // continues the dialog on enter press
            if (gp.keyH.enterPressed == true) {
                dr.speak();
                gp.keyH.enterPressed = false;
            }

            drawMessage(g2, currentString);
        }
        if (titleScreenState == 3) {
            fadeAnimation(g2);
        }
    }

    private void fadeAnimation(Graphics2D g2) {
        if (isAnimationActive) {
            if (animationStartTime == 0) {
                animationStartTime = System.currentTimeMillis();
            }
    
            elapsedTime = System.currentTimeMillis() - animationStartTime;
            int transparency = 0;

            if (elapsedTime <= animationDuration) {
                if (elapsedTime < 1500) {
                    // Incremento discreto della trasparenza in base al tempo trascorso
                    transparency = (int) (255 * (elapsedTime / 1500.0));
                } else if (elapsedTime < 2000) {
                    transparency = 255;
                } else if (elapsedTime < 3500) {
                    transparency = (int) (255 * (1 - ((elapsedTime - 2000) / 1000.0)));
                } else {
                    transparency = 0;
                }
    
                // Arrotondare il valore della trasparenza a intervalli discreti
                if (transparency > 0 && transparency < 255) {
                    transparency = (transparency / 16) * 16; // Arrotondamento al multiplo di 16
                }
    
                transparency = Math.max(0, Math.min(255, transparency)); // Garantire che sia tra 0 e 255
    
                g2.setColor(new Color(0, 0, 0, transparency));
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            } else {
                isAnimationActive = false;
                titleScreenState = 2;
            }
        }
    }
    
    

    public void drawPauseScreen(Graphics2D g2) {
        String text = " Game Paused ";

        // Draw semi-transparent background
        drawSubWindow(g2, 0, 0, gp.screenWidth, gp.screenHeight);

        // Set font and color for the text
        g2.setFont(pokemon_64);
        g2.setColor(textColor);

        // Center the text horizontally and vertically
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        int y = gp.screenHeight / 2;

        // Draw the "Game Paused" message
        g2.drawString(text, x, y);
    }

    public void drawPlayMessages(Graphics2D g2) {
        // play state messages
        int objIndex = gp.Checker.checkObject(gp.player, true);
        int npcIndex = gp.Checker.checkEntity(gp.player, gp.mapM.maps.get(gp.currentMap).npc);

        // PLAYER - OBJ MESSAGES
        if (objIndex != 999) { // checks if entity is colliding with obj null

            // pokeball message
            if (gp.mapM.maps.get(gp.currentMap).obj.get(objIndex) instanceof OBJ_PokeBall) {
                drawMessage(g2, "press E to pick up PokeBall");
            }

            // pokechest message
            if (gp.mapM.maps.get(gp.currentMap).obj.get(objIndex) instanceof OBJ_PokeChest) {
                OBJ_PokeChest pokechest = (OBJ_PokeChest) gp.mapM.maps.get(gp.currentMap).obj.get(objIndex); // downcast
                                                                                                             // to
                                                                                                             // obtain
                                                                                                             // opened
                                                                                                             // attribute
                if (pokechest.opened == false) { // checks if pokechest is opened or not
                    drawMessage(g2, "press E to open PokeChest");
                } else {
                    drawMessage(g2, "PokeChest already opened");
                }
            }

            // door message
            if (gp.mapM.maps.get(gp.currentMap).obj.get(objIndex) instanceof OBJ_Door) {
                drawMessage(g2, "press E to open up Door");
            }

            if (gp.mapM.maps.get(gp.currentMap).obj.get(objIndex) instanceof OBJ_nurseDialogue) {
                drawMessage(g2, "press F to talk to Nurse Joy");
            }
        }

        // PLAYER - NPC MESSAGES
        if (npcIndex != 999) {

            // doctor oaK message.
            if (gp.mapM.maps.get(gp.currentMap).npc.get(npcIndex) instanceof Doctor_Oak) {
                drawMessage(g2, "press F to talk to Dottor Oak");
            }

            // nurse joy message
            if (gp.mapM.maps.get(gp.currentMap).npc.get(npcIndex) instanceof Nurse_Joy) {
                drawMessage(g2, "press F to talk to Nurse Joy");
            }
        }
    }

    public void drawMessage(Graphics2D g2, String message) {
        int x = gp.tileSize;
        int y = (gp.screenHeight / 4) * 3;
        int width = gp.screenWidth - (gp.tileSize * 2);
        int height = (int) (gp.tileSize * 2.5);

        drawSubWindow(g2, x, y, width, height);

        g2.setFont(pokemon_16);

        int lastLineLenght = 0;
        for (String line : message.split("\n")) {
            lastLineLenght = (int) g2.getFontMetrics().getStringBounds(line, g2).getWidth();
            g2.setColor(textShadow);
            g2.drawString(line, x + 16 + 2, y + 32 + 2);
            g2.setColor(textColor);
            g2.drawString(line, x + 16, y + 32);
            y += 40;
        }

        dotHeight = y - 25;
        if (!dialogueTimer.isRunning()) {
            g2.drawImage(down_arrow, lastLineLenght + 70, dotHeight + dotOffset, null);
        }
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

        g2.setColor(textColor);
        g2.setFont(pokemon_16);

        int lastLineLenght = 0;
        for (String line : currentString.split("\n")) {
            lastLineLenght = (int) g2.getFontMetrics().getStringBounds(line, g2).getWidth();
            g2.setColor(textShadow);
            g2.drawString(line, x + 16 + 2, y + 32 + 2);
            g2.setColor(textColor);
            g2.drawString(line, x + 16, y + 32);
            y += 40;
        }

        dotHeight = y - 25;
        if (!dialogueTimer.isRunning()) {
            g2.drawImage(down_arrow, lastLineLenght + 70, dotHeight + dotOffset, null);
        }
    }

    public void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(white); // Black with transparency
        g2.fillRoundRect(x, y, width, height, 40, 50);

        g2.setColor(boxOutline1);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, width, height, 40, 50);

        g2.setColor(boxOutline2);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 4, y + 4, width - 8, height - 8, 32, 40);
    }

    // BATTLE
    public void drawBattleScreen(Graphics2D g2) {
        getBattleImagines(g2);
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
}
