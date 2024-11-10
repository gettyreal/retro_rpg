package main;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;

public class UI {
    GamePanel gp;
    Font font;

    public UI(GamePanel gp) {
        this.gp = gp;
        this.font = new Font("Arial", Font.BOLD, 16); // Custom font for message
    }

    public void drawText(Graphics2D g2, String message, int screenX, int screenY) {
        FontMetrics m = g2.getFontMetrics(font);

        // centre text on width
        int messageWidth = m.stringWidth(message);
        screenX = screenX - (messageWidth / 2);

        // centre text on height
        int messageHeight = m.getHeight();
        screenY = screenY - (messageHeight / 2);

        Color backgroundColor = new Color(0, 0, 0, 128); // Nero semi-trasparente (valore alpha 128 su 255)
        g2.setColor(backgroundColor);

        // Disegna il rettangolo semi-trasparente attorno al testo
        g2.fillRect(screenX - 6, screenY - m.getAscent() - 6, messageWidth + 12, messageHeight + 12);

        g2.setFont(font);
        g2.setColor(Color.WHITE); // white text color
        g2.drawString(message, screenX, screenY);
    }
}
