package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, Epressed, Fpressed, enterPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        if (gp.gameState == gp.titleState) {
            if (gp.userInterface.titleScreenState == 0) {
                gp.userInterface.titleScreenState = 1;
            }
            if (gp.userInterface.titleScreenState == 2) {
                if (code == KeyEvent.VK_ENTER && !gp.userInterface.dialogueTimer.isRunning()) {
                    refreshDialogue();
                }
            }
        } else if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.pauseState;
            }
            if (code == KeyEvent.VK_W) {
                upPressed = true; // key is pressed
            }
            if (code == KeyEvent.VK_S) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_E) {
                Epressed = true;
            }
            if (code == KeyEvent.VK_F) {
                Fpressed = true;
            }
        } else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.playState;
            }
        } else if (gp.gameState == gp.dialogState) {
            if (code == KeyEvent.VK_ENTER) {
                refreshDialogue();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = false; // key is released
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
        if (code == KeyEvent.VK_E) {
            Epressed = false;
        }
        if (code == KeyEvent.VK_F) {
            Fpressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
    }

    public boolean checkMovement() {
        if (upPressed == true || downPressed == true ||
                leftPressed == true || rightPressed == true) {
            return true;
        } else {
            return false;
        }
    }

    public void refreshDialogue() {
        enterPressed = true;
        gp.userInterface.currentString = "";
        gp.userInterface.dialogueTimer.start();
    }
}
