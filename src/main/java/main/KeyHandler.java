package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//KEYhandles class to handle key imput
//only handles that serves because keyboard is only game imput
public class KeyHandler implements KeyListener {
    GamePanel gp; //gamepanel
    public boolean upPressed, downPressed, leftPressed, rightPressed; //direction variables
    public boolean Apressed; //confirm variables

    //constructor
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    //override keytiped method (not used)
    @Override
    public void keyTyped(KeyEvent e) {
    }

    //override keypressed method
    //used to get keyboard input
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode(); //get keyboard event code

        //checks in which gamestate game currently is
        if (gp.gameState == gp.titleState) { // title state
            if (gp.userInterface.titleScreenState == 0) { //auto skips with any input
                gp.userInterface.titleScreenState = 1;
            }
            if (gp.userInterface.titleScreenState == 2) { //birch dialogue state
                if (gp.userInterface.dialogueTimer.isRunning()) { //does't do anything if progressive dialogue is going
                    return;
                }
                if (code == KeyEvent.VK_L) { //when a pressed reloads dialogue
                    refreshDialogue();
                }
                if (code == KeyEvent.VK_UP) { //arrow up
                    gp.userInterface.arrowY -= gp.userInterface.lineOffset;
                }
                if (code == KeyEvent.VK_DOWN) {//arrow down
                    gp.userInterface.arrowY += gp.userInterface.lineOffset;
                }
            }
        } else if (gp.gameState == gp.playState) { //play state
            if (code == KeyEvent.VK_ESCAPE) { //gets pause screen
                gp.gameState = gp.pauseState;
            }
            if (code == KeyEvent.VK_W) { //player up
                upPressed = true;
            }
            if (code == KeyEvent.VK_S) { //player down
                downPressed = true;
            }
            if (code == KeyEvent.VK_A) { //player left
                leftPressed = true;
            }
            if (code == KeyEvent.VK_D) { //player right
                rightPressed = true;
            }
            if (code == KeyEvent.VK_L) { //interact with obj and npc's
                Apressed = true;
            }
        } else if (gp.gameState == gp.pauseState) { //paused state
            if (code == KeyEvent.VK_ESCAPE) { //exti on pause state
                gp.gameState = gp.playState;
            }
        } else if (gp.gameState == gp.dialogState) { //dialogue state
            if (code == KeyEvent.VK_L) { //refresh dialogue on Apressed
                refreshDialogue();
            }
        }
    }

    //override keypressed method
    //used to resets keyboard input
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_L) {
            Apressed = false;
        }
    }

    //method to check if movement is appening
    public boolean checkMovement() {
        if (gp.player.SpriteAnimationOn) {
            return true;
        }
        if (upPressed == true || downPressed == true ||
                leftPressed == true || rightPressed == true) {
            return true;
        } else {
            return false;
        }
    }

    //method to update npc dialogue
    public void refreshDialogue() {
        Apressed = true;
        gp.userInterface.currentString = "";
        gp.userInterface.dialogueTimer.start();
    }
}
