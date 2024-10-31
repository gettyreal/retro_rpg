package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;

public class GamePanel extends JPanel implements Runnable {
    // screen setting

    final int originalTileSize = 64; // 64 x 64
    final double scale = 1; // moltiplicatore dei pixel
    public final int tileSize = (int)(originalTileSize * scale); // 64 x 64
    final int maxScreenCol = 16; // 16 colonne
    final int maxScreenRow = 12; // 12 rown quindi 4:3
    final int screenWidth = tileSize * maxScreenCol; // 768px
    final int screenHeight = tileSize * maxScreenRow; // 576px
    int fps = 60;

    KeyHandler keyH = new KeyHandler(); // get key input
    Thread gameThread; // thread per clock
    Player player = new Player(this, keyH); //instanzia player

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this); // gamepanel passed to thread
        gameThread.start();
    }

    @Override
    public void run() { // game loop = core of the game using sleep method
        double drawInterval = 1000000000 / fps; // 1second : fps = 60fps
        double nextDrawTime = System.nanoTime() + drawInterval; // next fps

        while (gameThread != null) { // repeat of the process
            update(); // 1) update information
            repaint(); // call paintComponent , 2) draw on the screen information

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000; // convert nano in milli.
                if (remainingTime < 0) {
                    remainingTime = 0; //makes thread not sleep.
                }
                Thread.sleep((long) remainingTime); // pase the game loop
                nextDrawTime += drawInterval; //set new drawtime
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update(); // update player
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        player.draw(g2); //draw player

        g2.dispose();
    }

}
