package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LoadSave {

    //Player Sprites
    public static final String PLAYER_IDLE = "/res/pirate/idle.png";
    public static final String PLAYER_ATTACKING = "/res/pirate/attack_1.png";
    public static final String PLAYER_JUMPING = "/res/pirate/jump.png";
    public static final String PLAYER_RUNNING = "/res/pirate/run.png";
    public static final String PLAYER_FALLING = "/res/pirate/fall.png";
    
    //Enemy (Pink Fish) sprites
    public static final String ENEMY_PK_IDLE = "/res/enemies/PinkFish/idle.png";

    //Tilesets
    public static final String TERRAIN_TILESET = "/res/Tilesets/Terrain.png";

    public static BufferedImage getSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream(fileName);
        if (is == null) {
            System.err.println("Could not find resource: " + fileName);
            return null;
        }
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

}
