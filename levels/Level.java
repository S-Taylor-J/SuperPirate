package levels;

import entities.SpawnPoint;

public class Level {

    private int[][] levelData;
    private int[][] backgroundData;
    private int[][] decorationData;
    
    // Spawn points
    private SpawnPoint playerSpawn;
    private SpawnPoint[] enemySpawns;

    public Level(int[][] levelData) {
        this.levelData = levelData;
        this.backgroundData = null;
        this.decorationData = null;
        this.playerSpawn = null;
        this.enemySpawns = new SpawnPoint[0];
    }

    public Level(int[][] levelData, int[][] backgroundData, int[][] decorationData) {
        this.levelData = levelData;
        this.backgroundData = backgroundData;
        this.decorationData = decorationData;
        this.playerSpawn = null;
        this.enemySpawns = new SpawnPoint[0];
    }

    // -- Spawn point methods --
    
    public void setPlayerSpawn(SpawnPoint spawn) {
        this.playerSpawn = spawn;
    }

    public SpawnPoint getPlayerSpawn() {
        return playerSpawn;
    }

    public void setEnemySpawns(SpawnPoint[] spawns) {
        this.enemySpawns = spawns != null ? spawns : new SpawnPoint[0];
    }

    public SpawnPoint[] getEnemySpawns() {
        return enemySpawns;
    }

    // -- Map data --

    public int getSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

    public int[][] getLevelData() {
        return levelData;
    }

    // -- Background data --
    public boolean hasBackground() {
        return backgroundData != null;
    }

    public int getBackgroundSpriteIndex(int x, int y) {
        if (backgroundData == null) return -1;
        if (y < 0 || y >= backgroundData.length || x < 0 || x >= backgroundData[0].length)
            return -1;
        return backgroundData[y][x];
    }

    public int[][] getBackgroundData() {
        return backgroundData;
    }

    public int getBackgroundMaxTileWidth() {
        return backgroundData != null ? backgroundData[0].length : 0;
    }

    public int getBackgroundMaxTileHeight() {
        return backgroundData != null ? backgroundData.length : 0;
    }

    // -- Decoration data --
    public boolean hasDecoration() { 
        return decorationData != null; 
    } 

    public int getDecorationSpriteIndex(int x, int y) {
        if (decorationData == null) 
            return -1; 
        if (y < 0 || y >= decorationData.length || x < 0 || x >= decorationData[0].length) {
            return -1; 
        }
        return decorationData[y][x]; 
    } 
    
    public int[][] getDecorationData() { 
        return decorationData; 
    }

    public int getDecorationMaxTileWidth() { 
        return decorationData != null ? decorationData[0].length : 0; 
    } 
    public int getDecorationMaxTileHeight() { 
        return decorationData != null ? decorationData.length : 0; 
    }
    // -- Collision --
    public boolean isSolid(int tileX, int tileY){
        return levelData[tileY][tileX] >= 0 ;
    }
    
    public int getMaxTileWidth(){
        return levelData[0].length;
    }
    public int getMaxTileHeight(){
        return levelData.length;
    }

}
