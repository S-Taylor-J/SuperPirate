package entities;

import java.awt.image.BufferedImage;
import levels.Level;
import utilz.LoadSave;

/**
 * PinkFish enemy type - a basic enemy with idle animation.
 * Customize attributes and animations for this specific enemy type.
 */
public class PinkFishEnemy extends Enemy {

    public PinkFishEnemy(float x, float y, Level level) {
        super(x, y, level);
        initAttributes();
        loadAnimations();
    }

    /**
     * Set PinkFish-specific attributes
     */
    private void initAttributes() {
        // Health
        this.health = 3;
        this.maxHealth = 3;

        // Speed
        this.enemySpeed = 1.0f;

        // Frame dimensions for sprite sheet
        this.frameWidth = 34;
        this.frameHeight = 30;

        // Rendered size
        this.enemyWidth = 80;
        this.enemyHeight = 70;

        // Hitbox
        this.hitboxOffsetX = 15;
        this.hitboxOffsetY = 15;
        this.hitBoxWidth = 50;
        this.hitBoxHeight = 50;

        // Animation frame counts: [idle, walk, attack, etc.]
        this.animationFrameCounts = new int[]{6, 6}; // 6 frames for idle and attack
    }

    @Override
    protected void loadAnimations() {
        animations = new BufferedImage[getAnimationCount()][];
        // Idle animation
        animations[0] = loadAnimationFromFile(LoadSave.ENEMY_PK_IDLE, 6);
        animations[1] = loadAnimationFromFile(LoadSave.ENEMY_PK_ATTACK, 4);
    }

    @Override
    protected int getAnimationCount() {
        return 2; 
    }

    @Override
    protected void attack() {
        // star fish attack is a spin attack when he move to the left or right of the player, he will do a spin attack that can hit the player if they are close enough
        if (isFacingRight) {
            // Attack to the right
            if (player.getHitbox().intersects(getAttackHitboxRight())) {
                player.takeDamage(1);
            }
        } else {
            // Attack to the left
            if (player.getHitbox().intersects(getAttackHitboxLeft())) {
                player.takeDamage(1);
            }
        }
    }

    // Override setAnimation() if you want custom behavior
    // @Override
    // protected void setAnimation() {
    //     if (moving) {
    //         enemyAction = 1; // walk
    //     } else {
    //         enemyAction = 0; // idle
    //     }
    // }
}
