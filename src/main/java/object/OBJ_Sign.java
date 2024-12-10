package object;

import java.util.ArrayList;

public class OBJ_Sign extends SuperObject{
    //texts
    public ArrayList<String> dialogues;
    public int dialogueIndex = 0;

    public OBJ_Sign(String message) {
        this.dialogues = new ArrayList<>();
        addMessage(message);
    }

    public void addMessage(String message) {
        this.dialogues.add(message);
    }

    public void printMessage() {
        for (String string : dialogues) {
            System.out.println(string);
        }
    }
}
