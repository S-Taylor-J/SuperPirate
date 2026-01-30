package utilz;

import levels.Level;
import static main.Game.TILES_SIZE;

public class HelpMethod {

    public static boolean isSolid(float x, float y, Level level){
        int tileX = (int) (x/TILES_SIZE);
        int tileY = (int) (y/TILES_SIZE);
        if (tileX < 0 || tileY < 0 || tileX >= level.getMaxTileWidth() || tileY >= level.getMaxTileHeight()) {
            return true;   
        }
        return level.isSolid(tileX, tileY);
    }

    public static boolean canMoveHere(float x, float y, float width, float height, Level level){
        // Check slightly inside the corners (1 pixel inward)
        float offset = 0.1f;
        
        // Top Left corner
        if (isSolid(x + offset, y + offset, level)) return false;
        
        // Top Right corner
        if (isSolid(x + width - offset, y + offset, level)) return false;
        
        // Bottom Left corner
        if (isSolid(x + offset, y + height - offset, level)) return false;
        
        // Bottom Right corner
        if (isSolid(x + width - offset, y + height - offset, level)) return false;
        
    return true;
}
}
