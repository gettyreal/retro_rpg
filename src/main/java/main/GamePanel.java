package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import entity.Player;
import map.MapManager;

//MAIN CLASS OF THE GAME
//handles the only panel in the game and the run thread to update 60 times per seconds
public class GamePanel extends JPanel implements Runnable {
    // screen setting
    final int originalTileSize = 16; // 64 x 64
    final double scale = 4; // moltiplicatore dei pixel
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
    public KeyHandler keyH = new KeyHandler(this); // get key input
    public Player player = new Player(this, keyH); // instantiace player
    public UI userInterface = new UI(this); // instantiace user interface for messages
    public CollisionChecker Checker = new CollisionChecker(this);

    // maps
    public MapManager mapM = new MapManager(this);
    public int currentMap = 0;

    // game status
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int battleState = 4;

    //constructor
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    //method to call the game setup
    //called in main method
    public void setupGame() { // set default object before game starts
        mapSetup(0); //world map 
        mapSetup(1); //birch map
        mapSetup(2); //players house
        gameState = playState;
    }

    //method to get single map setup
    //SETUPS pokenon, object, and npc's
    private void mapSetup(int mapIndex) {
        mapM.maps.get(mapIndex).aSetter.setPokemons(mapIndex);
        mapM.maps.get(mapIndex).aSetter.setObject(mapIndex);
        mapM.maps.get(mapIndex).aSetter.setNPC(mapIndex);
    }

    //method to start the game
    //called in main method
    public void startGameThread() {
        gameThread = new Thread(this); // gamepanel passed to thread
        gameThread.start();
    }

    //override run method
    //updates every 60fps
    //2 events: update and repaint
    @Override
    public void run() { // game loop = core of the game using sleep method
        double drawInterval = 1000000000 / fps; // 1second : fps = 60fps
        double nextDrawTime = System.nanoTime() + drawInterval; // next fps
        long lastTime = System.nanoTime();
        int frameCount = 0;
    
        while (gameThread != null) { // repeat of the process
            update(); // 1) update information
            repaint(); // call paintComponent , 2) draw on the screen information
    
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000; // convert nano in milli.
                if (remainingTime < 0) {
                    remainingTime = 0; // makes thread not sleep.
                }
                Thread.sleep((long) remainingTime); // pause the game loop
                nextDrawTime += drawInterval; // set new drawtime
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            frameCount++;
    
            // Check if one second has passed
            long currentTime = System.nanoTime();
            if (currentTime - lastTime >= 1000000000) { // 1 second = 1,000,000,000 nanoseconds
                System.out.println("FPS: " + frameCount);
                frameCount = 0; // Reset frame count
                lastTime = currentTime; // Reset timer
            }
        }
    }
    

    //method called every fps, updates the game
    public void update() {
        if (gameState == playState) { // update only when game is on play state.
            // player update
            player.update(); //updates the player
            // pokemons update
            for (int i = 0; i < mapM.maps.get(currentMap).pokemons.size(); i++) {
                if (mapM.maps.get(currentMap).pokemons.get(i) != null) { //updates the pokemon
                    //mapM.maps.get(currentMap).pokemons.get(i).update();
                }
            }

            for (int i = 0; i < mapM.maps.get(currentMap).npc.size(); i++) { //updates the pokemon
                if (mapM.maps.get(currentMap).npc.get(i) != null) {
                    mapM.maps.get(currentMap).npc.get(i).update();
                }
            }
        }
    }

    //method to draw on the panel screen 
    //called on update method
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //System.out.println(player.worldX / tileSize + " | " + player.worldY / tileSize);// player position debug
        switch (gameState) { //checks gamestate
            case titleState:
                userInterface.draw(g2); //draw title screen
                break;
            case playState:
                paintPlayState(g2); //draw playstate screen
                break;
            case pauseState:
                paintPlayState(g2); //draw playstate + pause screen in user interface
                break;
            case dialogState:
                paintPlayState(g2); //draw play state + dialogues in user interface
                break;
            case battleState:
                paintBattleState(g2); //draw battle state
                break;
        }

        g2.dispose();
    }

    //method to draw playState
    //draws map layers, player, obj and entities (pokemon, and npcs)
    private void paintPlayState(Graphics2D g2) {
        mapM.maps.get(currentMap).layers.get(0).draw(g2); //draws first layer
        mapM.maps.get(currentMap).layers.get(1).draw(g2); //draws second layer

        // obj drawing
        for (int i = 0; i < mapM.maps.get(currentMap).obj.size(); i++) {
            if (mapM.maps.get(currentMap).obj.get(i) != null) {
                mapM.maps.get(currentMap).obj.get(i).draw(g2, this);
            }
        }

        // pokemon drawing
        for (int i = 0; i < mapM.maps.get(currentMap).pokemons.size(); i++) {
            if (mapM.maps.get(currentMap).pokemons.get(i) != null) {
                mapM.maps.get(currentMap).pokemons.get(i).draw(g2);
            }
        }

        // npc drawing
        for (int i = 0; i < mapM.maps.get(currentMap).npc.size(); i++) {
            if (mapM.maps.get(currentMap).npc.get(i) != null) {
                mapM.maps.get(currentMap).npc.get(i).draw(g2);
            }
        }

        player.draw(g2); // draw player

        for (int i = 0; i < mapM.maps.get(currentMap).npc.size(); i++) {
            if (mapM.maps.get(currentMap).npc.get(i) != null) {
                mapM.maps.get(currentMap).npc.get(i).drawHead(g2);
            }
        }

        mapM.maps.get(currentMap).layers.get(2).draw(g2); //draws third layer
        userInterface.draw(g2); // draws ui messages
    }

    private void paintBattleState(Graphics2D g2) {
        userInterface.draw(g2); // draws battle screens.
    }
}
