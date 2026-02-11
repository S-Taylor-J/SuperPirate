package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import utilz.Camera;
import utilz.LoadSave;

public class LevelHandler {

    private Game game;
    private BufferedImage[] levelSprite;
    private Level level;
    private int currentLevelIndex = 0;

    // Tileset configurations: [columns, rows]
    private static final int[][] TILESET_CONFIG = {
        {17, 5},   // Level 0: Terrain.png (17x5)
        {19, 13}   // Level 1: ship.png (19x13)
    };

    public LevelHandler(Game game){
        this.game = game;
        loadLevel(1);
    }

    public void loadLevel(int levelIndex) {
        this.currentLevelIndex = levelIndex;
        importTilesetForLevel(levelIndex);
        createLevel(levelIndex);
    }

    private void createLevel(int levelIndex) {
        int[][] levelData;
        switch(levelIndex) {
            case 1:
                levelData = levels.level1;
                break;
            case 0:
            default:
                levelData = levels.level0;
                break;
        }
        level = new Level(levelData);
    }

    private void importTilesetForLevel(int levelIndex) {
        BufferedImage img;
        int cols, rows;
        
        switch(levelIndex) {
            case 1:
                img = LoadSave.getSpriteAtlas(LoadSave.SHIP_TILESET);
                cols = TILESET_CONFIG[1][0];  // 19
                rows = TILESET_CONFIG[1][1];  // 13
                break;
            case 0:
            default:
                img = LoadSave.getSpriteAtlas(LoadSave.TERRAIN_TILESET);
                cols = TILESET_CONFIG[0][0];  // 17
                rows = TILESET_CONFIG[0][1];  // 5
                break;
        }
        
        levelSprite = new BufferedImage[cols * rows];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                int index = i * cols + j;
                levelSprite[index] = img.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public void draw(Graphics g, Camera camera){
        // Calculate which tiles are visible on screen
        int startX = (int)(camera.getXOffset() / Game.TILES_SIZE);
        int startY = (int)(camera.getYOffset() / Game.TILES_SIZE);
        int endX = (int)((camera.getXOffset() + Game.SCREEN_WIDTH) / Game.TILES_SIZE) + 1;
        int endY = (int)((camera.getYOffset() + Game.SCREEN_HEIGHT) / Game.TILES_SIZE) + 1;
        
        // Clamp to map bounds using level's actual dimensions
        startX = Math.max(0, startX);
        startY = Math.max(0, startY);
        endX = Math.min(level.getMaxTileWidth(), endX);
        endY = Math.min(level.getMaxTileHeight(), endY);
        
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                int index = level.getSpriteIndex(x, y);
                if (index < 0 || index >= levelSprite.length)
                    continue;
                g.drawImage(levelSprite[index], 
                    (int)(x * Game.TILES_SIZE - camera.getXOffset()), 
                    (int)(y * Game.TILES_SIZE - camera.getYOffset()), 
                    (int)Game.TILES_SIZE, 
                    (int)Game.TILES_SIZE, 
                    null);
            }
        }
    }

    public void update(){

    }

    public Level getLevel() {
        return level;
    }
}
