package ui;

import entities.Player;
import java.awt.Graphics;

public class UIManager {

    private Player player;
    private UI ui;

    public UIManager(Player player) {
        this.player = player;
        this.ui = new UI();
    }

    public void update() {
        // Update UI animations with current score
        ui.update(player.getScore());
    }


    public void render(Graphics g, int health, int coins, int score) {
        // Render the UI elements
        ui.draw(g, health, coins, score);
    }



}
