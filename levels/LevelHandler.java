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
        loadLevel(currentLevelIndex);
    }

    public void loadLevel(int levelIndex) {
        this.currentLevelIndex = levelIndex;
        importTilesetForLevel(levelIndex);
        createLevel(levelIndex);
    }

    private void createLevel(int levelIndex) {
        int[][] levelData;
        int[][] backgroundData = null; 
        int[][] decorationData = null;
        
        switch(levelIndex) {
            case 1:
                levelData = levels.level1;
                backgroundData = levels.level1B;  // Level 1 has a background tile map
                break;
            case 0:
            default:
                levelData = levels.level0;
                // Level 0 has no background tile map
                break;
        }
        
        if (backgroundData != null) {
            level = new Level(levelData, backgroundData, decorationData);
        } else {
            level = new Level(levelData);
        }
        
        // Load spawn points for this level
        level.setPlayerSpawn(SpawnData.getPlayerSpawn(levelIndex));
        level.setEnemySpawns(SpawnData.getEnemySpawns(levelIndex));

        // Load collectable points for this level
        level.setCollectablePoints(CollectableData.getCollectables(levelIndex));
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
        
        // Draw background layer first (if this level has one)
        if (level.hasBackground()) {
            int bgEndX = Math.min(level.getBackgroundMaxTileWidth(), endX);
            int bgEndY = Math.min(level.getBackgroundMaxTileHeight(), endY);
            
            for (int y = startY; y < bgEndY; y++) {
                for (int x = startX; x < bgEndX; x++) {
                    int index = level.getBackgroundSpriteIndex(x, y);
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
        
        // Draw foreground layer
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

        // Draw collectable items as squares
        entities.CollectablePoint[] collectables = level.getCollectablePoints();
        for (entities.CollectablePoint c : collectables) {
            int drawX = (int)(c.getX() - camera.getXOffset());
            int drawY = (int)(c.getY() - camera.getYOffset());
            int size = (int)Game.TILES_SIZE;
            switch (c.getType()) {
                case entities.CollectablePoint.GOLD -> {
                    g.setColor(java.awt.Color.YELLOW);
                }
                case entities.CollectablePoint.POWERUP -> {
                    g.setColor(java.awt.Color.CYAN);
                }
                default -> {
                    g.setColor(java.awt.Color.GRAY);
                }
            }
            g.fillRect(drawX, drawY, size, size);
            g.setColor(java.awt.Color.BLACK);
            g.drawRect(drawX, drawY, size, size);
        }

        if (level.hasDecoration()) { 
            int decEndX = Math.min(level.getDecorationMaxTileWidth(), endX); 
            int decEndY = Math.min(level.getDecorationMaxTileHeight(), endY); 
            for (int y = startY; y < decEndY; y++) { 
                for (int x = startX; x < decEndX; x++) { 
                    int index = level.getDecorationSpriteIndex(x, y); 
                    if (index < 0 || index >= levelSprite.length) 
                        continue;
                    g.drawImage(levelSprite[index], 
                        (int)(x * Game.TILES_SIZE - camera.getXOffset()), 
                        (int)(y * Game.TILES_SIZE - camera.getYOffset()), 
                        (int)Game.TILES_SIZE, (int)Game.TILES_SIZE, 
                        null); 
                } 
            } 
        }
    }

    public void update(){

    }

    public Level getLevel() {
        return level;
    }
}
