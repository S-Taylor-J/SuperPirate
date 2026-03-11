package main;

import entities.EnemyManager;
import entities.Player;
import entities.SpawnPoint;
import java.awt.Graphics;
import levels.LevelHandler;
import levels.SpawnData;
import levels.SpawnData.LevelExit;
import ui.UIManager;
import utilz.Camera;

public class Game implements Runnable{

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private LevelHandler levelHandler;
    private Camera camera;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2.0f;
    public final static float TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);

    // Map dimensions in tiles (the FULL map - larger than screen)
    public final static int MAP_TILES_WIDTH = 100;
    public final static int MAP_TILES_HEIGHT = 20;

    // Viewport dimensions in tiles (what's VISIBLE on screen - smaller than map)
    public final static int VIEWPORT_TILES_WIDTH = 15;   
    public final static int VIEWPORT_TILES_HEIGHT = 10;  

    // Screen dimensions based on VIEWPORT
    public static int SCREEN_WIDTH = (int)(TILES_SIZE * VIEWPORT_TILES_WIDTH);
    public static int SCREEN_HEIGHT = (int)(TILES_SIZE * VIEWPORT_TILES_HEIGHT);


    // Game entities and managers
    private Player player;
    private EnemyManager enemyManager;
    private UIManager uiManager;
    private int currentLevelIndex = 0;
    private boolean gameComplete = false;
    
    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        levelHandler = new LevelHandler(this);
        
        // Get player spawn point from level data
        SpawnPoint playerSpawn = levelHandler.getLevel().getPlayerSpawn();
        int playerX = playerSpawn != null ? playerSpawn.getX() : 200;
        int playerY = playerSpawn != null ? playerSpawn.getY() : 500;
        player = new Player(playerX, playerY, levelHandler.getLevel());
        uiManager = new UIManager(player);
        
        // Initialize enemy manager
        enemyManager = new EnemyManager(levelHandler.getLevel());
        
        // Connect player and enemy manager (bidirectional)
        player.setEnemyManager(enemyManager);
        enemyManager.setPlayer(player);
        
        // Spawn enemies from level spawn points
        SpawnPoint[] enemySpawns = levelHandler.getLevel().getEnemySpawns();
        for (SpawnPoint spawn : enemySpawns) {
            switch (spawn.getEntityType()) {
                case SpawnPoint.PINKFISH -> enemyManager.spawnPinkFish(spawn.getX(), spawn.getY());
                case SpawnPoint.CRAB -> enemyManager.spawnCrab(spawn.getX(), spawn.getY());
            }
        }

        // Spawn collectables from level collectable points
        entities.CollectablePoint[] collectablePoints = levelHandler.getLevel().getCollectablePoints();
        // TODO: Add logic to create collectable entities and add to the game world
    
        // Pass FULL MAP dimensions to camera
        camera = new Camera(MAP_TILES_WIDTH, MAP_TILES_HEIGHT);
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        if (gameComplete) return;
        
        player.update();
        enemyManager.update();
        camera.update(player);
        levelHandler.update();
        uiManager.update();
        
        checkLevelExit();
    }
    
    private void checkLevelExit() {
        LevelExit exit = SpawnData.getLevelExit(currentLevelIndex);
        if (exit == null) return;
        
        // Check if player hitbox intersects with exit zone
        if (exit.intersects(player.getX() + 50, player.getY() + 25, 30, 40)) {
            if (SpawnData.hasNextLevel(exit.nextLevelIndex)) {
                loadLevel(exit.nextLevelIndex);
            } else {
                gameComplete = true;
                System.out.println("GAME COMPLETE! Congratulations!");
            }
        }
    }
    
    public void loadLevel(int levelIndex) {
        System.out.println("Loading level " + levelIndex + "...");
        currentLevelIndex = levelIndex;
        
        // Load the new level in LevelHandler
        levelHandler.loadLevel(levelIndex);
        
        // Get new player spawn point
        SpawnPoint playerSpawn = levelHandler.getLevel().getPlayerSpawn();
        int playerX = playerSpawn != null ? playerSpawn.getX() : 200;
        int playerY = playerSpawn != null ? playerSpawn.getY() : 500;
        
        // Update player position and level reference
        player.setPosition(playerX, playerY);
        player.setLevel(levelHandler.getLevel());
        
        // Re-initialize enemy manager with new level
        enemyManager = new EnemyManager(levelHandler.getLevel());
        player.setEnemyManager(enemyManager);
        enemyManager.setPlayer(player);
        
        // Spawn enemies from new level's spawn points
        SpawnPoint[] enemySpawns = levelHandler.getLevel().getEnemySpawns();
        for (SpawnPoint spawn : enemySpawns) {
            switch (spawn.getEntityType()) {
                case SpawnPoint.PINKFISH -> enemyManager.spawnPinkFish(spawn.getX(), spawn.getY());
            }
        }
        
        System.out.println("Level " + levelIndex + " loaded!");
    }

    public void render(Graphics g){
        levelHandler.draw(g, camera);
        player.render(g, camera);
        enemyManager.render(g, camera);
        uiManager.render(g, player.getHealth(), player.getCoins(), player.getScore());
    }
    
    public Camera getCamera() {
        return camera;
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;
        long previousTime = System.nanoTime();

        long lastCheck = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;
        double deltaU = 0;
        double deltaF = 0;

        while(true){
            long currentTime = System.nanoTime();
            deltaU += ((currentTime - previousTime) / timePerUpdate);
            deltaF += ((currentTime - previousTime) / timePerFrame);
            previousTime = currentTime;
            
            if (deltaU >= 1){
                update();
                updates++;
                deltaU--;
            }
            if(deltaF >= 1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }
            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void windowFocusLost(){
        player.resetDirBooleans();
    }

    public Player getPlayer(){
        return player;
    }
}