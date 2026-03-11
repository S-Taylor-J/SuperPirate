package levels;

import entities.SpawnPoint;

/**
 * Spawn point data for each level.
 * 
 * HOW TO ADD SPAWN POINTS FROM TILED:
 * 1. In Tiled, create an Object Layer called "SpawnPoints"
 * 2. Add Point objects where you want spawns
 * 3. Set a custom property "type" to: "player", "pinkfish", or "pirate"
 * 4. Export the map and copy the object x,y coordinates here
 * 
 * Coordinates are in PIXELS (Tiled exports pixel coordinates by default).
 * For tile-based placement, use: SpawnPoint.fromTile(tileX, tileY, type, 32)
 */
public class SpawnData {

    // ==================== LEVEL EXIT DATA ====================
    // Stores exit zone position (in tiles) and next level index
    
    public static class LevelExit {
        public final int tileX, tileY;      // Exit zone position (tile coords)
        public final int width, height;      // Exit zone size (in tiles)
        public final int nextLevelIndex;     // Level to load (-1 = game complete)
        
        public LevelExit(int tileX, int tileY, int width, int height, int nextLevelIndex) {
            this.tileX = tileX;
            this.tileY = tileY;
            this.width = width;
            this.height = height;
            this.nextLevelIndex = nextLevelIndex;
        }
        
        // Get pixel coordinates for collision detection
        public int getPixelX() { return tileX * 32; }
        public int getPixelY() { return tileY * 32; }
        public int getPixelWidth() { return width * 32; }
        public int getPixelHeight() { return height * 32; }
        
        // Check if a position (in pixels) is inside the exit zone
        public boolean contains(float px, float py) {
            return px >= getPixelX() && px < getPixelX() + getPixelWidth()
                && py >= getPixelY() && py < getPixelY() + getPixelHeight();
        }
        
        // Check if a hitbox overlaps with the exit zone
        public boolean intersects(float px, float py, int hitboxWidth, int hitboxHeight) {
            return px + hitboxWidth > getPixelX() && px < getPixelX() + getPixelWidth()
                && py + hitboxHeight > getPixelY() && py < getPixelY() + getPixelHeight();
        }
    }
    
    // Level 0 exit - leads to Level 1
    public static final LevelExit LEVEL0_EXIT = new LevelExit(95, 10, 2, 4, 1);
    
    // Level 1 exit - leads to Level 2 (or -1 for game complete)
    public static final LevelExit LEVEL1_EXIT = new LevelExit(58, 10, 2, 4, -1);

    // ==================== LEVEL 0 SPAWN POINTS ====================
    // Grassland / Island level
    
    public static final SpawnPoint LEVEL0_PLAYER_SPAWN = 
        SpawnPoint.fromTile(3, 14, SpawnPoint.PLAYER, 32);  // Starting position
    
    public static final SpawnPoint[] LEVEL0_ENEMY_SPAWNS = {
        // PinkFish enemies
        SpawnPoint.fromTile(30, 11, SpawnPoint.PINKFISH, 32),
        SpawnPoint.fromTile(55, 9, SpawnPoint.CRAB, 32),
        SpawnPoint.fromTile(80, 12, SpawnPoint.CRAB, 32),

        
        // Pirate enemies (if implemented)
        // SpawnPoint.fromTile(45, 14, SpawnPoint.PIRATE, 32),
    };

    // ==================== LEVEL 1 SPAWN POINTS ====================
    // Ship interior level
    
    public static final SpawnPoint LEVEL1_PLAYER_SPAWN = 
        SpawnPoint.fromTile(5, 13, SpawnPoint.PLAYER, 32);  // Ship deck start
    
    public static final SpawnPoint[] LEVEL1_ENEMY_SPAWNS = {
        // PinkFish enemies
        SpawnPoint.fromTile(25, 8, SpawnPoint.PINKFISH, 32),
        SpawnPoint.fromTile(35, 13, SpawnPoint.PINKFISH, 32),
        SpawnPoint.fromTile(45, 8, SpawnPoint.PINKFISH, 32),
    };

    // ==================== HELPER METHODS ====================
    
    //  Get player spawn point for a level
    public static SpawnPoint getPlayerSpawn(int levelIndex) {
        return switch (levelIndex) {
            case 0 -> LEVEL0_PLAYER_SPAWN;
            case 1 -> LEVEL1_PLAYER_SPAWN;
            default -> LEVEL0_PLAYER_SPAWN;
        };
    }

    //  Get all enemy spawn points for a level
    public static SpawnPoint[] getEnemySpawns(int levelIndex) {
        return switch (levelIndex) {
            case 0 -> LEVEL0_ENEMY_SPAWNS;
            case 1 -> LEVEL1_ENEMY_SPAWNS;
            default -> new SpawnPoint[0];
        };
    }

    //  Get all spawn points (player + enemies) for a level
    public static SpawnPoint[] getAllSpawns(int levelIndex) {
        SpawnPoint playerSpawn = getPlayerSpawn(levelIndex);
        SpawnPoint[] enemySpawns = getEnemySpawns(levelIndex);
        
        SpawnPoint[] all = new SpawnPoint[1 + enemySpawns.length];
        all[0] = playerSpawn;
        System.arraycopy(enemySpawns, 0, all, 1, enemySpawns.length);
        return all;
    }
    
    // ==================== LEVEL EXIT METHODS ====================
    
    // Get level exit for a level
    public static LevelExit getLevelExit(int levelIndex) {
        return switch (levelIndex) {
            case 0 -> LEVEL0_EXIT;
            case 1 -> LEVEL1_EXIT;
            default -> null;
        };
    }
    
    // Check if level index is valid (not game complete)
    public static boolean hasNextLevel(int nextLevelIndex) {
        return nextLevelIndex >= 0;
    }
    
    // Get total number of levels
    public static int getTotalLevels() {
        return 2;
    }
}
