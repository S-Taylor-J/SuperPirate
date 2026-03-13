package gamestates;

import entities.EnemyManager;
import entities.Player;
import entities.SpawnPoint;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import levels.Level;
import levels.LevelHandler;
import levels.SpawnData;
import levels.SpawnData.LevelExit;
import main.Game;
import ui.UIManager;
import utilz.Camera;

public class PlayingState extends State {
    
    private Player player;
    private EnemyManager enemyManager;
    private UIManager uiManager;
    private LevelHandler levelHandler;
    private Camera camera;
    
    private int currentLevelIndex = 0;
    private boolean paused = false;
    
    public PlayingState(Game game) {
        super(game);
    }
    
    public void initGame(int levelIndex) {
        this.currentLevelIndex = levelIndex;
        levelHandler = new LevelHandler(game);
        levelHandler.loadLevel(levelIndex);
        
        // Get player spawn point from level data
        SpawnPoint playerSpawn = levelHandler.getLevel().getPlayerSpawn();
        int playerX = playerSpawn != null ? playerSpawn.getX() : 200;
        int playerY = playerSpawn != null ? playerSpawn.getY() : 500;
        player = new Player(playerX, playerY, levelHandler.getLevel());
        
        uiManager = new UIManager(player);
        
        // Initialize enemy manager
        enemyManager = new EnemyManager(levelHandler.getLevel());
        
        // Connect player and enemy manager
        player.setEnemyManager(enemyManager);
        enemyManager.setPlayer(player);
        
        // Spawn enemies from level spawn points
        spawnEnemies();
        
        // Initialize camera
        camera = new Camera(Game.MAP_TILES_WIDTH, Game.MAP_TILES_HEIGHT);
    }
    
    private void spawnEnemies() {
        SpawnPoint[] enemySpawns = levelHandler.getLevel().getEnemySpawns();
        for (SpawnPoint spawn : enemySpawns) {
            switch (spawn.getEntityType()) {
                case SpawnPoint.PINKFISH -> enemyManager.spawnPinkFish(spawn.getX(), spawn.getY());
                case SpawnPoint.CRAB -> enemyManager.spawnCrab(spawn.getX(), spawn.getY());
            }
        }
    }
    
    @Override
    public void update() {
        if (paused) return;
        
        player.update();
        enemyManager.update();
        camera.update(player);
        levelHandler.update();
        uiManager.update();
        
        checkPlayerDeath();
        checkLevelExit();
    }
    
    private void checkPlayerDeath() {
        if (player.getHealth() <= 0) {
            game.setGameState(GameState.DEATH);
        }
    }
    
    private void checkLevelExit() {
        LevelExit exit = SpawnData.getLevelExit(currentLevelIndex);
        if (exit == null) return;
        
        if (exit.intersects(player.getX() + 50, player.getY() + 25, 30, 40)) {
            if (SpawnData.hasNextLevel(exit.nextLevelIndex)) {
                game.startLoading(exit.nextLevelIndex);
            } else {
                // Game complete - could add a victory state
                game.setGameState(GameState.MENU);
            }
        }
    }
    
    @Override
    public void render(Graphics g) {
        levelHandler.draw(g, camera);
        player.render(g, camera);
        enemyManager.render(g, camera);
        uiManager.render(g, player.getHealth(), player.getCoins(), player.getScore());
        
        if (paused) {
            // Draw pause overlay
            g.setColor(new java.awt.Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
            g.setColor(java.awt.Color.WHITE);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 48));
            String pauseText = "PAUSED";
            int textWidth = g.getFontMetrics().stringWidth(pauseText);
            g.drawString(pauseText, Game.SCREEN_WIDTH / 2 - textWidth / 2, Game.SCREEN_HEIGHT / 2);
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            paused = !paused;
            if (!paused) return;
        }
        
        if (!paused) {
            player.keyPressed(e);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (!paused) {
            player.keyReleased(e);
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {}
    
    // Getters
    public Player getPlayer() { return player; }
    public Camera getCamera() { return camera; }
    public int getCurrentLevelIndex() { return currentLevelIndex; }
    public boolean isPaused() { return paused; }
    public void setPaused(boolean paused) { this.paused = paused; }
}