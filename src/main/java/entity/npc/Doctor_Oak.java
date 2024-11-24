package entity.npc;

import entity.NPC;
import main.GamePanel;

public class Doctor_Oak extends NPC {
    public Doctor_Oak(GamePanel gp) {
        super(gp, "Dottor Oak", "npc/doctor_oak/doctor_oak_", 0, 0);
        setDialogue();
    }

    public void setDialogue() {
        this.dialogueIndex = 9;
        this.dialogues.add("Hello, there!\nGlad to meet you!");
        this.dialogues.add("Welcome to the world of POKèMON!");
        this.dialogues.add("My name is OAK.");
        this.dialogues.add("People affectionately refer to me\nas the POKèMON PROFESSOR.");
        this.dialogues.add("This world...");
        this.dialogues.add("...is inhabited far and wide by\ncreatures called POKèMON.");
        this.dialogues.add("For some people, POKèMON are pets.\nOthers use them for battling");
        this.dialogues.add("As for myself...");
        this.dialogues.add("I study POKèMON as a profession.");
        this.dialogues.add("But first, tell me a little about\nyourseft.");
        this.dialogues.add("Now tell me. Are you a boy?\nOr are you a girl?");
        //add boy or girl user input
        this.dialogues.add("Let's begin with your name.\nWhat is it?");
        //add your name screen
        this.dialogues.add("Right...\nSo your name is "+ gp.player.name+".");
        this.dialogues.add("This is my grandson.");
        this.dialogues.add("He's been your rival since you both\nboth were babies.");
        this.dialogues.add("...Er, what was his name now?");
        //add rival name selection input
        this.dialogues.add("..Er, was it GREEN?");
        this.dialogues.add("That's right! I remenber now!\nHis name is GREEN!");
        this.dialogues.add(gp.player.name+"!");
        this.dialogues.add("Your very own POKèMON legend is\nabout to unfold!");
        this.dialogues.add("A world of dreams and adventures\nwith POKèMON awaits! Let's go!");
        //add last animation.
    }

    @Override
    public void speak() {
        super.speak();
    }
}
