package entities;

public class SpawnPoint {

    // Entity type constants
    public static final int PLAYER = 0;
    public static final int PINKFISH = 1;
    public static final int PIRATE = 2;

    private int x, y;
    private int entityType;

    public SpawnPoint(int x, int y, int entityType) { 
        this.x = x; 
        this.y = y;
        this.entityType = entityType;
    }

    public static SpawnPoint fromTile(int tileX, int tileY, int entityType, int tileSize) {
        return new SpawnPoint(tileX * tileSize, tileY * tileSize, entityType);
    }
    
    public int getX() { 
        return x; 
    } 

    public int getY() { 
        return y; 
    }

    public int getEntityType() {
        return entityType;
    }

    public boolean isPlayer() {
        return entityType == PLAYER;
    }

    public boolean isEnemy() {
        return entityType != PLAYER;
    }

    @Override
    public String toString() {
        String typeName = switch(entityType) {
            case PLAYER -> "Player";
            case PINKFISH -> "PinkFish";
            case PIRATE -> "Pirate";
            default -> "Unknown";
        };
        return "SpawnPoint[" + typeName + " at (" + x + ", " + y + ")]";
    }
}
