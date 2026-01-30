package utilz;

public class Constants {

    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }




    public static class PlayerConstants{ 
        public static final int IDLE = 0;       
        public static final int RUNNING = 1;
        public static final int DASH_1 = 2;
        public static final int DASH_2 = 3;
        public static final int DASH_3 = 4; 
        public static final int JUMPING = 5;


        public static int GetSpriteAmount(int player_action){

            return switch (player_action) {
                case IDLE -> 19;
                case RUNNING -> 20;
                case DASH_1 -> 16;
                case DASH_2 -> 16;
                case DASH_3 -> 16;
                case JUMPING -> 8;
                default -> 0;
            };
        }
    }

}
