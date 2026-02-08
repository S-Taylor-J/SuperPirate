package ui;

import entities.Player;
import java.awt.Graphics;

public class UIManager {

    private Player player;

    public UIManager(Player player) {
        this.player = player;
    }

    public void update() {
        // Update UI elements if needed (e.g., animations, timers)
    }


    public void render(Graphics g, int health, int coins, int score) {
        // Render the UI elements
        UI ui = new UI();
        ui.draw(g, player.getHealth(), coins, score);
    }



}
