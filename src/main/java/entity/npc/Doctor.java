package entity.npc;

import entity.NPC;
import main.GamePanel;

public class Doctor extends NPC {
    public Doctor(GamePanel gp) {
        super(gp, "prof Oak", "npc/doctor.png", 0, 0);
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
        this.dialogues.add("Hello, there!");
        this.dialogues.add("Glad to meet you!");
        this.dialogues.add("Welcome to the world of Pokemon!");
        this.dialogues.add("My name is oak.");
        this.dialogues.add("People affectionately refer to me as the \nPokemon professor.");
        this.dialogues.add("This world is inhabited far and wide by creatures \ncalled Pokemon.");
        this.dialogues.add("For some people, Pokemon are pets. \nOthers use them for battling.");
        this.dialogues.add("As for myself, I study Pokemon as a profession.");
        this.dialogues.add("But first, tell me a little about yourself.");
        this.dialogues.add("Now tell me. Are you a boy? Or are you a girl?");
        this.dialogues.add("Let’s begin with your name. What is it?");
        this.dialogues.add("Right…");
        this.dialogues.add("So your name is ...");
        this.dialogues.add("This is my grandson.");
        this.dialogues.add("He’s been your rival since you both were babies.");
        this.dialogues.add("Erm, what was his name again?");
        this.dialogues.add("Er, was it .....?");
        this.dialogues.add("That’s right! I remember now! His name is .....!");
        this.dialogues.add("Gettyreal!");
        this.dialogues.add("Your very own Pokemon legend is about to \nunfold!");
        this.dialogues.add("A world of dream and adventures with \nPokemon Awaits! \nLet’s go!");
    }

    @Override
    public void speak() {
        super.speak();
    }
}
