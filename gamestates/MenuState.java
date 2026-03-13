package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import main.Game;

public class MenuState extends State {
    
    private Rectangle playButton;
    private Rectangle levelSelectButton;
    private Rectangle quitButton;
    
    private int hoveredButton = -1; // -1 = none, 0 = play, 1 = level select, 2 = quit
    
    // Title animation
    private float titleBounce = 0;
    
    public MenuState(Game game) {
        super(game);
        initButtons();
    }
    
    private void initButtons() {
        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = Game.SCREEN_WIDTH / 2 - buttonWidth / 2;
        int startY = Game.SCREEN_HEIGHT / 2;
        int spacing = 70;
        
        playButton = new Rectangle(centerX, startY, buttonWidth, buttonHeight);
        levelSelectButton = new Rectangle(centerX, startY + spacing, buttonWidth, buttonHeight);
        quitButton = new Rectangle(centerX, startY + spacing * 2, buttonWidth, buttonHeight);
    }
    
    @Override
    public void update() {
        titleBounce += 0.05f;
    }
    
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Draw title with bounce effect
        int titleY = 120 + (int)(Math.sin(titleBounce) * 8);
        drawTitle(g2d, titleY);
        
        // Draw buttons
        drawButton(g2d, playButton, "PLAY", hoveredButton == 0);
        drawButton(g2d, levelSelectButton, "LEVELS", hoveredButton == 1);
        drawButton(g2d, quitButton, "QUIT", hoveredButton == 2);
        
        // Draw footer
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(new Color(150, 140, 120));
        g2d.drawString("Use mouse to navigate", Game.SCREEN_WIDTH / 2 - 60, Game.SCREEN_HEIGHT - 30);
    }
    
    private void drawTitle(Graphics2D g2d, int y) {
        g2d.setFont(new Font("Serif", Font.BOLD, 72));
        String title = "Super Pirate Adventure";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        int x = Game.SCREEN_WIDTH / 2 - titleWidth / 2;
        
        // Shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString(title, x + 4, y + 4);
        
        // Main title (golden)
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString(title, x, y);
        
        // Highlight
        g2d.setColor(new Color(255, 255, 200, 100));
        g2d.drawString(title, x - 1, y - 1);
    }
    
    private void drawButton(Graphics2D g2d, Rectangle rect, String text, boolean hovered) {
        // Button background
        if (hovered) {
            g2d.setColor(new Color(60, 45, 30));
        } else {
            g2d.setColor(new Color(40, 30, 20, 220));
        }
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);
        
        // Button border
        g2d.setColor(hovered ? new Color(255, 215, 0) : new Color(100, 80, 60));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);
        
        // Button text
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        int textX = rect.x + (rect.width - textWidth) / 2;
        int textY = rect.y + rect.height / 2 + 8;
        
        g2d.setColor(hovered ? new Color(255, 215, 0) : new Color(200, 190, 170));
        g2d.drawString(text, textX, textY);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            game.setGameState(GameState.PLAYING);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (playButton.contains(e.getPoint())) {
            game.startNewGame(0);
            game.setGameState(GameState.PLAYING);
        } else if (levelSelectButton.contains(e.getPoint())) {
            game.setGameState(GameState.LEVEL_SELECT);
        } else if (quitButton.contains(e.getPoint())) {
            System.exit(0);
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {
        hoveredButton = -1;
        if (playButton.contains(e.getPoint())) {
            hoveredButton = 0;
        } else if (levelSelectButton.contains(e.getPoint())) {
            hoveredButton = 1;
        } else if (quitButton.contains(e.getPoint())) {
            hoveredButton = 2;
        }
    }
}
