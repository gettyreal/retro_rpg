package entity;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Door;
import object.OBJ_PokeChest;
import object.OBJ_Sign;
import object.OBJ_Stairs;
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

        getEntityImage("characters/player/player_");
        getEntityBushImage("characters/player/bush_");

        Xoffset = 2;
        Yoffset = 2;
        this.collisionArea = new Rectangle(Xoffset, Yoffset, 60, 60);
        solidAreaDefaultX = collisionArea.x;
        solidAreaDefaultY = collisionArea.y;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
    }

    public void setDefaultValues() {
        this.name = "GETTYREAL";
        this.worldX = 25 * gp.tileSize;
        this.worldY = 50 * gp.tileSize;
        this.speed = 4;
        this.direction = "down";
    }

    @Override
    public void update() {
        if (movingDisabled) {
            return;
        }
        if (moving == false) {
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

                moving = true;

                // check collision on all layers
                collisionOn = false;
                checkTileCollision();

                // check objcets collison + gets the value of the obj colliding
                objIndex = gp.Checker.checkObject(this, true);

                // check if player is in bush
                bushIn = gp.mapM.maps.get(gp.currentMap).layers.get(0).cChecker.checkInBush(this);
                // used 1nd layer because bushes are on 1nd layer.
            }
        }

        if (moving == true) {
            // checks pokemon collision
            pokemonIndex = gp.Checker.checkEntity(this, gp.mapM.maps.get(gp.currentMap).pokemons);

            // checks entity collision
            npcIndex = gp.Checker.checkEntity(this, gp.mapM.maps.get(gp.currentMap).npc);
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
            if (spriteCounter == 8) { // ogni 8 update cambia sprite
                if (spriteNumber == 3) { // se le sprite sono finite l'animazione riparte
                    spriteNumber = 0;
                } else {
                    spriteNumber++; // aumenta la sprite
                }
                spriteCounter = 0;
            }

            pixelCounter += this.speed;
            if (pixelCounter == walkDuration) {
                moving = false;
                pixelCounter = 0;
            }

            this.walkCounter++;
            if (walkCounter < 8) {
                imageOffset = 24;
            } else if (walkCounter < 16) {
                imageOffset = 22;
            } else
                walkCounter = 0;
        }

        // events on collision
        interactNPC(npcIndex);
        interactPokemon(pokemonIndex);
        interactObject(objIndex); // removes the obj on e key press if possible
    }

    public void interactObject(int index) {
        if (index != 999) { // index == 999 null object.
            if (keyH.Apressed == true) {
                if (gp.mapM.maps.get(gp.currentMap).obj.get(index) instanceof OBJ_PokeChest) {
                    gp.mapM.maps.get(gp.currentMap).obj.get(index).loadImage("object/poke-chest-open.png");
                    OBJ_PokeChest pokeChest = (OBJ_PokeChest) gp.mapM.maps.get(gp.currentMap).obj.get(index);
                    pokeChest.opened = true; // put pokechst opened on true after user input
                }

                // it needs to be the last cause objindex--;
                if (gp.mapM.maps.get(gp.currentMap).obj.get(index).pickable == true) {
                    gp.mapM.maps.get(gp.currentMap).aSetter.removeObject(index);
                    objIndex--; // prevents index out bounds exeption
                }

                if (gp.mapM.maps.get(gp.currentMap).obj.get(index) instanceof OBJ_Sign) {
                    OBJ_Sign sign = (OBJ_Sign) gp.mapM.maps.get(gp.currentMap).obj.get(index);
                    sign.printMessage();
                }

                keyH.Apressed = false; // resets the key
            }
            if (keyH.upPressed == true) {
                if (gp.mapM.maps.get(gp.currentMap).obj.get(index) instanceof OBJ_Door) {
                    interactDoorUp(gp.mapM.maps.get(gp.currentMap).obj.get(index));
                }
            }
            if (keyH.downPressed == true) {
                if (gp.mapM.maps.get(gp.currentMap).obj.get(index) instanceof OBJ_Door) {
                    interactDoorDown(gp.mapM.maps.get(gp.currentMap).obj.get(index));
                }
            }
            if (keyH.leftPressed == true) {
                if (gp.mapM.maps.get(gp.currentMap).obj.get(index) instanceof OBJ_Stairs) {
                    interactStairsDown(gp.mapM.maps.get(gp.currentMap).obj.get(index));
                }
            }
            if (keyH.rightPressed == true) {
                if (gp.mapM.maps.get(gp.currentMap).obj.get(index) instanceof OBJ_Stairs) {
                    interactStairsUp(gp.mapM.maps.get(gp.currentMap).obj.get(index));
                }
            }
            // if (gp.mapM.maps.get(gp.currentMap).obj.get(index) instanceof
            // OBJ_nurseDialogue) {
            // npcIndex = 1;
            // if (keyH.Fpressed == true) {
            // interactNPC(npcIndex);
            // keyH.Fpressed = false;
            // }
            // }
        }
    }

    public void interactPokemon(int index) {
        if (index != 999) {
            gp.gameState = gp.battleState;
        }
    }

    public void interactDoorUp(SuperObject Door) {
        OBJ_Door door = (OBJ_Door) Door;
        if (door.actionCode.equals("toPlayerHouse")) {
            door.setTransferCoordinates(2, 4, 8);
            door.openAnimation();
        }
        if (door.actionCode.equalsIgnoreCase("toBirchLab")) {
            door.setTransferCoordinates(1, 7, 12);
            door.openAnimation();
        }
    }

    public void interactDoorDown(SuperObject Door) {
        OBJ_Door door = (OBJ_Door) Door;
        if (door.actionCode.equalsIgnoreCase("fromPlayerHouse")) {
            door.setTransferCoordinates(0, 19, 48);
            door.closeAnimation();
        }
        if (door.actionCode.equalsIgnoreCase("fromBirchLab")) {
            door.setTransferCoordinates(0, 22, 56);
            door.closeAnimation();
        }
    }

    public void interactStairsUp(SuperObject stair) {
        if (stair.actionCode.equalsIgnoreCase("toSecondFloor")) {
            upAnimation();
        }
    }

    public void interactStairsDown(SuperObject stair) {
        if (stair.actionCode.equalsIgnoreCase("fromSecondFloor")) {
            downAnimation();
        }
    }

    // method to start npc interaction
    // set gamestate to dialog state and instaantiace the first dialog
    private void interactNPC(int index) {
        if (index != 999) {
            if (keyH.Apressed == true) {
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
                    Image = this.up[spriteNumber].body;
                    lastSprite = this.up[spriteNumber].body;
                    break;
                case "down":
                    Image = this.down[spriteNumber].body;
                    lastSprite = this.down[spriteNumber].body;
                    break;
                case "left":
                    Image = this.left[spriteNumber].body;
                    lastSprite = this.left[spriteNumber].body;
                    break;
                case "right":
                    Image = this.right[spriteNumber].body;
                    lastSprite = this.right[spriteNumber].body;
                    break;
            }
        } else { // if in bush gets player bush sprites
            switch (this.direction) {
                case "up":
                    Image = this.bushUp[spriteNumber].body;
                    break;
                case "down":
                    Image = this.bushDown[spriteNumber].body;
                    break;
                case "left":
                    Image = this.bushLeft[spriteNumber].body;
                    break;
                case "right":
                    Image = this.bushRight[spriteNumber].body;
                    break;
            }
        }
        // draws current player sprite
        g2.drawImage(Image, screenX + 4, screenY - imageOffset, null);
        g2.drawRect(screenX + Xoffset, screenY + Yoffset, collisionArea.width, collisionArea.height); // hitbox

    }
}
