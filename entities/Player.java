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
    // Animations
    private BufferedImage[][] animations;
    private int animTick, animIndex, animSpeed = 10;
    private int playerAction = IDLE;
    private boolean moving = false;
    private int frameWidth = 64;
    private final int frameHeight = 64;

    // Movement Booleans
    private boolean left, up, right, down;
    private boolean isFacingRight = true;


    // Player attributes
    private final int playerWidth = 136;
    private final int playerHeight = 93;
    private final float playerSpeed = 2.0f;

    // Player Hitbox 
    private final int hitboxOffsetX = 53;
    private final int hitboxOffsetY = 25;
    private final int hitBoxWidth = 30;
    private final int hitBoxHeight = 60;

    // Gravity and jumping
    private float airSpeed = 0f;
    private final float gravity = 0.04f;
    private final float jumpSpeed = -4f;
    private final float maxFallSpeed = 10f;
    private boolean inAir = false;

    // Player cooldowns
    private boolean isAttacking = false;
    private long isAttackingStartTime = 0;
    private final long attackingCooldown = 1000; // 1 second cooldown
    private long lastAttackingTime = 0;

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

        // Attacking 
        if(isAttacking && (System.currentTimeMillis() - isAttackingStartTime >= attackingCooldown)) {
            if(isFacingRight) {
                // nextX += playerSpeed * dashMultiplier; // Dash speed multiplier
            } else {
                // nextX -= playerSpeed * dashMultiplier; // Dash speed multiplier
            }
            isAttackingStartTime = System.currentTimeMillis();
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
        if (inAir) {
            if (airSpeed > 0) {
                playerAction = FALLING;
            } else {
                playerAction = JUMPING;
            }
        } else if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
        if (isAttacking) {
            playerAction = ATTACKING;
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
                if (playerAction == ATTACKING) {
                    isAttacking = false;
                }
                if (playerAction == FALLING) {
                    animIndex = GetSpriteAmount(FALLING)- 1; // Stay on last frame of falling
                }
            }
        } 
    }

    private void loadAnimations() {
        // animations = new BufferedImage[20][];
        
        // Load each animation from its own image file with its specific size
        // animations[IDLE] = loadAnimationFromFile(LoadSave.PLAYER_IDLE, GetSpriteAmount(IDLE));
        // animations[DASH_1] = loadAnimationFromFile(LoadSave.PLAYER_DASH_1, GetSpriteAmount(DASH_1));
        // animations[DASH_2] = loadAnimationFromFile(LoadSave.PLAYER_DASH_2, GetSpriteAmount(DASH_2));
        // animations[DASH_3] = loadAnimationFromFile(LoadSave.PLAYER_DASH_3, GetSpriteAmount(DASH_3));
        // animations[JUMPING] = loadAnimationFromFile(LoadSave.PLAYER_JUMPING, GetSpriteAmount(JUMPING));
        // animations[RUNNING] = loadAnimationFromFile(LoadSave.PLAYER_RUNNING, GetSpriteAmount(RUNNING));

        animations = new BufferedImage[8][];
        animations[IDLE] = loadAnimationFromFile(LoadSave.PLAYER_IDLE, GetSpriteAmount(IDLE));
        animations[JUMPING] = loadAnimationFromFile(LoadSave.PLAYER_JUMPING, GetSpriteAmount(JUMPING));
        animations[FALLING] = loadAnimationFromFile(LoadSave.PLAYER_FALLING, GetSpriteAmount(FALLING));
        animations[ATTACKING] = loadAnimationFromFile(LoadSave.PLAYER_ATTACKING, GetSpriteAmount(ATTACKING));
        animations[RUNNING] = loadAnimationFromFile(LoadSave.PLAYER_RUNNING, GetSpriteAmount(RUNNING));

    }
    
    private BufferedImage[] loadAnimationFromFile(String path, int frameCount) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        BufferedImage img = LoadSave.getSpriteAtlas(path);
        
        for (int i = 0; i < frameCount; i++) {
            // 512
            frames[i] = img.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
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

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean isAttacking) {
        if (isAttacking) {
            long now = System.currentTimeMillis();
            if (!this.isAttacking && (now - lastAttackingTime >= attackingCooldown)) {
                this.isAttacking = true;
                isAttackingStartTime = now;
                lastAttackingTime = now;
            }
        } else {
            this.isAttacking = false;
        }
    }
}