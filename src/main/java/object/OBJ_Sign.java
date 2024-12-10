package object;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OBJ_Sign extends SuperObject{
    ActionListener listener;
    boolean closeMessage = false;
    //texts
    String dialogues;
    public int dialogueIndex = 0;

    public OBJ_Sign(String message, ActionListener listener) {
        this.dialogues = message;
        this.listener = listener;
    }

    public void printMessage() {
        ActionEvent event;
        if (listener == null) return;
        if (!closeMessage) {
            event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, dialogues);
            listener.actionPerformed(event);
            closeMessage = true;
        } else {
            event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "stop");
            listener.actionPerformed(event);
            closeMessage = false;
        }
    }
}
