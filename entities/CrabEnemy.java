package entities;

import java.awt.image.BufferedImage;
import levels.Level;
import utilz.LoadSave;

public class CrabEnemy extends Enemy {

    public CrabEnemy(float x, float y, Level level) {
        super(x, y, level);
        initAttributes();
        loadAnimations();
    }

    private void initAttributes() {
        // Health - crabs are tougher
        this.health = 5;
        this.maxHealth = 5;

        // Speed - crabs are slower
        this.enemySpeed = 0.7f;

        // Frame dimensions for sprite sheet (adjust based on your sprite)
        this.frameWidth = 72;
        this.frameHeight = 32;

        // Rendered size - crabs are bigger
        this.enemyWidth = 96;
        this.enemyHeight = 96;

        // Hitbox - larger hitbox for bigger enemy
        this.hitboxOffsetX = 20;
        this.hitboxOffsetY = 20;
        this.hitBoxWidth = 56;
        this.hitBoxHeight = 70;

        // Animation frame counts: [idle, walk, attack]
        this.animationFrameCounts = new int[]{4, 6, 5}; // Example: 4 idle, 6 walk, 5 attack frames
    }

    @Override
    protected void loadAnimations() {
        animations = new BufferedImage[getAnimationCount()][];
        
        // TODO: Replace with actual sprite paths once you have them
        animations[0] = loadAnimationFromFile(LoadSave.ENEMY_CRAB_IDLE, 4);
        animations[1] = loadAnimationFromFile(LoadSave.ENEMY_CRAB_RUN, 6);
        animations[2] = loadAnimationFromFile(LoadSave.ENEMY_CRAB_ATTACK, 5);
        
    }

    @Override
    protected int getAnimationCount() {
        return 3; // Change to 3 when you have walk and attack animations
    }

    /**
     * Override setAnimation for custom behavior
     * Uncomment when you have more animations
     */
    // @Override
    // protected void setAnimation() {
    //     int startAnim = enemyAction;
        
    //     if (isAttacking) {
    //         enemyAction = 2; // attack
    //     } else if (moving) {
    //         enemyAction = 1; // walk
    //     } else {
    //         enemyAction = 0; // idle
    //     }
        
    //     // Reset animation index if action changed
    //     if (startAnim != enemyAction) {
    //         animIndex = 0;
    //         animTick = 0;
    //     }
    // }
}
