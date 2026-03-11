package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import utilz.LoadSave;

public class ScoreDisplay {
    private BufferedImage bannerImg;
    
    // Position (top right area)
    private final int x = 20;
    private final int y = 115;
    
    // Score animation
    private int displayedScore = 0;
    private int targetScore = 0;
    private float scoreScale = 1.0f;
    
    public ScoreDisplay() {
        loadImages();
    }
    
    private void loadImages() {
        bannerImg = LoadSave.getSpriteAtlas(LoadSave.UI_SMALL_BANNER);
    }
    
    public void update(int actualScore) {
        targetScore = actualScore;
        
        // Smooth score counting animation
        if (displayedScore < targetScore) {
            int diff = targetScore - displayedScore;
            displayedScore += Math.max(1, diff / 10);
            if (displayedScore > targetScore) {
                displayedScore = targetScore;
            }
            scoreScale = 1.15f; // Pop effect when score changes
        }
        
        // Ease back to normal scale
        if (scoreScale > 1.0f) {
            scoreScale -= 0.02f;
            if (scoreScale < 1.0f) {
                scoreScale = 1.0f;
            }
        }
    }
    
    public void draw(Graphics g, int score) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Background panel
        g2d.setColor(new Color(20, 15, 10, 200));
        g2d.fillRoundRect(x, y, 110, 35, 12, 12);
        g2d.setColor(new Color(60, 45, 30));
        g2d.drawRoundRect(x, y, 110, 35, 12, 12);
        
        // Draw banner decoration
        if (bannerImg != null) {
            g2d.drawImage(bannerImg, x + 4, y + 2, 30, 30, null);
        }
        
        // Score label
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.setColor(new Color(180, 160, 130));
        g2d.drawString("SCORE", x + 38, y + 14);
        
        // Score value with scale animation
        int fontSize = (int)(16 * scoreScale);
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
        
        String scoreText = String.format("%,d", displayedScore);
        
        // Main text (warm white/cream)
        g2d.setColor(new Color(255, 248, 220));
        g2d.drawString(scoreText, x + 38, y + 28);
    }
}
