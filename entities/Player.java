package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import levels.Level;
import utilz.Camera;
import static utilz.Constants.PlayerConstants.*;
import utilz.HelpMethod;
import utilz.LoadSave;


public class Player extends Entity{
    // Animations
    private BufferedImage[][] animations;
    private int animTick, animIndex, animSpeed = 15;
    private int playerAction = IDLE;
    private boolean moving = false;

    // Frame dimensions for sprite sheet
    private final int frameWidth = 64;
    private final int frameHeight = 40;

    // Movement Booleans
    private boolean left, up, right, down;
    private boolean isFacingRight = true;

    // Player attributes
    private final int playerWidth = 128;
    private final int playerHeight = 80;
    private final float playerSpeed = 2.0f;

    // Player Hitbox 
    private final int hitboxOffsetX = 50;
    private final int hitboxOffsetY = 25;
    private final int hitBoxWidth = 30;
    private final int hitBoxHeight = 40;

    // Gravity and jumping
    private float airSpeed = 0f;
    private final float gravity = 0.04f;
    private final float jumpSpeed = -4.2f;
    private final float maxFallSpeed = 10f;
    private boolean inAir = false;

    // Player cooldowns
    private boolean isAttacking = false;
    private long isAttackingStartTime = 0;
    private final long attackingCooldown = 1000;
    private long lastAttackingTime = 0;
    private boolean attackChecked = false;
    private final long invulnerabilityDuration = 1000;
    private long lastDamageTime = 0;

    //Player data
    private int health = 5;
    private boolean alive = true;
    private boolean invulnerable = false;

    // Attack range check
    private final int attackRange = 100;

    private EnemyManager enemyManager;
    public Player(float x, float y, Level level) {
        super(x, y, level);
        loadAnimations();
    }

    public void setEnemyManager(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    public void update() {
        if (!alive) {
            System.out.println("Player is dead. No update.");
            return; // Skip update if player is dead
        }
        updatePos();
        checkAttacks();
        updateAnimationTick();
        setAnimation();
        checkEnemyCollisions();
        checkIfAlive();
    }

    public void render(Graphics g, Camera camera){
        int drawX = (int)(x - camera.getXOffset());
        int drawY = (int)(y - camera.getYOffset());
        if (isFacingRight) {
            g.drawImage(animations[playerAction][animIndex], drawX, drawY, playerWidth, playerHeight, null);
        } else {
            g.drawImage(animations[playerAction][animIndex], drawX + playerWidth, drawY, -playerWidth, playerHeight, null);
        }

        // draw hitbox 
        g.setColor(Color.RED);
        g.drawRect((int)(x + hitboxOffsetX - camera.getXOffset()), (int)(y + hitboxOffsetY - camera.getYOffset()),hitBoxWidth, hitBoxHeight);

        // Draw attack range (solid blue, not translucent)
        g.setColor(Color.BLUE);
        float playerCenterX = x + hitboxOffsetX + hitBoxWidth / 2f - camera.getXOffset();
        float playerCenterY = y + hitboxOffsetY + hitBoxHeight / 2f - camera.getYOffset();
        int rangeX = (int) (isFacingRight ? playerCenterX : playerCenterX - attackRange);
        int rangeWidth = attackRange;
        g.fillOval(rangeX, (int)(playerCenterY - 20), rangeWidth, 40);
    }

    // -- Attacking --
    private void checkAttacks() {
        if (isAttacking && enemyManager != null) {
            for (Enemy enemy : enemyManager.getAliveEnemies()) {
                checkAttack(enemy);
            }
        }
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

            } else {

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
            playerAction = ATTACK_1;
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
                if (playerAction == ATTACK_1) {
                    isAttacking = false;
                    attackChecked = false; // Reset for next attack
                }
                if (playerAction == FALLING) {
                    animIndex = GetSpriteAmount(FALLING)- 1; // Stay on last frame of falling
                }
                if (playerAction == JUMPING) {
                    animIndex = GetSpriteAmount(JUMPING) - 1; // Stay on last frame of jumping
                }
            }
        } 
    }

    private void loadAnimations() {
        animations = new BufferedImage[6][];
        animations[IDLE] = loadAnimationFromFile(LoadSave.PLAYER_IDLE, GetSpriteAmount(IDLE));
        animations[JUMPING] = loadAnimationFromFile(LoadSave.PLAYER_JUMPING, GetSpriteAmount(JUMPING));
        animations[FALLING] = loadAnimationFromFile(LoadSave.PLAYER_FALLING, GetSpriteAmount(FALLING));
        animations[ATTACK_1] = loadAnimationFromFile(LoadSave.PLAYER_ATTACKING, GetSpriteAmount(ATTACK_1));
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

    public int getHealth() {
        return health;
    }

    public int getWidth() {
        return playerWidth;
    }

    // -- Attacking --
    public boolean isEnemyInRange(Enemy e) {
        // Get player's attack hitbox center
        float playerCenterX = x + hitboxOffsetX + hitBoxWidth / 2f;
        float playerCenterY = y + hitboxOffsetY + hitBoxHeight / 2f;

        // Get enemy's hitbox center
        float enemyCenterX = e.getX() + e.getHitboxOffsetX() + e.getHitBoxWidth() / 2f;
        float enemyCenterY = e.getY() + e.getHitboxOffsetY() + e.getHitBoxHeight() / 2f;

        // Check direction - only hit enemies in front of player
        if (isFacingRight && enemyCenterX < playerCenterX) {
            return false;
        }
        if (!isFacingRight && enemyCenterX > playerCenterX) {
            return false;
        }

        // Calculate distance
        float dx = playerCenterX - enemyCenterX;
        float dy = playerCenterY - enemyCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return distance <= attackRange;
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

    private void checkAttack(Enemy e) {
        if (isAttacking && !attackChecked && isEnemyInRange(e)) {
            e.takeDamage(1);
            attackChecked = true;
        }
    }

    private void checkEnemyCollisions() {
        if (enemyManager != null) {
            for (Enemy enemy : enemyManager.getAliveEnemies()) {
                if (getHitbox().intersects(enemy.getHitbox())) {
                    takeDamage(1);
                    break; // Only take damage from one enemy per update
                }
            }
        }
    }

    public void checkIfAlive() {
        if (health <= 0) {
            alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    
    public Rectangle getHitbox() {
        return new Rectangle((int) (x + hitboxOffsetX), (int) (y + hitboxOffsetY), hitBoxWidth, hitBoxHeight);
    }

    public void takeDamage(int damage) {
        long now = System.currentTimeMillis();
        if (!invulnerable || (now - lastDamageTime >= invulnerabilityDuration)) {
            health -= damage;
            invulnerable = true;
            lastDamageTime = now;
            knockBack();
        }
    }

    // TODO - Add invulnerability flashing effect in render method and knock back effect in updatePos method when taking damage
    private void knockBack(){
        x += isFacingRight ? -30: 30; // Knock back in opposite direction of facing
        y -= 30; // Knock up slightly

    }

}