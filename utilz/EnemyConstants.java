package utilz;

public class EnemyConstants {


    public static class PinkFishEnemyConstants {
        public static final int IDLE = 0;
        public static final int ATTACK = 1;
    }

    public static int GetSpriteAmount(int enemy_action) {
        return switch (enemy_action) {
            case PinkFishEnemyConstants.IDLE -> 6;
            case PinkFishEnemyConstants.ATTACK -> 4;
            default -> 0;
        };
    }

}
