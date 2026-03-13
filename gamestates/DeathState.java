package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import main.Game;

public class DeathState extends State {
    
    private Rectangle retryButton;
    private Rectangle menuButton;
    
    private int hoveredButton = -1;
    private float fadeAlpha = 0;
    private boolean fullyVisible = false;
    
    public DeathState(Game game) {
        super(game);
        initButtons();
    }
    
    private void initButtons() {
        int buttonWidth = 180;
        int buttonHeight = 50;
        int centerX = Game.SCREEN_WIDTH / 2 - buttonWidth / 2;
        int startY = Game.SCREEN_HEIGHT / 2 + 40;
        int spacing = 70;
        
        retryButton = new Rectangle(centerX, startY, buttonWidth, buttonHeight);
        menuButton = new Rectangle(centerX, startY + spacing, buttonWidth, buttonHeight);
    }
    
    public void reset() {
        fadeAlpha = 0;
        fullyVisible = false;
    }
    
    @Override
    public void update() {
        if (fadeAlpha < 1.0f) {
            fadeAlpha += 0.02f;
            if (fadeAlpha >= 1.0f) {
                fadeAlpha = 1.0f;
                fullyVisible = true;
            }
        }
    }
    
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dark overlay with fade
        g2d.setColor(new Color(0, 0, 0, (int)(200 * fadeAlpha)));
        g2d.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        
        // Red vignette effect
        g2d.setColor(new Color(100, 0, 0, (int)(100 * fadeAlpha)));
        g2d.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        
        if (fadeAlpha > 0.5f) {
            float textAlpha = (fadeAlpha - 0.5f) * 2;
            
            // "GAME OVER" text
            g2d.setFont(new Font("Serif", Font.BOLD, 64));
            String gameOver = "GAME OVER";
            int textWidth = g2d.getFontMetrics().stringWidth(gameOver);
            int x = Game.SCREEN_WIDTH / 2 - textWidth / 2;
            int y = Game.SCREEN_HEIGHT / 2 - 60;
            
            // Shadow
            g2d.setColor(new Color(0, 0, 0, (int)(150 * textAlpha)));
            g2d.drawString(gameOver, x + 3, y + 3);
            
            // Red text
            g2d.setColor(new Color(180, 30, 30, (int)(255 * textAlpha)));
            g2d.drawString(gameOver, x, y);
        }
        
        if (fullyVisible) {
            drawButton(g2d, retryButton, "RETRY", hoveredButton == 0);
            drawButton(g2d, menuButton, "MENU", hoveredButton == 1);
        }
    }
    
    private void drawButton(Graphics2D g2d, Rectangle rect, String text, boolean hovered) {
        // Button background
        if (hovered) {
            g2d.setColor(new Color(80, 30, 30));
        } else {
            g2d.setColor(new Color(50, 20, 20, 220));
        }
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);
        
        // Button border
        g2d.setColor(hovered ? new Color(255, 100, 100) : new Color(120, 60, 60));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);
        
        // Button text
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        int textX = rect.x + (rect.width - textWidth) / 2;
        int textY = rect.y + rect.height / 2 + 7;
        
        g2d.setColor(hovered ? new Color(255, 200, 200) : new Color(200, 150, 150));
        g2d.drawString(text, textX, textY);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (fullyVisible) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                game.restartLevel();
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                game.setGameState(GameState.MENU);
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (!fullyVisible) return;
        
        if (retryButton.contains(e.getPoint())) {
            game.restartLevel();
        } else if (menuButton.contains(e.getPoint())) {
            game.setGameState(GameState.MENU);
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {
        hoveredButton = -1;
        if (fullyVisible) {
            if (retryButton.contains(e.getPoint())) {
                hoveredButton = 0;
            } else if (menuButton.contains(e.getPoint())) {
                hoveredButton = 1;
            }
        }
    }
}
