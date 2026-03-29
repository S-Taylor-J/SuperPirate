package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import levels.Level;
import utilz.Camera;
import utilz.HelpMethod;
import utilz.LoadSave;

// Abstract base class for all enemy types.

public abstract class Enemy extends Entity {
    // Animations - protected so subclasses can access
    protected BufferedImage[][] animations;
    protected int animTick, animIndex;
    protected int animSpeed = 15;
    protected int enemyAction = 0;
    protected boolean moving = false;
    protected boolean attacking = false;
    protected boolean stunned = false;

    // Frame dimensions
    protected int frameWidth = 34;
    protected int frameHeight = 30;

    // Enemy attributes
    protected int enemyWidth = 80;
    protected int enemyHeight = 70;
    protected float enemySpeed = 1.0f;

    // Enemy Hitbox
    protected int hitboxOffsetX = 15;
    protected int hitboxOffsetY = 15;
    protected int hitBoxWidth = 50;
    protected int hitBoxHeight = 50;

    // Health
    protected int health = 3;
    protected int maxHealth = 3;
    protected boolean alive = true;
    protected int stunDuration = 1000;
    protected long stunStartTime = 0; 

    // Gravity and falling
    protected float airSpeed = 0f;
    protected float gravity = 0.04f;
    protected float maxFallSpeed = 10f;
    protected boolean inAir = false;

    // Direction
    protected boolean isFacingRight = true;

    // Animation frame counts per action
    protected int[] animationFrameCounts = {6}; // Default to 6 frames for all actions

    // Attack properties
    protected int attackRange = 50;
    protected int attackDamage = 1;
    protected Player player; // Reference to player for attack checks

    // Patrol behavior
    protected boolean patrolEnabled = false;
    protected float patrolLeftBound;
    protected float patrolRightBound;
    protected float patrolDistance = 400f; // Default patrol distance from spawn

    public Enemy(float x, float y, Level level) {
        super(x, y, level);
        // Initialize patrol bounds based on spawn position
        patrolLeftBound = x - patrolDistance;
        patrolRightBound = x + patrolDistance;
    }

    // Set player reference for attack checks
    public void setPlayer(Player player) {
        this.player = player;
    }

    // Enable patrol behavior with custom distance
    public void enablePatrol(float distance) {
        this.patrolEnabled = true;
        this.patrolDistance = distance;
        this.patrolLeftBound = x - distance;
        this.patrolRightBound = x + distance;
    }

    
    // Enable patrol behavior with default distance
    public void enablePatrol() {
        enablePatrol(patrolDistance);
    }

    // Check if attack hits player and deal damage
    protected void checkAttackHit() {
        if (player != null && attacking) {
            Rectangle attackBox = isFacingRight ? getAttackHitboxRight() : getAttackHitboxLeft();
            Rectangle playerHitbox = player.getHitbox();
            if (attackBox.intersects(playerHitbox)) {
                player.takeDamage(attackDamage);
            }
        }
    }

    // Abstract method - each enemy type must load its own animations
    protected abstract void loadAnimations();

    // Get the number of animation actions this enemy has (idle, walk, attack, etc.)
    protected abstract int getAnimationCount();

    // Abstract method for attack behavior - to be implemented by each enemy type
    protected abstract void attack();

    public void update() {
        updateStunState();
        updatePos();
        updateAnimationTick();
        attack();
        setAnimation();
    }

    // Check if stun duration has elapsed and recover
    protected void updateStunState() {
        if (stunned && System.currentTimeMillis() - stunStartTime >= stunDuration) {
            stunned = false;
            patrolEnabled = true;
        }
    }

    // Get enemy's hitbox as a Rectangle (for collision checks)
    public Rectangle getHitbox() {
        return new Rectangle((int) (x + hitboxOffsetX), (int) (y + hitboxOffsetY), hitBoxWidth, hitBoxHeight);
    }

    // Get attack hitbox to the right of the enemy
    protected Rectangle getAttackHitboxRight() {
        int attackX = (int) (x + hitboxOffsetX + hitBoxWidth);
        int attackY = (int) (y + hitboxOffsetY);
        return new Rectangle(attackX, attackY, attackRange, hitBoxHeight);
    }

    // Get attack hitbox to the left of the enemy
    protected Rectangle getAttackHitboxLeft() {
        int attackX = (int) (x + hitboxOffsetX - attackRange);
        int attackY = (int) (y + hitboxOffsetY);
        return new Rectangle(attackX, attackY, attackRange, hitBoxHeight);
    }

    protected Rectangle healthBar() {
        int barWidth = (int) ((health / (float) maxHealth) * enemyWidth);
        return new Rectangle((int) (x - hitboxOffsetX), (int) (y - hitboxOffsetY - 10), barWidth, 5);
    }

    protected void updatePos() {
        moving = false;
        float nextY = y;
        float nextX = x;

        // Patrol movement
        if (patrolEnabled && !attacking) {
            if (isFacingRight) {
                nextX = x + enemySpeed;
                if (nextX >= patrolRightBound) {
                    isFacingRight = false;
                }
            } else {
                nextX = x - enemySpeed;
                if (nextX <= patrolLeftBound) {
                    isFacingRight = true;
                }
            }
        }

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
            } else if (patrolEnabled) {
                // Hit a wall, reverse direction
                isFacingRight = !isFacingRight;
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
        // Draw health bar
        drawHealthBar(g, camera);

        // Draw hitbox (for debugging)
        // g.setColor(Color.GREEN);
        // g.drawRect((int) (x + hitboxOffsetX - camera.getXOffset()), (int) (y + hitboxOffsetY - camera.getYOffset()), hitBoxWidth, hitBoxHeight);
    }
    
    private void drawHealthBar(Graphics g, Camera camera){
        g.setColor(Color.RED);
        Rectangle healthBar = healthBar();
        g.fillRect((int)(healthBar.x - camera.getXOffset()), (int)(healthBar.y - camera.getYOffset()), healthBar.width, healthBar.height);
        g.setColor(Color.BLACK);
        g.drawRect((int)(healthBar.x - camera.getXOffset()), (int)(healthBar.y - camera.getYOffset()), (int) ((health / (float) maxHealth) * enemyWidth), 5);
        
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

    // Helper method to load animation frames from a sprite sheet
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
        knockBack(); 
        System.out.println("Enemy hit! Health: " + health);
        if (health <= 0) {
            alive = false;
            player.addScore(50);
            enemyAction = 3; 
        }
    }
    public void knockBack() {
        stunned = true;
        attacking = false;
        float knockbackStrength = 15f;
        float newX;
        
        if (player.getX() < x) {
            newX = x + knockbackStrength;
        } else {
            newX = x - knockbackStrength;
        }
        
        // Only apply knockback if the new position is valid
        float hitBoxX = newX + hitboxOffsetX;
        float hitBoxY = y + hitboxOffsetY;
        if (HelpMethod.canMoveHere(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight, level)) {
            x = newX;
        }
        
        // Temporarily disable movement and start stun timer
        patrolEnabled = false;
        stunStartTime = System.currentTimeMillis();
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
