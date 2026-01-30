package levels;

public class Level {

    private int[][] levelData;

    public Level(int[][] levelData) {
        this.levelData = levelData;
    }

    public int getSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

    public int[][] getLevelData() {
        return levelData;
    }

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
