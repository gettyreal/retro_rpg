package main;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.Timer;
import object.OBJ_Sign;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.BasicStroke;
import java.awt.Color;

public class UI implements ActionListener{
    GamePanel gp; //gamePanel
    UtilityTool ut; //utility tool to handle imagines

    // timers
    public Timer dialogueTimer;
    Timer titleTimer;
    Timer dotTimer;

    // fonts
    Font pokemon_font;
    Font pokemon_16;
    Font pokemon_14;
    Font pokemon_24;
    Font pokemon_32;
    Font pokemon_48;
    Font pokemon_64;

    // Colors
    Color black = Color.BLACK;
    Color white = Color.white;
    Color boxOutline1 = new Color(66, 117, 160);
    Color boxOutline2 = new Color(162, 208, 232);
    Color genderBoxOutline1 = new Color(38, 49, 52);
    Color genderBoxOutline2 = new Color(111, 105, 127);
    Color textColor = new Color(101, 101, 101);
    Color textShadow = new Color(192, 192, 192);

    // Screen states
    public int titleScreenState = 0;

    // animation states
    private boolean showStartText = true;
    public long animationStartTime = 0; // Tempo d'inizio dell'animazione
    public boolean isAnimationActive = false; // Stato dell'animazione
    final int animationDuration = 3750; // Durata in millisecondi
    long elapsedTime;

    // divs propreties
    int width;
    int height;
    int dotHeight;
    int dotOffset;
    int dotCount;
    int arrowX = 450;
    int arrowY = 305;
    int lineOffset = 40;

    // text propreties
    // dialog states
    public String currentDialog;
    public String currentString = "";
    int charIndex = 0;
    int yOffset = 0;

    //start dialigue (unused)

    //down arrow image
    BufferedImage down_arrow;

    public boolean textActive = false;

    //constructor
    //loads class and timers for animations and ui
    public UI (GamePanel gp) {
        this.gp = gp;
        ut = new UtilityTool();

        //load down arrow
        down_arrow = ut.getBufferedImage("screens/down_arrow.png");
        down_arrow = UtilityTool.scaleImage(down_arrow, down_arrow.getWidth() * 4, down_arrow.getHeight() * 4);

        // timers.
        titleTimer = new Timer(500, event -> { //title timer
            showStartText = !showStartText;
            gp.repaint();
        });
        titleTimer.start();
        dotTimer = new Timer(100, event -> { // dialogue dot timer
            dotCount++;
            if (dotCount <= 6) {
                dotOffset--;
            } else {
                dotOffset++;
            }
            if (dotCount == 12) {
                dotCount = 0;
            }
            gp.repaint();
        });
        dotTimer.start();
        dialogueTimer = new Timer(40, event -> { //progressive dialogue start
            currentString += currentDialog.charAt(charIndex);
            charIndex++;
            if (charIndex == currentDialog.length()) {
                charIndex = 0;
                dialogueTimer.stop();
            }
        });

        // fonts
        try {
            InputStream is = getClass().getResourceAsStream("/font/power clear.ttf");
            this.pokemon_font = Font.createFont(Font.TRUETYPE_FONT, is);
            this.pokemon_16 = pokemon_font.deriveFont(Font.BOLD, 16f * 2);
            this.pokemon_14 = pokemon_font.deriveFont(Font.BOLD, 14f);
            this.pokemon_24 = pokemon_font.deriveFont(Font.BOLD, 24f);
            this.pokemon_32 = pokemon_font.deriveFont(Font.BOLD, 32f);
            this.pokemon_48 = pokemon_font.deriveFont(Font.PLAIN, 48f);
            this.pokemon_64 = pokemon_font.deriveFont(Font.BOLD, 64f);
            is.close();
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // attributes
        width = gp.screenWidth;
        height = gp.screenHeight;
    }

    //method which draws messages on event, only player can cause these events
    public void draw(Graphics2D g2) {
        if (gp.gameState == gp.playState) { //play state 
            drawPlayScreen(g2);
        }
        if (gp.gameState == gp.titleState) { //title screen
            drawTitleScreen(g2);
        }
        if (gp.gameState == gp.pauseState) { // pause screen
            drawPauseScreen(g2);
        }
        if (gp.gameState == gp.dialogState) { //dialogue state for npc's and story
            drawDialogueScreen(g2);
        }
        if (gp.gameState == gp.battleState) { //battle state for pokemon and npc battles
            drawBattleScreen(g2);
        }

    }

    //method to draw events onn playstate
    public void drawPlayScreen(Graphics2D g2) {
        gp.userInterface.fadeAnimation(g2, 1600);

        if (textActive) {
            drawMessage(g2, currentString);
        }
    }

    //method to draw the title screen (for now unused)
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
            if (elapsedTime > 3500) { // start oak speech at the end of animation.
                //dr.speak();
                dialogueTimer.start();
                titleScreenState = 2;
            }
            fadeAnimation(g2, 3750);
        }
        if (titleScreenState == 2) {
            g2.drawImage(oakDialogue, 0, -92, oakDialogue.getWidth(), oakDialogue.getHeight(), null);

            // continues the dialog on enter press
            if (gp.keyH.Apressed == true) {
                //dr.speak();
                gp.keyH.Apressed = false;
            }
            drawMessage(g2, currentString);
            //if (dr.dialogueIndex == 11) {
                drawGenderPopUp(g2);
                setPlayerGender();
            //}
        }
    }

    //method to draw the gender character selection in titlstate (unused)
    public void drawGenderPopUp(Graphics2D g2) {
        // checs Y bounds of the genderArrow
        if (arrowY < 305) {
            arrowY = 305 + lineOffset;
        } else if (arrowY > 305 + lineOffset) {
            arrowY = 305;
        }

        BufferedImage genderArrow = ut.getBufferedImage("screens/gender_arrow.png");
        genderArrow = UtilityTool.scaleImage(genderArrow, (int) (genderArrow.getWidth() * 3.5),
                (int) (genderArrow.getHeight() * 3.5));

        int width = 250;
        int height = 144;
        int x = gp.screenWidth - width - gp.tileSize * 2;
        int y = 275;

        drawGenderWindow(g2, x, y, width, height);
        g2.setFont(pokemon_24);

        g2.setColor(textShadow);
        g2.drawString("BOY", x + 60 + 2, y + 60 + 2);
        g2.drawString("GIRL", x + 60 + 2, y + 100 + 2);

        g2.setColor(textColor);
        g2.drawString("BOY", x + 60, y + 60);
        g2.drawString("GIRL", x + 60, y + 100);

        g2.drawImage(genderArrow, arrowX, arrowY, null);
    }

    //method to set the player gender (unused)
    private void setPlayerGender() {
        if (arrowY == 305) {
            gp.player.gender = "male";
        } else if (arrowY == 305 + lineOffset) {
            gp.player.gender = "female";
        }
    }

    //method to draw the genderwindown in the gender selection state (unused)
    private void drawGenderWindow(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(white); // Black with transparency
        g2.fillRoundRect(x, y, width, height, 5, 5);

        g2.setColor(genderBoxOutline1);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x, y, width, height, 5, 5);

        g2.setColor(genderBoxOutline2);
        g2.setStroke(new BasicStroke(10));
        g2.drawRect(x + 6, y + 6, width - 12, height - 12);
    }

    //method to draw a fade in and fade out animation
    //takes as paramether the graphics engine and the complete fade duration
    public void fadeAnimation(Graphics2D g2, final int animationDuration) {
        if (isAnimationActive) { //when event this is set to true
            if (animationStartTime == 0) { //gets the start time
                animationStartTime = System.currentTimeMillis();
            }
            
            //calculates duration based n start time
            elapsedTime = System.currentTimeMillis() - animationStartTime;
            int transparency = 0; // trasparency variable to get the fade
    
            // time stages
            int time1 = animationDuration * 40 / 100; // State 1: 40% (fade in)
            int time2 = animationDuration * 13 / 100 + time1; // State 2: 13% (total black, used for events like tp)
            int time3 = animationDuration * 40 / 100 + time2; // State 3: 40% (fade out)
            // time 4 = 7% = time ti reset all proprieties and get a nice ui/ux
            
            //calculates what to do based oin which time state the animation is in
            if (elapsedTime <= animationDuration) {
                if (elapsedTime < time1) {
                    // State 1
                    transparency = (int) (255 * (elapsedTime / (double) time1));
                } else if (elapsedTime < time2) {
                    // Stadio 2
                    transparency = 255;
                } else if (elapsedTime < time3) {
                    // Stadio 4
                    transparency = (int) (255 * (1 - ((elapsedTime - time2) / (double) (time3 - time2))));
                } else {
                    // Stadio 4
                    transparency = 0;
                }
    
                // Round the transparency value to discrete intervals (avoid incopatible transparency)
                if (transparency > 0 && transparency < 255) {
                    transparency = (transparency / 16) * 16; // 16 and multiples
                }
    
                // Grants transparency bewteen 0 and 255 (avoid incopatible transparency)
                transparency = Math.max(0, Math.min(255, transparency));
    
                // sets a rectangle big as the screen size with color black and the transparecy.
                g2.setColor(new Color(0, 0, 0, transparency));
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            } else {
                // resets variables when animation is complete
                isAnimationActive = false;
                titleScreenState = 2; //(unused) (only used in title screen).
            }
        }
    }

    public void startAnimation() {
        this.animationStartTime = 0;
        this.isAnimationActive = true;
    }
    
    //method to draw the paused screen state
    //to be changed with option bar and player menu states
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

    //method to draw a generic message (think it's unused)
    //gets as parameter the message to draw
    public void drawMessage(Graphics2D g2, String message) { 

        int x = gp.tileSize;
        int y = (gp.screenHeight / 4) * 3;
        int width = gp.screenWidth - (gp.tileSize * 2);
        int height = (int) (gp.tileSize * 2.5);

        drawSubWindow(g2, x, y, width, height);

        g2.setFont(pokemon_48);

        int lastLineLenght = 0;
        for (String line : message.split("\n")) {
            lastLineLenght = (int) g2.getFontMetrics().getStringBounds(line, g2).getWidth();
            g2.setColor(textShadow);
            g2.drawString(line, x + 32 + 3, y + 64 + 3);
            g2.setColor(textColor);
            g2.drawString(line, x + 32, y + 64);
            y += 40;
        }

        dotHeight = y;
        if (!dialogueTimer.isRunning()) {
            g2.drawImage(down_arrow, lastLineLenght + 100, dotHeight + dotOffset, null);
        }
    }

    //mwthod to draw npc dialogue
    public void drawDialogueScreen(Graphics2D g2) {
        // continues the dialog on enter press
        if (gp.keyH.Apressed == true) {
            gp.mapM.maps.get(gp.currentMap).npc
                    .get(gp.player.npcIndex).speak();
            gp.keyH.Apressed = false;
        }

        //sets window position and dimensions
        int x = gp.tileSize;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 2);
        int height = gp.tileSize * 4;
        //draw window
        drawSubWindow(g2, x, y, width, height);

        //sets message color and size
        g2.setColor(textColor);
        g2.setFont(pokemon_16);

        int lastLineLenght = 0;
        //draws message and message's shadow
        for (String line : currentString.split("\n")) {
            lastLineLenght = (int) g2.getFontMetrics().getStringBounds(line, g2).getWidth();
            g2.setColor(textShadow);
            g2.drawString(line, x + 16 + 2, y + 32 + 2);
            g2.setColor(textColor);
            g2.drawString(line, x + 16, y + 32);
            y += 40;
        }

        //drawsn red dot
        dotHeight = y - 25;
        if (!dialogueTimer.isRunning()) {
            g2.drawImage(down_arrow, lastLineLenght + 70, dotHeight + dotOffset, null);
        }
    }

    //method to draw a generic subwindow
    //gets as parameter the x and y coordinates and window dimensions
    public void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        //draws white backround
        g2.setColor(white); // Black with transparency
        g2.fillRoundRect(x, y, width, height, 40, 50);
        //draws first outline
        g2.setColor(boxOutline1);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, width, height, 40, 50);
        //draws second outline
        g2.setColor(boxOutline2);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 4, y + 4, width - 8, height - 8, 32, 40);
    }

    // BATTLE STATE
    //method to draw the battle screen state
    public void drawBattleScreen(Graphics2D g2) {
        getBattleImagines(g2);
    }

    //mwthod to get the battle imagines such as backround, player and pokemo image ecc.
    // !!! INCOMPLETE. TO BE IMPLEMENTED FURTHER
    public void getBattleImagines(Graphics2D g2) {
        UtilityTool ui = new UtilityTool(); //instantiate utulity tool to get image
        BufferedImage image; //image

        // draws the backround image
        image = ui.getBufferedImage("screens/battle_forest.png");
        image = UtilityTool.scaleImage(image, image.getWidth() * 2, image.getHeight() * 2);
        g2.drawImage(image, -128, 0, image.getWidth(), image.getHeight(), null);
        // draws the pokemon image (to be implememted further)
        image = gp.mapM.maps.get(gp.currentMap).pokemons.get(gp.player.pokemonIndex).battleImage;
        g2.drawImage(image, 17 * gp.tileSize, 5 * gp.tileSize, image.getWidth(), image.getHeight(), null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String eventText = e.getActionCommand();
        if (eventText.equals("stop")) { //precondition to skip NULL events
            this.textActive = false;
            this.currentString = "";
            return;
        }
        if (e.getSource() instanceof OBJ_Sign) {
            this.currentDialog = eventText;
            this.textActive = true;
            this.dialogueTimer.start();
        }
    }
}
