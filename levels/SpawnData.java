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

    // ==================== LEVEL 0 SPAWN POINTS ====================
    // Grassland / Island level
    
    public static final SpawnPoint LEVEL0_PLAYER_SPAWN = 
        SpawnPoint.fromTile(3, 14, SpawnPoint.PLAYER, 32);  // Starting position
    
    public static final SpawnPoint[] LEVEL0_ENEMY_SPAWNS = {
        // PinkFish enemies
        SpawnPoint.fromTile(30, 11, SpawnPoint.PINKFISH, 32),
        SpawnPoint.fromTile(55, 9, SpawnPoint.PINKFISH, 32),
        SpawnPoint.fromTile(80, 12, SpawnPoint.PINKFISH, 32),
        
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
}
