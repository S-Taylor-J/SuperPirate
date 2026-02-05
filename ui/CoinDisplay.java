package ui;

import java.awt.Color;
import java.awt.Graphics;

public class CoinDisplay {
    public void draw(Graphics g, int coins) {
        g.setColor(Color.YELLOW);
        g.fillOval(20, 50, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("x " + coins, 50, 65);
    }
}
