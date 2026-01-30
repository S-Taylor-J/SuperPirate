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

    public LevelHandler(Game game){
        this.game = game;
        importOutsideSprite();
        createLevel();
    }

    private void createLevel() {
        // Define your level directly as a 2D array
        // -1 = empty, 0-48 = tile indices from tileset
        int[][] levelData = {
            {-1, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,15, 15, 15, 15, 2},
            {9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5},
            {9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5},
            {9, -1, -1, -1, 9, 1-1, -1, -1, -1, -1, -1, -1, -1, 9, 1-1, -1, -1, -1, -1, 5},
            {9, -1, -1, -1, 11, 12, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1, 5},
            {9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5},
            {9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5},
            {9, -1, -1, -1, -1, -1, -1, -1, 9, 1-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5},
            {9, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5},
            {6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 8}
            };
        
        level = new Level(levelData);
    }

    private void importOutsideSprite() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.MOSS_TILESET);
        // 49 = tileset = 7x7 
        levelSprite = new BufferedImage[49];
        for(int i = 0; i < 7; i++){
            for(int j = 0 ; j < 7; j++){
                int index = i * 7 + j;
                levelSprite[index] = img.getSubimage(j* 512, i*512, 512, 512);
            }
        } 
    }

    public void draw(Graphics g, Camera camera){
        // Calculate which tiles are visible on screen
        int startX = (int)(camera.getXOffset() / Game.TILES_SIZE);
        int startY = (int)(camera.getYOffset() / Game.TILES_SIZE);
        int endX = (int)((camera.getXOffset() + Game.SCREEN_WIDTH) / Game.TILES_SIZE) + 1;
        int endY = (int)((camera.getYOffset() + Game.SCREEN_HEIGHT) / Game.TILES_SIZE) + 1;
        
        // Clamp to map bounds
        startX = Math.max(0, startX);
        startY = Math.max(0, startY);
        endX = Math.min(Game.MAP_TILES_WIDTH, endX);
        endY = Math.min(Game.MAP_TILES_HEIGHT, endY);
        
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
