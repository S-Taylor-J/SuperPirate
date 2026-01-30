package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import levels.Level;
import utilz.Camera;
import static utilz.Constants.PlayerConstants.*;
import utilz.HelpMethod;
import utilz.LoadSave;


public class Player extends Entity{
    
    private BufferedImage[][] animations;
    private int animTick, animIndex, animSpeed = 10;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down;
    private boolean isFacingRight = true; 
    private final int playerWidth = 136;
    private final int playerHeight = 93;
    private final float playerSpeed = 2.0f;

    // Player Hitbox 
    private final int hitboxOffsetX = 53;
    private final int hitboxOffsetY = 25;
    private final int hitBoxWidth = 30;
    private final int hitBoxHeight = 40;

    // Gravity and jumping
    private float airSpeed = 0f;
    private final float gravity = 0.04f;
    private final float jumpSpeed = -3.5f;
    private final float maxFallSpeed = 10f;
    private boolean inAir = false;

    // Level
    public Player(float x, float y, Level level){
        super(x, y, level);
        loadAnimations();
    }

    public void update(){
        updatePos();
        updateAnimationTick();
        setAnimation();
    }
    
    public void render(Graphics g, Camera camera){
        int drawX = (int)(x - camera.getXOffset());
        int drawY = (int)(y - camera.getYOffset());
        if (isFacingRight) {
            g.drawImage(animations[playerAction][animIndex], drawX, drawY, playerWidth, playerHeight, null);
        } else {
            // Flip horizontally by drawing with negative width, adjust x
            g.drawImage(animations[playerAction][animIndex], drawX + playerWidth, drawY, -playerWidth, playerHeight, null);
        }
        // draw hitbox 
        g.setColor(Color.RED);
        g.drawRect((int)(x + hitboxOffsetX - camera.getXOffset()), (int)(y + hitboxOffsetY - camera.getYOffset()),hitBoxWidth, hitBoxHeight);
    }
    
    //  -- Player Movement --
    private void updatePos() {
        moving = false;

        float nextX = x;
        float nextY = y;

        // Horizontal movement
        if(left && !right){
            nextX -= playerSpeed;
            isFacingRight = false;
        }else if (right && !left){
            nextX += playerSpeed;
            isFacingRight = true;
        }

        // Jumping (only if on ground)
        if(up && !inAir){
            airSpeed = jumpSpeed;
            inAir = true;
        }

        // Apply gravity
        if(inAir) {
            airSpeed += gravity;
            if(airSpeed > maxFallSpeed) {
                airSpeed = maxFallSpeed;
            }
            nextY += airSpeed;
        }

        // Check X movement separately
        if(nextX != x) {
            float hitBoxX = nextX + hitboxOffsetX;
            float hitBoxY = y + hitboxOffsetY;
            
            if(HelpMethod.canMoveHere(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight, level)){
                x = nextX;
                moving = true;
            }
        }

        // Check Y movement separately
        if(inAir) {
            float hitBoxX = x + hitboxOffsetX;
            float hitBoxY = nextY + hitboxOffsetY;
            
            // Check if can move down (falling)
            if(airSpeed > 0) {
                if(HelpMethod.canMoveHere(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight, level)){
                    y = nextY;
                } else {
                    // Hit ground
                    inAir = false;
                    airSpeed = 0;
                }
            } 
            // Check if can move up (jumping)
            else {
                if(HelpMethod.canMoveHere(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight, level)){
                    y = nextY;
                } else {
                    // Hit ceiling
                    airSpeed = 0;
                }
            }
        } else {
            // Check if still on ground
            float hitBoxX = x + hitboxOffsetX;
            float hitBoxY = y + hitboxOffsetY + airSpeed + 1;
            
            if(!HelpMethod.canMoveHere(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight, level)){
                // Still on ground
                inAir = false;
            } else {
                // Started falling
                inAir = true;
            }
        }
    }

    // --   Animations    --
    private void setAnimation() {
        int startAnim = playerAction;
        
        if(inAir) {
            playerAction = JUMPING;
        } else if (moving){
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if(attacking){
            playerAction = DASH_3;
        }
        
        // Reset animation index when switching animations
        if (startAnim != playerAction) {
            animIndex = 0;
            animTick = 0;
        }
    }

    private void updateAnimationTick() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0 ;
            animIndex++;
            if(animIndex >= GetSpriteAmount(playerAction)){
                animIndex = 0; 
                attacking = false;
            }
        } 
    }

    private void loadAnimations() {
        animations = new BufferedImage[20][];
        
        // Load each animation from its own image file with its specific size
        animations[IDLE] = loadAnimationFromFile(LoadSave.PLAYER_IDLE, GetSpriteAmount(IDLE));
        animations[DASH_1] = loadAnimationFromFile(LoadSave.PLAYER_DASH_1, GetSpriteAmount(DASH_1));
        animations[DASH_2] = loadAnimationFromFile(LoadSave.PLAYER_DASH_2, GetSpriteAmount(DASH_2));
        animations[DASH_3] = loadAnimationFromFile(LoadSave.PLAYER_DASH_3, GetSpriteAmount(DASH_3));
        animations[JUMPING] = loadAnimationFromFile(LoadSave.PLAYER_JUMPING, GetSpriteAmount(JUMPING));
        animations[RUNNING] = loadAnimationFromFile(LoadSave.PLAYER_RUNNING, GetSpriteAmount(RUNNING));
    }
    
    private BufferedImage[] loadAnimationFromFile(String path, int frameCount) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        BufferedImage img = LoadSave.getSpriteAtlas(path);
        
        for (int i = 0; i < frameCount; i++) {
            // 512
            frames[i] = img.getSubimage(i * 512, 0, 512, 512);
        }
        
        return frames;
    }

    //  -- Boolean Reset --

    public void resetDirBooleans(){
        left = false;
        right = false;
        down = false;
        up = false;
    }

    // -- Getters & Setters -- 

    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }

    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}