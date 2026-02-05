package ui;

import java.awt.Color;
import java.awt.Graphics;

public class ScoreDisplay {
    public void draw(Graphics g, int score) {
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 20, 100);
    }
}
