package utilz;

import entities.Player;
import main.Game;

public class Camera {
    
    private float xOffset, yOffset;
    private int mapWidth, mapHeight;
    
    public Camera(int mapWidthInTiles, int mapHeightInTiles) {
        this.mapWidth = (int)(mapWidthInTiles * Game.TILES_SIZE);
        this.mapHeight = (int)(mapHeightInTiles * Game.TILES_SIZE);
    }
    
    public void update(Player player) {
        // Center the camera on the player
        xOffset = player.getX() - Game.SCREEN_WIDTH / 2;
        yOffset = player.getY() - Game.SCREEN_HEIGHT / 2;
        
        // Clamp camera to map bounds
        if (xOffset < 0) {
            xOffset = 0;
        }
        if (yOffset < 0) {
            yOffset = 0;
        }
        if (xOffset > mapWidth - Game.SCREEN_WIDTH) {
            xOffset = mapWidth - Game.SCREEN_WIDTH;
        }
        if (yOffset > mapHeight - Game.SCREEN_HEIGHT) {
            yOffset = mapHeight - Game.SCREEN_HEIGHT;
        }
    }
    
    public float getXOffset() {
        return xOffset;
    }
    
    public float getYOffset() {
        return yOffset;
    }
    
    public void setMapSize(int mapWidthInTiles, int mapHeightInTiles) {
        this.mapWidth = (int)(mapWidthInTiles * Game.TILES_SIZE);
        this.mapHeight = (int)(mapHeightInTiles * Game.TILES_SIZE);
    }
}
