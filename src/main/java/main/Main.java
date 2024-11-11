package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("RetroRPG");

        GamePanel gamePanel = new GamePanel(); //add gamePanel alla finestra
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null); 
        window.setVisible(true);
        
        gamePanel.setupGame(); //setup del game

        gamePanel.startGameThread(); //start del game
    }
}
