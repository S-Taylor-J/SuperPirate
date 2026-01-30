package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LoadSave {

    //Player Sprites
    public static final String PLAYER_IDLE = "/res/char/idle.png";
    public static final String PLAYER_DASH_1 = "/res/char/dash_1.png";
    public static final String PLAYER_DASH_2 = "/res/char/dash_2.png";
    public static final String PLAYER_DASH_3 = "/res/char/dash_3.png";
    public static final String PLAYER_JUMPING = "/res/char/jump.png";
    public static final String PLAYER_RUNNING = "/res/char/walk.png";

    //Tilesets
    public static final String MOSS_TILESET = "/res/Tilesets/moss.png";

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
