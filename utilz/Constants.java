package utilz;

public class Constants {

    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }




    public static class PlayerConstants{ 
        // public static final int IDLE = 0;       
        // public static final int RUNNING = 1;
        // public static final int DASH_1 = 2;
        // public static final int DASH_2 = 3;
        // public static final int DASH_3 = 4; 
        // public static final int JUMPING = 5;

        public static final int IDLE = 0;       
        public static final int RUNNING = 1;
        public static final int ATTACKING = 2; 
        public static final int JUMPING = 3;
        public static final int FALLING = 4;
        


        public static int GetSpriteAmount(int player_action){

            return switch (player_action) {
                case IDLE -> 4;
                case RUNNING -> 8;
                case ATTACKING -> 8;
                case JUMPING -> 15;
                case FALLING -> 3;
                default -> 0;
            };
        }
    }

}
