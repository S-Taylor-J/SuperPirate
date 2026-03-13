package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import main.Game;

public class LoadingState extends State {
    
    private int targetLevelIndex = 0;
    private float loadProgress = 0;
    private boolean loadingComplete = false;
    private String loadingText = "Loading...";
    
    // Visual settings
    private final int barWidth = 400;
    private final int barHeight = 30;
    private final int barX = Game.SCREEN_WIDTH / 2 - barWidth / 2;
    private final int barY = Game.SCREEN_HEIGHT / 2 + 50;
    
    public LoadingState(Game game) {
        super(game);
    }
    
    public void startLoading(int levelIndex) {
        this.targetLevelIndex = levelIndex;
        this.loadProgress = 0;
        this.loadingComplete = false;
        this.loadingText = "Loading Level " + (levelIndex + 1) + "...";
    }
    
    @Override
    public void update() {
        if (!loadingComplete) {
            // Simulate loading progress
            loadProgress += 0.02f;
            
            if (loadProgress >= 0.5f && loadProgress < 0.6f) {
                // Actually load the level in the middle of the progress bar
                game.getPlayingState().initGame(targetLevelIndex);
            }
            
            if (loadProgress >= 1.0f) {
                loadProgress = 1.0f;
                loadingComplete = true;
            }
        } else {
            // Small delay before transitioning
            loadProgress += 0.05f;
            if (loadProgress >= 1.5f) {
                game.setGameState(GameState.PLAYING);
            }
        }
    }
    
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Dark background
        g2d.setColor(new Color(20, 15, 10));
        g2d.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        
        // Level title
        g2d.setFont(new Font("Serif", Font.BOLD, 48));
        String levelTitle = "LEVEL " + (targetLevelIndex + 1);
        int titleWidth = g2d.getFontMetrics().stringWidth(levelTitle);
        
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString(levelTitle, Game.SCREEN_WIDTH / 2 - titleWidth / 2 + 3, Game.SCREEN_HEIGHT / 2 - 50 + 3);
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString(levelTitle, Game.SCREEN_WIDTH / 2 - titleWidth / 2, Game.SCREEN_HEIGHT / 2 - 50);
        
        // Loading text
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.setColor(new Color(180, 170, 150));
        int textWidth = g2d.getFontMetrics().stringWidth(loadingText);
        g2d.drawString(loadingText, Game.SCREEN_WIDTH / 2 - textWidth / 2, barY - 15);
        
        // Progress bar background
        g2d.setColor(new Color(40, 30, 20));
        g2d.fillRoundRect(barX, barY, barWidth, barHeight, 10, 10);
        
        // Progress bar fill
        float displayProgress = Math.min(loadProgress, 1.0f);
        int fillWidth = (int)(barWidth * displayProgress);
        if (fillWidth > 0) {
            g2d.setColor(new Color(255, 193, 7));
            g2d.fillRoundRect(barX + 2, barY + 2, fillWidth - 4, barHeight - 4, 8, 8);
            
            // Shine effect
            g2d.setColor(new Color(255, 255, 200, 80));
            g2d.fillRoundRect(barX + 2, barY + 2, fillWidth - 4, (barHeight - 4) / 2, 8, 8);
        }
        
        // Progress bar border
        g2d.setColor(new Color(100, 80, 60));
        g2d.drawRoundRect(barX, barY, barWidth, barHeight, 10, 10);
        
        // Percentage
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String percent = (int)(displayProgress * 100) + "%";
        int percentWidth = g2d.getFontMetrics().stringWidth(percent);
        g2d.setColor(Color.WHITE);
        g2d.drawString(percent, Game.SCREEN_WIDTH / 2 - percentWidth / 2, barY + barHeight + 25);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {}
}
