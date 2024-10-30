package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // get WASD key input
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

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        // get WASD key input
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
    }

    public boolean checkMovement() {
        if (upPressed == true || downPressed == true ||
                leftPressed == true || rightPressed == true) {
            return true;
        } else {
            return false;
        }
    }

}
