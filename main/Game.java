package main;

import entities.Player;
import java.awt.Graphics;
import levels.LevelHandler;
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
    public final static int MAP_TILES_HEIGHT = 40;

    // Viewport dimensions in tiles (what's VISIBLE on screen - smaller than map)
    public final static int VIEWPORT_TILES_WIDTH = 20;   
    public final static int VIEWPORT_TILES_HEIGHT = 8;  
    
    // Screen dimensions based on VIEWPORT (not full map)
    public static int SCREEN_WIDTH = (int)(TILES_SIZE * VIEWPORT_TILES_WIDTH);
    public static int SCREEN_HEIGHT = (int)(TILES_SIZE * VIEWPORT_TILES_HEIGHT);

    private Player player;
    
    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        levelHandler = new LevelHandler(this);
        player = new Player(50, 50, levelHandler.getLevel());
        // Pass FULL MAP dimensions to camera
        camera = new Camera(MAP_TILES_WIDTH, MAP_TILES_HEIGHT);
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        player.update();
        camera.update(player);
        levelHandler.update();
    }

    public void render(Graphics g){
        levelHandler.draw(g, camera);
        player.render(g, camera);
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