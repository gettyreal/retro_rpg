package entity.npc;

import entity.NPC;
import main.GamePanel;

public class Nurse_Joy extends NPC{
    public Nurse_Joy(GamePanel gp) {
        super(gp, "Nurse Joy", "npc/nurse_joy.png", 0, 0);
        setDialogue();
    }

    @Override
    public void getEntityImage(String packageName) {
        loadImage(this.down, 0, packageName);
    }

    @Override
    public void setAction() {

    }

    public void setDialogue() {
        this.dialogues.add("Hello!");
        this.dialogues.add("Welcome to our Pokémon Center.");
        this.dialogues.add("We can heal your Pokémon to perfect health.");
        this.dialogues.add("Shall we heal your Pokémon?"); 
    }

    @Override
    public void speak() {
        super.speak();
    }
}
