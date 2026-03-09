package levels;

import entities.CollectablePoint;

public class CollectableData {
    // Example collectable points for Level 0
    public static final CollectablePoint[] LEVEL0_COLLECTABLES = {
        CollectablePoint.fromTile(95, 10, CollectablePoint.GOLD, 32),
        CollectablePoint.fromTile(20, 23, CollectablePoint.POWERUP, 32)
    };

    // Example collectable points for Level 1
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
