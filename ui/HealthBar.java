package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import utilz.LoadSave;

public class HealthBar {
    private BufferedImage frameImg;
    private BufferedImage fillImg;
    private BufferedImage bgImg;
    
    // Position and dimensions
    private final int x = 20;
    private final int y = 15;
    private final int frameSize = 40;  // Size for corner frame pieces
    private final int barWidth = 180;
    private final int barHeight = 24;
    private final int barOffsetX = 35; 
    private final int barOffsetY = 8;  
    
    private int maxHealth = 5;
    
    public HealthBar() {
        loadImages();
    }
    
    private void loadImages() {
        frameImg = LoadSave.getSpriteAtlas(LoadSave.UI_HEALTH_BAR_FRAME);
        fillImg = LoadSave.getSpriteAtlas(LoadSave.UI_HEALTH_BAR_FILL);
        bgImg = LoadSave.getSpriteAtlas(LoadSave.UI_HEALTH_BAR_BG);
    }
    
    public void draw(Graphics g, int health) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int barX = x + barOffsetX;
        int barY = y + barOffsetY;
        
        // Draw background panel with transparency
        g2d.setColor(new Color(20, 15, 10, 200));
        g2d.fillRoundRect(x, y, barWidth + 50, frameSize, 12, 12);
        g2d.setColor(new Color(60, 45, 30));
        g2d.drawRoundRect(x, y, barWidth + 50, frameSize, 12, 12);
        
        // Draw bar background (dark)
        g2d.setColor(new Color(40, 30, 25));
        g2d.fillRoundRect(barX, barY, barWidth, barHeight, 6, 6);
        
        // Draw health fill
        float healthPercent = Math.max(0, Math.min(1, health / (float) maxHealth));
        int fillWidth = (int) (barWidth * healthPercent);
        
        if (fillWidth > 0) {
            // Gradient health color based on health level
            Color healthColor;
            if (healthPercent > 0.6f) {
                healthColor = new Color(76, 175, 80);  // Green
            } else if (healthPercent > 0.3f) {
                healthColor = new Color(255, 193, 7);  // Yellow/Orange
            } else {
                healthColor = new Color(244, 67, 54);  // Red
            }
            
            // Main fill
            g2d.setColor(healthColor);
            g2d.fillRoundRect(barX + 2, barY + 2, fillWidth - 4, barHeight - 4, 4, 4);
            
        }
        
        // Draw frame decoration
        if (frameImg != null) {
            g2d.drawImage(frameImg, x + 2, y + 4, 32, 32, null);
        }
        
        // Draw border around bar
        g2d.setColor(new Color(100, 80, 60));
        g2d.drawRoundRect(barX, barY, barWidth, barHeight, 6, 6);
        
        // Draw health text
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(12f).deriveFont(java.awt.Font.BOLD));
        String healthText = health + "/" + maxHealth;
        int textWidth = g2d.getFontMetrics().stringWidth(healthText);
        g2d.drawString(healthText, barX + (barWidth - textWidth) / 2, barY + barHeight - 6);
    }
    
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
}
