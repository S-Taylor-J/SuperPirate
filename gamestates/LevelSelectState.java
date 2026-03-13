package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import levels.SpawnData;
import main.Game;

public class LevelSelectState extends State {
    
    private Rectangle[] levelButtons;
    private Rectangle backButton;
    
    private int hoveredButton = -1; // -1 = none, -2 = back, 0+ = level index
    private int totalLevels;
    
    public LevelSelectState(Game game) {
        super(game);
        totalLevels = SpawnData.getTotalLevels();
        initButtons();
    }
    
    private void initButtons() {
        levelButtons = new Rectangle[totalLevels];
        
        int buttonSize = 80;
        int spacing = 20;
        int columns = 4;
        int startX = Game.SCREEN_WIDTH / 2 - ((buttonSize + spacing) * Math.min(columns, totalLevels) - spacing) / 2;
        int startY = 180;
        
        for (int i = 0; i < totalLevels; i++) {
            int row = i / columns;
            int col = i % columns;
            int x = startX + col * (buttonSize + spacing);
            int y = startY + row * (buttonSize + spacing);
            levelButtons[i] = new Rectangle(x, y, buttonSize, buttonSize);
        }
        
        // Back button
        backButton = new Rectangle(30, Game.SCREEN_HEIGHT - 70, 120, 45);
    }
    
    @Override
    public void update() {}
    
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Title
        g2d.setFont(new Font("Serif", Font.BOLD, 48));
        String title = "SELECT LEVEL";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString(title, Game.SCREEN_WIDTH / 2 - titleWidth / 2 + 3, 103);
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString(title, Game.SCREEN_WIDTH / 2 - titleWidth / 2, 100);
        
        // Level buttons
        for (int i = 0; i < totalLevels; i++) {
            drawLevelButton(g2d, levelButtons[i], i + 1, hoveredButton == i);
        }
        
        // Back button
        drawBackButton(g2d, hoveredButton == -2);
    }
    
    private void drawLevelButton(Graphics2D g2d, Rectangle rect, int levelNum, boolean hovered) {
        // Button background
        if (hovered) {
            g2d.setColor(new Color(60, 45, 30));
        } else {
            g2d.setColor(new Color(40, 30, 20, 220));
        }
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 12, 12);
        
        // Button border
        g2d.setColor(hovered ? new Color(255, 215, 0) : new Color(100, 80, 60));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 12, 12);
        
        // Level number
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        String text = String.valueOf(levelNum);
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        int textX = rect.x + (rect.width - textWidth) / 2;
        int textY = rect.y + rect.height / 2 + 12;
        
        g2d.setColor(hovered ? new Color(255, 215, 0) : new Color(200, 190, 170));
        g2d.drawString(text, textX, textY);
    }
    
    private void drawBackButton(Graphics2D g2d, boolean hovered) {
        // Button background
        if (hovered) {
            g2d.setColor(new Color(60, 45, 30));
        } else {
            g2d.setColor(new Color(40, 30, 20, 220));
        }
        g2d.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 10, 10);
        
        // Button border
        g2d.setColor(hovered ? new Color(255, 215, 0) : new Color(100, 80, 60));
        g2d.drawRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 10, 10);
        
        // Text
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(hovered ? new Color(255, 215, 0) : new Color(200, 190, 170));
        g2d.drawString("< BACK", backButton.x + 25, backButton.y + 30);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            game.setGameState(GameState.MENU);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (backButton.contains(e.getPoint())) {
            game.setGameState(GameState.MENU);
            return;
        }
        
        for (int i = 0; i < totalLevels; i++) {
            if (levelButtons[i].contains(e.getPoint())) {
                game.startNewGame(i);
                game.setGameState(GameState.PLAYING);
                return;
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {
        hoveredButton = -1;
        
        if (backButton.contains(e.getPoint())) {
            hoveredButton = -2;
            return;
        }
        
        for (int i = 0; i < totalLevels; i++) {
            if (levelButtons[i].contains(e.getPoint())) {
                hoveredButton = i;
                return;
            }
        }
    }
}
