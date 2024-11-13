package entity.npc;

import entity.NPC;
import main.GamePanel;

public class Doctor extends NPC{
    public Doctor(GamePanel gp) {
        super(gp, "prof Oak", "npc/doctor.png", 0, 0);
    }

    @Override
    public void getEntityImage(String packageName) {
        loadImage(this.down, 0, packageName);
    }

    @Override
    public void setAction() {

    }
}
