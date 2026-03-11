package entities;

public class CollectablePoint {
    public static final int GOLD = 0;
    public static final int POWERUP = 1;
    public static final int HEALTH = 2;

    private int x, y;
    private int type;

    public CollectablePoint(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public static CollectablePoint fromTile(int tileX, int tileY, int type, int tileSize) {
        return new CollectablePoint(tileX * tileSize, tileY * tileSize, type);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getType() { return type; }

    @Override
    public String toString() {
        String typeName = switch(type) {
            case GOLD -> "Gold";
            case POWERUP -> "Powerup";
            case HEALTH -> "Health";
            default -> "Unknown";
        };
        return "CollectablePoint[" + typeName + " at (" + x + ", " + y + ")]";
    }
}
