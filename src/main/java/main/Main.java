package main;

import javax.swing.JFrame;

//MAIN CLASS
public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame(); //instantiate new frame
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //sets default operation
        window.setResizable(false); //set false to resisable window
        window.setTitle("POKEMON FREED EDITION"); //sets title

        //add gamePanel alla finestra
        GamePanel gamePanel = new GamePanel(); //instantiate panel
        window.add(gamePanel); 
        window.pack(); // sets frame to the dimensions of the panel

        window.setLocationRelativeTo(null); //set frame to midscreen
        window.setVisible(true); //set visible
        
        gamePanel.setupGame(); //setup del game

        gamePanel.startGameThread(); //start del game
    }
}
