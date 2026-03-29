package levels;

import entities.CollectablePoint;

public class CollectableData {

    public static final CollectablePoint[] LEVEL0_COLLECTABLES = {
        // GOLD (common – spread across map)
        CollectablePoint.fromTile(20, 18, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(35, 17, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(50, 15, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(65, 16, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(70, 12, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(80, 15, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(95, 10, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(110, 11, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(120, 14, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(130, 10, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(140, 16, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(150, 14, CollectablePoint.GOLD, 32),

        // GEM (rare – hard to reach positions)
        CollectablePoint.fromTile(70, 20, CollectablePoint.GEM, 32),
        CollectablePoint.fromTile(145, 9, CollectablePoint.GEM, 32),

        // HEALTH (rare – useful reward)
        // CollectablePoint.fromTile(50, 15, CollectablePoint.HEALTH, 32),
        CollectablePoint.fromTile(125, 13, CollectablePoint.HEALTH, 32),

        // POWERUP / POTION (rare)
        // CollectablePoint.fromTile(20, 23, CollectablePoint.POWERUP, 32),
        CollectablePoint.fromTile(100, 12, CollectablePoint.POWERUP, 32),
    };


    public static final CollectablePoint[] LEVEL1_COLLECTABLES = {
        CollectablePoint.fromTile(15, 10, CollectablePoint.GOLD, 32)
    };
    
    
    public static CollectablePoint[] getCollectables(int levelIndex) {
        return switch (levelIndex) {
            case 0 -> LEVEL0_COLLECTABLES;
            case 1 -> LEVEL1_COLLECTABLES;
            default -> new CollectablePoint[0];
        };
    }

}
