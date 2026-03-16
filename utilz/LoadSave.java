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
    public static final String ENEMY_PK_ATTACK = "/res/enemies/PinkFish/attack.png";
    public static final String ENEMY_PK_DEATH = "/res/enemies/PinkFish/death.png";
    public static final String ENEMY_PK_HIT = "/res/enemies/PinkFish/hit.png";

    //Enemy (Crab Enemy) sprites 
    public static final String ENEMY_CRAB_IDLE = "/res/enemies/Crab/idle.png";
    public static final String ENEMY_CRAB_RUN = "/res/enemies/Crab/run.png";
    public static final String ENEMY_CRAB_ATTACK = "/res/enemies/Crab/attack.png";
    public static final String ENEMY_CRAB_DEATH = "/res/enemies/Crab/death.png";
    public static final String ENEMY_CRAB_HIT = "/res/enemies/Crab/hit.png";
    
    // Collectibles
    public static final String COLLECTIBLE_COIN = "/res/collectibles/coin.png";
    public static final String COLLECTIBLE_GEM = "/res/collectibles/skull.png";
    public static final String COLLECTIBLE_HEALTH_POTION = "/res/collectibles/greenPotion.png";
    public static final String COLLECTIBLE_MANA_POTION = "/res/collectibles/bluePotion.png";

    //Tilesets
    public static final String TERRAIN_TILESET = "/res/Tilesets/Terrain.png";
    public static final String SHIP_TILESET = "/res/Tilesets/ship.png";
    public static final String DOOR = "/res/Tilesets/door.png";

    // UI Elements
    public static final String UI_HEALTH_BAR_FRAME = "/res/UI/Life Bars/Big Bars/1.png";
    public static final String UI_HEALTH_BAR_FILL = "/res/UI/Life Bars/Colors/1.png"; // Red health fill
    public static final String UI_HEALTH_BAR_BG = "/res/UI/Life Bars/Colors/4.png";   // Dark background
    public static final String UI_INVENTORY_SLOT = "/res/UI/Inventory/1.png";
    public static final String UI_SMALL_BANNER = "/res/UI/Small Banner/1.png";
    public static final String UI_YELLOW_PAPER = "/res/UI/Yellow Paper/1.png";
    public static final String UI_GOLD_COIN = "/res/Treasure/Gold Coin/01.png";

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
