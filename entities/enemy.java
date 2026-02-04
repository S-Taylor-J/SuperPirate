package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import levels.Level;
import utilz.Camera;
import utilz.HelpMethod;
import utilz.LoadSave;

/**
 * Abstract base class for all enemy types.
 * Subclasses must implement loadAnimations() and provide their own attributes.
 */
public abstract class Enemy extends Entity {
    // Animations - protected so subclasses can access
    protected BufferedImage[][] animations;
    protected int animTick, animIndex;
    protected int animSpeed = 15;
    protected int enemyAction = 0; // 0 = idle
    protected boolean moving = false;

    // Frame dimensions - override in subclass if different
    protected int frameWidth = 34;
    protected int frameHeight = 30;

    // Enemy attributes - override in subclass for different stats
    protected int enemyWidth = 80;
    protected int enemyHeight = 70;
    protected float enemySpeed = 1.0f;

    // Enemy Hitbox - override in subclass if different
    protected int hitboxOffsetX = 15;
    protected int hitboxOffsetY = 15;
    protected int hitBoxWidth = 50;
    protected int hitBoxHeight = 50;

    // Health
    protected int health = 3;
    protected int maxHealth = 3;
    protected boolean alive = true;

    // Gravity and falling
    protected float airSpeed = 0f;
    protected float gravity = 0.04f;
    protected float maxFallSpeed = 10f;
    protected boolean inAir = false;

    // Direction
    protected boolean isFacingRight = true;

    // Animation frame counts per action - override in subclass
    protected int[] animationFrameCounts = {6}; // Default: 6 frames for idle

    // Attack properties
    protected int attackRange = 50;
    protected int attackDamage = 1;
    protected Player player; // Reference to player for attack checks

    public Enemy(float x, float y, Level level) {
        super(x, y, level);
        // Subclass must call loadAnimations() in their constructor
    }

    /**
     * Set player reference for attack checks
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Abstract method - each enemy type must load its own animations
     */
    protected abstract void loadAnimations();

    /**
     * Get the number of animation actions this enemy has (idle, walk, attack, etc.)
     */
    protected abstract int getAnimationCount();

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    /**
     * Get enemy's hitbox as a Rectangle (for collision checks)
     */
    public Rectangle getHitbox() {
        return new Rectangle((int) (x + hitboxOffsetX), (int) (y + hitboxOffsetY), hitBoxWidth, hitBoxHeight);
    }

    /**
     * Get attack hitbox to the right of the enemy
     */
    protected Rectangle getAttackHitboxRight() {
        int attackX = (int) (x + hitboxOffsetX + hitBoxWidth);
        int attackY = (int) (y + hitboxOffsetY);
        return new Rectangle(attackX, attackY, attackRange, hitBoxHeight);
    }

    /**
     * Get attack hitbox to the left of the enemy
     */
    protected Rectangle getAttackHitboxLeft() {
        int attackX = (int) (x + hitboxOffsetX - attackRange);
        int attackY = (int) (y + hitboxOffsetY);
        return new Rectangle(attackX, attackY, attackRange, hitBoxHeight);
    }

    protected void attack() {
        // Default attack behavior - can be overridden by subclasses
        // For example, you could set enemyAction to an attack animation and reset animIndex
        enemyAction = 1; // Assuming index 1 is attack animation
        animIndex = 0;
    }

    protected void updatePos() {
        moving = false;
        float nextY = y;
        float nextX = x;

        if (inAir) {
            airSpeed += gravity;
            if (airSpeed > maxFallSpeed) {
                airSpeed = maxFallSpeed;
            }
            nextY += airSpeed;
        }

        // Check X movement separately
        if (nextX != x) {
            float hitBoxX = nextX + hitboxOffsetX;
            float hitBoxY = y + hitboxOffsetY;

            if (HelpMethod.canMoveHere(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight, level)) {
                x = nextX;
                moving = true;
            }
        }

        if (inAir) {
            float hitBoxX = x + hitboxOffsetX;
            float hitBoxY = nextY + hitboxOffsetY;

            // Check if can move down (falling)
            if (airSpeed > 0) {
                if (HelpMethod.canMoveHere(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight, level)) {
                    y = nextY;
                } else {
                    // Hit ground
                    inAir = false;
                    airSpeed = 0;
                }
            }
            // Check if can move up (jumping)
            else {
                if (HelpMethod.canMoveHere(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight, level)) {
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

            if (!HelpMethod.canMoveHere(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight, level)) {
                // Still on ground
                inAir = false;
            } else {
                // Started falling
                inAir = true;
            }
        }
    }

    public void render(Graphics g, Camera camera) {
        int drawX = (int) (x - camera.getXOffset());
        int drawY = (int) (y - camera.getYOffset());

        if (animations != null && animations[enemyAction] != null) {
            if (isFacingRight) {
                g.drawImage(animations[enemyAction][animIndex], drawX, drawY, enemyWidth, enemyHeight, null);
            } else {
                // Flip horizontally
                g.drawImage(animations[enemyAction][animIndex], drawX + enemyWidth, drawY, -enemyWidth, enemyHeight, null);
            }
        }

        // Draw hitbox (for debugging)
        g.setColor(Color.GREEN);
        g.drawRect((int) (x + hitboxOffsetX - camera.getXOffset()), (int) (y + hitboxOffsetY - camera.getYOffset()), hitBoxWidth, hitBoxHeight);
    }

    protected void setAnimation() {
        // Default: idle animation. Override in subclass for more complex behavior.
        enemyAction = 0;
    }

    protected void updateAnimationTick() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            int frameCount = (animationFrameCounts != null && enemyAction < animationFrameCounts.length)
                    ? animationFrameCounts[enemyAction]
                    : 6;
            if (animIndex >= frameCount) {
                animIndex = 0;
            }
        }
    }

    /**
     * Helper method to load animation frames from a sprite sheet
     */
    protected BufferedImage[] loadAnimationFromFile(String path, int frameCount) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        BufferedImage img = LoadSave.getSpriteAtlas(path);

        if (img != null) {
            for (int i = 0; i < frameCount; i++) {
                frames[i] = img.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
        }

        return frames;
    }

    // Getters
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getHitboxOffsetX() {
        return hitboxOffsetX;
    }

    public int getHitboxOffsetY() {
        return hitboxOffsetY;
    }

    public int getHitBoxWidth() {
        return hitBoxWidth;
    }

    public int getHitBoxHeight() {
        return hitBoxHeight;
    }

    public void takeDamage(int damage) {
        health -= damage;
        System.out.println("Enemy hit! Health: " + health);
        if (health <= 0) {
            alive = false;
            System.out.println("Enemy defeated!");
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public float getEnemySpeed() {
        return enemySpeed;
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }

    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;
    }
}
