package main;

import gamestates.DeathState;
import gamestates.GameState;
import gamestates.LevelSelectState;
import gamestates.LoadingState;
import gamestates.MenuState;
import gamestates.PlayingState;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

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

    // Game states
    private GameState currentState = GameState.MENU;
    private MenuState menuState;
    private PlayingState playingState;
    private DeathState deathState;
    private LevelSelectState levelSelectState;
    private LoadingState loadingState;
    
    public Game() {
        initStates();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initStates() {
        menuState = new MenuState(this);
        playingState = new PlayingState(this);
        deathState = new DeathState(this);
        levelSelectState = new LevelSelectState(this);
        loadingState = new LoadingState(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (currentState) {
            case MENU -> menuState.update();
            case PLAYING -> playingState.update();
            case DEATH -> deathState.update();
            case LEVEL_SELECT -> levelSelectState.update();
            case LOADING -> loadingState.update();
        }
    }

    public void render(Graphics g) {
        switch (currentState) {
            case MENU -> menuState.render(g);
            case PLAYING -> playingState.render(g);
            case DEATH -> {
                // Render game behind death screen
                playingState.render(g);
                deathState.render(g);
            }
            case LEVEL_SELECT -> levelSelectState.render(g);
            case LOADING -> loadingState.render(g);
        }
    }
    
    // State management
    public void setGameState(GameState state) {
        this.currentState = state;
        if (state == GameState.DEATH) {
            deathState.reset();
        }
    }
    
    public GameState getGameState() {
        return currentState;
    }
    
    // Start a new game at specified level
    public void startNewGame(int levelIndex) {
        playingState.initGame(levelIndex);
    }
    
    // Restart current level
    public void restartLevel() {
        int currentLevel = playingState.getCurrentLevelIndex();
        playingState.initGame(currentLevel);
        setGameState(GameState.PLAYING);
    }
    
    // Start loading screen for level transition
    public void startLoading(int levelIndex) {
        loadingState.startLoading(levelIndex);
        setGameState(GameState.LOADING);
    }
    
    // Get playing state for level initialization
    public PlayingState getPlayingState() {
        return playingState;
    }

    // Input handling - delegate to current state
    public void keyPressed(KeyEvent e) {
        switch (currentState) {
            case MENU -> menuState.keyPressed(e);
            case PLAYING -> playingState.keyPressed(e);
            case DEATH -> deathState.keyPressed(e);
            case LEVEL_SELECT -> levelSelectState.keyPressed(e);
            case LOADING -> loadingState.keyPressed(e);
        }
    }
    
    public void keyReleased(KeyEvent e) {
        switch (currentState) {
            case MENU -> menuState.keyReleased(e);
            case PLAYING -> playingState.keyReleased(e);
            case DEATH -> deathState.keyReleased(e);
            case LEVEL_SELECT -> levelSelectState.keyReleased(e);
            case LOADING -> loadingState.keyReleased(e);
        }
    }
    
    public void mousePressed(MouseEvent e) {
        switch (currentState) {
            case MENU -> menuState.mousePressed(e);
            case PLAYING -> playingState.mousePressed(e);
            case DEATH -> deathState.mousePressed(e);
            case LEVEL_SELECT -> levelSelectState.mousePressed(e);
            case LOADING -> loadingState.mousePressed(e);
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        switch (currentState) {
            case MENU -> menuState.mouseReleased(e);
            case PLAYING -> playingState.mouseReleased(e);
            case DEATH -> deathState.mouseReleased(e);
            case LEVEL_SELECT -> levelSelectState.mouseReleased(e);
            case LOADING -> loadingState.mouseReleased(e);
        }
    }
    
    public void mouseMoved(MouseEvent e) {
        switch (currentState) {
            case MENU -> menuState.mouseMoved(e);
            case PLAYING -> playingState.mouseMoved(e);
            case DEATH -> deathState.mouseMoved(e);
            case LEVEL_SELECT -> levelSelectState.mouseMoved(e);
            case LOADING -> loadingState.mouseMoved(e);
        }
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

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += ((currentTime - previousTime) / timePerUpdate);
            deltaF += ((currentTime - previousTime) / timePerFrame);
            previousTime = currentTime;
            
            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }
            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void windowFocusLost() {
        if (currentState == GameState.PLAYING && playingState.getPlayer() != null) {
            playingState.getPlayer().resetDirBooleans();
        }
    }
}