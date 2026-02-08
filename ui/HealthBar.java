package ui;

import java.awt.Color;
import java.awt.Graphics;

public class HealthBar {
    public void draw(Graphics g, int health) {
        g.setColor(Color.GRAY);
        g.fillRect(20, 20, 200 , 20);
        g.setColor(Color.RED);
        g.fillRect(20, 20, health * 40, 20);
        g.setColor(Color.BLACK);
        g.drawRect(20, 20, 200 , 20);
    }
}
