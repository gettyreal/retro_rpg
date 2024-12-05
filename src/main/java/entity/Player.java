package entity;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Door;
import object.OBJ_PokeChest;
import object.SuperObject;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    public KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int objIndex = 999; // set obj null value.
    public int pokemonIndex = 999;
    public int npcIndex = 999;

    public String gender = "";

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        setDefaultValues(); // set spawn coordinates
        
        getEntityImage("player/player_");
        getEntityBushImage("player/bush_");

        Xoffset = 8;
        Yoffset = 32;
        this.collisionArea = new Rectangle(Xoffset, Yoffset, 20, 20);
        solidAreaDefaultX = collisionArea.x;
        solidAreaDefaultY = collisionArea.y;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
    }

    public void setDefaultValues() {
        this.name = "GETTYREAL";
        this.worldX = 93 * gp.tileSize;
        this.worldY = 65 * gp.tileSize;
        this.speed = 2;
        this.direction = "up";
    }

    @Override
    public void update() {
        if (keyH.checkMovement()) {
            // move the player by playerSpeed
            if (keyH.upPressed == true) {
                this.direction = "up";
            } else if (keyH.downPressed == true) {
                this.direction = "down";
            } else if (keyH.leftPressed == true) {
                this.direction = "left";
            } else if (keyH.rightPressed == true) {
                this.direction = "right";
            }

            // check collision on all layers
            collisionOn = false;
            checkTileCollision();

            // check if player is in bush
            // debug System.out.println(gp.tileM2.cChecker.checkInBush(this));
            //bushIn = gp.mapM.maps.get(gp.currentMap).layers.get(1).cChecker.checkInBush(this); // used 2nd layer because bushes are on 2nd layer.

            // checks pokemon collision
            pokemonIndex = gp.Checker.checkEntity(this, gp.mapM.maps.get(gp.currentMap).pokemons);

            //checks entity collision
            npcIndex = gp.Checker.checkEntity(this, gp.mapM.maps.get(gp.currentMap).npc);

            // check objcets collison + gets the value of the obj colliding
            objIndex = gp.Checker.checkObject(this, true);

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
        //events on collision
        interactNPC(npcIndex);
        interactPokemon(pokemonIndex);
        interactObject(objIndex); // removes the obj on e key press if possible
    }

    public void interactObject(int index) {
        if (index != 999) { // index == 999 null object.
            if (keyH.Epressed == true) {
                if (gp.mapM.maps.get(gp.currentMap).obj.get(index) instanceof OBJ_PokeChest) {
                    gp.mapM.maps.get(gp.currentMap).obj.get(index).loadImage("object/poke-chest-open.png"); // changes imagine after opening chest
                    OBJ_PokeChest pokeChest = (OBJ_PokeChest) gp.mapM.maps.get(gp.currentMap).obj.get(index); // down cast to modify opened boolean
                    pokeChest.opened = true; // put pokechst opened on true after user input
                }

                // it needs to be the last cause objindex--;
                if (gp.mapM.maps.get(gp.currentMap).obj.get(index).pickable == true) {
                    gp.mapM.maps.get(gp.currentMap).aSetter.removeObject(index);
                    objIndex--; // prevents index out bounds exeption
                }

                if (gp.mapM.maps.get(gp.currentMap).obj.get(index) instanceof OBJ_Door) {
                    interactDoor(gp.mapM.maps.get(gp.currentMap).obj.get(index));
                }

                keyH.Epressed = false; // resets the key
            }
            // if (gp.mapM.maps.get(gp.currentMap).obj.get(index) instanceof OBJ_nurseDialogue) {
            //     npcIndex = 1;
            //     if (keyH.Fpressed == true) {
            //         interactNPC(npcIndex);
            //         keyH.Fpressed = false;
            //     }
            // }
        }
    }

    public void interactPokemon(int index) {
        if (index != 999) {
            gp.gameState = gp.battleState;
        }
    }

    public void interactDoor(SuperObject door) {
        if (door.actionCode.equals("toPokecentre")) {
            gp.currentMap = 1;
            setEntityWorldPosition(25, 27);
        }
        if (door.actionCode.equals("toHome")) {
            gp.currentMap = 2;
            setEntityWorldPosition(13, 28);
        }
        if (door.actionCode.equalsIgnoreCase("fromPokecentre")) {
            gp.currentMap = 0;
            setEntityWorldPosition(7, 9);
        }
        if (door.actionCode.equalsIgnoreCase("fromHome")) {
            gp.currentMap = 0;
            setEntityWorldPosition(18, 11);
        }
        if (door.actionCode.equalsIgnoreCase("toSecondFloor")) {
            upAnimation();
        }
        if (door.actionCode.equalsIgnoreCase("fromSecondFloor")) {
            downAnimation();
        }
        
    }

    //method to start npc interaction
    //set gamestate to dialog state and instaantiace the first dialog
    private void interactNPC(int index) {
        if (index != 999) { 
            if (keyH.Fpressed == true) {
                gp.gameState = gp.dialogState;
                gp.mapM.maps.get(gp.currentMap).npc.get(index).speak();
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage Image = null;

        // gets player srites
        if (!bushIn) { // if not in bush gets player normal sprites
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
        } else { // if in bush gets player bush sprites
            switch (this.direction) {
                case "up":
                    Image = this.bushUp[spriteNumber];
                    break;
                case "down":
                    Image = this.bushDown[spriteNumber];
                    break;
                case "left":
                    Image = this.bushLeft[spriteNumber];
                    break;
                case "right":
                    Image = this.bushRight[spriteNumber];
                    break;
            }
        }
        // draws current player sprite
        g2.drawImage(Image, screenX, screenY, null);
        g2.drawRect(screenX + Xoffset, screenY + Yoffset, collisionArea.width, collisionArea.height); //debug for visualising hitbox

    }
}
