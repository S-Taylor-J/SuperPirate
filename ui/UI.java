package ui;

import java.awt.Graphics;

public class UI {
    private HealthBar healthBar;
    private CoinDisplay coinDisplay;
    private ScoreDisplay scoreDisplay;

    public UI() {
        healthBar = new HealthBar();
        coinDisplay = new CoinDisplay();
        scoreDisplay = new ScoreDisplay();
    }
    
    public void update(int score) {
        coinDisplay.update();
        scoreDisplay.update(score);
    }

    public void draw(Graphics g, int health, int coins, int score) {
        healthBar.draw(g, health);
        coinDisplay.draw(g, coins);
        scoreDisplay.draw(g, score);
    }
}
