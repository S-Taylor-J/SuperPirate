package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import utilz.LoadSave;

public class CoinDisplay {
    private BufferedImage coinImg;
    
    // Position
    private final int x = 20;
    private final int y = 65;
    private final int slotSize = 40;
    
    public CoinDisplay() {
        loadImages();
    }
    
    private void loadImages() {
        coinImg = LoadSave.getSpriteAtlas(LoadSave.UI_GOLD_COIN);
    }

    public void update() {
        // No animation
    }
    
    public void draw(Graphics g, int coins) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Background panel
        g2d.setColor(new Color(20, 15, 10, 200));
        g2d.fillRoundRect(x, y, 95, slotSize, 12, 12);
        g2d.setColor(new Color(60, 45, 30));
        g2d.drawRoundRect(x, y, 95, slotSize, 12, 12);
        
        // Draw coin sprite
        int coinX = x + 8;
        int coinY = y + 8;
        int coinSize = 24; 
        
        if (coinImg != null) {
            g2d.drawImage(coinImg, coinX, coinY, coinSize, coinSize, null);
        }
        
        // Draw coin count 
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        String coinText = "x" + coins;
                
        // Main text 
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString(coinText, x + 44, y + 27);
    }
}
