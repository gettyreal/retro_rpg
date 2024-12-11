package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;
import tile.TileManager;

//super class per player mostri npc entita' ecc.

public abstract class Entity {
    public String name;

    public GamePanel gp;

    public int worldX, worldY; // entity spawn coordinates
    public int speed; // entity speed

    // movement animation

    public BufferedImage[] up = new BufferedImage[4];
    public BufferedImage[] down = new BufferedImage[4];
    public BufferedImage[] left = new BufferedImage[4];
    public BufferedImage[] right = new BufferedImage[4];

    public BufferedImage[] bushUp = new BufferedImage[4];
    public BufferedImage[] bushDown = new BufferedImage[4];
    public BufferedImage[] bushLeft = new BufferedImage[4];
    public BufferedImage[] bushRight = new BufferedImage[4];

    public BufferedImage lastSprite;
    public BufferedImage battleImage;

    public String direction; // direction of the player

    public int spriteCounter = 0; // index of sprite active on screen
    public int spriteNumber = 0;

    public boolean movingDisabled = false;
    boolean moving = false;
    int pixelCounter = 0;
    public int walkDuration;
    //entity animation
    ActionListener at;
    int animationDuration = 0;
    Timer directionDownTimer;
    Timer directionUpTimer;
    public boolean SpriteAnimationOn = false;

    // collisions
    public Rectangle collisionArea;
    public int Xoffset, Yoffset;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean bushIn = false;

    // for update interval
    public int updateLockCounter = 0;

    // entity dialogues
    public ArrayList<String> dialogues = new ArrayList<>();
    public int dialogueIndex = 0;

    public Entity(GamePanel gp) {
        this.gp = gp;
        loadTimers();
        walkDuration = gp.tileSize;
    }

    private void loadTimers() {
        directionDownTimer = new Timer(20, event -> {
            this.worldX--;
            if (animationDuration % 2 == 0) this.worldY++;
            animationDuration++;
            if (animationDuration == 40) {
                directionDownTimer.stop();
                this.walkDuration = gp.tileSize;
                setEntityWorldPosition(10, 2);
            }
        });

        directionUpTimer = new Timer(20, event -> {
            this.worldX++;
            if (animationDuration % 2 == 0) this.worldY--;
            animationDuration++;
            if (animationDuration == 40) {
                directionUpTimer.stop();
                this.walkDuration = gp.tileSize;
                setEntityWorldPosition(36, 2);
            }
        });
    }

    // gets the entity image in the resources package
    // receive the package path intended as package + filename equal for all
    // imagines
    public void getEntityImage(String packagePath) {
        int indexFile = 0;
        int indexArray = 0;
        for (int i = 0; i < 16; i++) {
            if (indexArray == 4) {
                indexArray = 0;
            }
            // loads all player imagines
            String fileName = packagePath + indexFile + ".png";
            if (i < 4) {
                loadImage(this.down, indexArray, fileName);
            } else if (i < 8) {
                loadImage(this.up, indexArray, fileName);
            } else if (i < 12) {
                loadImage(this.left, indexArray, fileName);
            } else {
                loadImage(this.right, indexArray, fileName);
            }
            indexFile++;
            indexArray++;
        }
    }

    public void getEntityBushImage(String packagePath) {
        int indexFile = 0;
        int indexArray = 0;
        for (int i = 0; i < 16; i++) {
            if (indexArray == 4) {
                indexArray = 0;
            }
            // loads all player imagines
            String bushName = packagePath + indexFile + ".png";
            if (i < 4) {
                loadImage(this.bushDown, indexArray, bushName);
            } else if (i < 8) {
                loadImage(this.bushUp, indexArray, bushName);
            } else if (i < 12) {
                loadImage(this.bushLeft, indexArray, bushName);
            } else {
                loadImage(this.bushRight, indexArray, bushName);
            }
            indexFile++;
            indexArray++;
        }
    }

    public void loadImage(BufferedImage[] array, int indexArray, String fileName) {
        try {
            array[indexArray] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
            array[indexArray] = UtilityTool.scaleImage(array[indexArray],
            array[indexArray].getWidth() * 2, array[indexArray].getHeight() * 2); // scale img
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // sets default behavior of entity class
    // each single entity behaves differently so nothing is inside
    public void setAction() {

        updateLockCounter++;
        Random random = new Random();
        int randomTime = random.nextInt(240) + 180; // Intervallo di 2-3 secondi circa

        if (updateLockCounter > randomTime) {
            int i = random.nextInt(100) + 1; // numero casuale tra 1 e 100

            if (i <= 70) {
                direction = "idle";
            } else if (i > 70 && i <= 80) {
                direction = "up";
            } else if (i > 80 && i <= 90) {
                direction = "down";
            } else if (i > 90 && i <= 95) {
                direction = "left";
            } else {
                direction = "right";
            }

            updateLockCounter = 0;
        }
    }

    public void setEntityWorldPosition(int row, int column) {
        this.worldX = row * gp.tileSize;
        this.worldY = column * gp.tileSize;
    }

    // updates the entity
    // set default update cicle of entiy
    // much the same for all entities exept for player
    public void update() {
        if (movingDisabled) return;
        setAction();

        // checks entity collision
        collisionOn = false;
        checkTileCollision();

        // checks obj collision
        gp.Checker.checkObject(this, false);

        // checks player collision
        gp.Checker.checkPlayer(this);

        // move the entity
        if (collisionOn == false) {
            switch (direction) {
                case "up":
                    this.worldY -= this.speed;
                    break;
                case "down":
                    this.worldY += this.speed;
                    break;
                case "left":
                    this.worldX -= this.speed;
                    break;
                case "right":
                    this.worldX += this.speed;
                    break;
                default:
                    break;
            }
        }

        // updates the sprite of the entity
        this.spriteCounter++;
        if (spriteCounter > 10) { // ogni 10 update cambia sprite
            if (spriteNumber == 3) { // se le sprite sono finite l'animazione riparte
                spriteNumber = 0;
            } else {
                spriteNumber++; // aumenta la sprite
            }
            spriteCounter = 0;
        }
    }

    public void checkTileCollision() {
        for (TileManager tileM : gp.mapM.maps.get(gp.currentMap).layers) {
            tileM.cChecker.checkTile(this);
        }
    }

    // method to reload the currentdialog to continue the dialog.
    public void speak() {
        if (dialogueIndex == dialogues.size()) {
            gp.gameState = gp.playState;
            dialogueIndex = dialogues.size() - 1;
        } else {
            gp.userInterface.currentDialog = dialogues.get(dialogueIndex);
            dialogueIndex++;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage Image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (this.direction) {
                case "idle":
                    Image = lastSprite;
                    spriteCounter--; // stays on the same image
                    break;
                case "up":
                    Image = this.up[spriteNumber];
                    lastSprite = this.up[spriteNumber];
                    break;
                case "down":
                    Image = this.down[spriteNumber];
                    lastSprite = this.down[spriteNumber];
                    break;
                case "left":
                    Image = this.left[spriteNumber];
                    lastSprite = this.left[spriteNumber];
                    break;
                case "right":
                    Image = this.right[spriteNumber];
                    lastSprite = this.right[spriteNumber];
                    break;
            }
            if (Image != null) {
                g2.drawImage(Image, screenX, screenY, Image.getWidth(), Image.getHeight(), null);
            } else System.out.println("null image");
            g2.drawRect(screenX + Xoffset, screenY + Yoffset, collisionArea.width, collisionArea.height); // hitbox
        }
    }

    public void downAnimation() {
        this.walkDuration = gp.tileSize * 2;
        this.direction = "left";
        gp.userInterface.startAnimation();
        animationDuration = 0;
        directionDownTimer.start();
    }

    public void upAnimation() {
        this.walkDuration = gp.tileSize * 2;
        this.direction = "right";
        gp.userInterface.startAnimation();
        animationDuration = 0;
        directionUpTimer.start();
    }
}
