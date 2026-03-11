package entities;

import java.awt.image.BufferedImage;
import levels.Level;
import utilz.LoadSave;

public class CrabEnemy extends Enemy {
    private long lastAttackingTime = 0;
    private final long attackCoolDown = 5000;
    private long attackAnimationStartTime = 0;
    private final long attackAnimationDuration = 1000; 
    private final int attackRange = 200;

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
        
        this.patrolEnabled = true;

        // Frame dimensions for sprite sheet
        this.frameWidth = 72;
        this.frameHeight = 32;

        // Rendered size 
        this.enemyWidth = 96;
        this.enemyHeight = 96;

        // Hitbox 
        this.hitboxOffsetX = 20;
        this.hitboxOffsetY = 20;
        this.hitBoxWidth = 56;
        this.hitBoxHeight = 70;

        // Animation frame counts: [idle, run, attack]
        this.animationFrameCounts = new int[]{9, 6, 7};
        this.patrolEnabled = true;
    }

    @Override
    protected void loadAnimations() {
        animations = new BufferedImage[getAnimationCount()][];
    
        animations[0] = loadAnimationFromFile(LoadSave.ENEMY_CRAB_IDLE, 9);
        animations[1] = loadAnimationFromFile(LoadSave.ENEMY_CRAB_RUN, 6);
        animations[2] = loadAnimationFromFile(LoadSave.ENEMY_CRAB_ATTACK, 7);        
    }

    @Override
    protected int getAnimationCount() {
        return 3;
    }

    @Override
    protected void attack() {
        if (player != null) {
            long currentTime = System.currentTimeMillis();
            long cooldownTime = currentTime - lastAttackingTime;

            if (!attacking && cooldownTime >= attackCoolDown) {
                attacking = true;
                enemyAction = 2; // attack
                attackAnimationStartTime = currentTime;
                lastAttackingTime = currentTime;
            }
            if (attacking && (currentTime - attackAnimationStartTime >= attackAnimationDuration)) {
                attacking = false;
                enemyAction = 0;
            }
        }
        checkAttackHit();
    }



    @Override
    protected void setAnimation() {
        int startAnim = enemyAction;
        
        if (attacking) {
            enemyAction = 2; // attack
        } else if (moving) {
            enemyAction = 1; // walk
        } else {
            enemyAction = 0; // idle
        }
    
        // Reset animation index if action changed
        if (startAnim != enemyAction) {
            animIndex = 0;
            animTick = 0;
        }
    }
}
