package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    // screen setting
    final int originalTileSize = 16; // 64 x 64
    final double scale = 3; // moltiplicatore dei pixel
    public final int tileSize = (int) (originalTileSize * scale); // 64 x 64
    public final int maxScreenCol = 16; // 16 colonne
    public final int maxScreenRow = 12; // 12 rown quindi 4:3
    public final int screenWidth = tileSize * maxScreenCol; // 768px
    public final int screenHeight = tileSize * maxScreenRow; // 576px

    // fps
    int fps = 60;

    // world settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // game thread
    Thread gameThread; // for clock

    // game input and checkers
    KeyHandler keyH = new KeyHandler(); // get key input
    public AssetSetter aSetter = new AssetSetter(this); // instantiace obj and entities
    public UI userInterface = new UI(this); // instantiace user interface for messages
    public CollisionChecker Checker = new CollisionChecker(this);

    // game entities and objects
    public Player player = new Player(this, keyH); // instantiace player
    public ArrayList<Entity> pokemons = new ArrayList<>(); // pokemon entitiy arraylist
    public ArrayList<SuperObject> obj = new ArrayList<>(); // obj in the game

    // map layers
    public TileManager tileM1 = new TileManager(this, "tiles/tileset.png", "maps/tilemap_layer1.csv",
            "tiles/collisions.txt");
    public TileManager tileM2 = new TileManager(this, "tiles/tileset.png", "maps/tilemap_layer2.csv",
            "tiles/collisions.txt");
    public TileManager tileM3 = new TileManager(this, "tiles/tileset.png", "maps/tilemap_layer3.csv",
            "tiles/collisions.txt");
    public TileManager tileM4 = new TileManager(this, "tiles/tileset.png", "maps/tilemap_layer4.csv",
            "tiles/collisions.txt");

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() { // set default object before game starts
        aSetter.setPokemons();
        aSetter.setObject();
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
                    remainingTime = 0; // makes thread not sleep.
                }
                Thread.sleep((long) remainingTime); // pase the game loop
                nextDrawTime += drawInterval; // set new drawtime
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        // player update
        player.update();
        // pokemons update
        for(int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i) != null) {
                pokemons.get(i).update();
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileM1.draw(g2);
        tileM2.draw(g2);

        // obj drawing
        for (int i = 0; i < obj.size(); i++) {
            if (obj.get(i) != null) {
                obj.get(i).draw(g2, this);
            }
        }

        // pokemon drawing
        for (int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i) != null) {
                pokemons.get(i).draw(g2);
            }
        }

        player.draw(g2); // draw player

        tileM3.draw(g2);
        tileM4.draw(g2);

        userInterface.drawMessage(g2); // draws messages

        g2.dispose();
    }

}
