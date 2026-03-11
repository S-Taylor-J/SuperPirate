package entities;

import java.awt.image.BufferedImage;
import levels.Level;
import static utilz.EnemyConstants.PinkFishEnemyConstants.*;
import utilz.LoadSave;
public class PinkFishEnemy extends Enemy {
    private long lastAttackingTime = 0;
    private final long attackCoolDown = 5000;
    private long attackAnimationStartTime = 0;
    private final long attackAnimationDuration = 1000; 
    private final int attackRange = 200;

    public PinkFishEnemy(float x, float y, Level level) {
        super(x, y, level);
        initAttributes();
        loadAnimations();
    }

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

        this.animationFrameCounts = new int[]{6, 4}; 
    }

    @Override
    protected void loadAnimations() {
        animations = new BufferedImage[getAnimationCount()][];
        // Idle animation
        animations[IDLE] = loadAnimationFromFile(LoadSave.ENEMY_PK_IDLE, 9);
        animations[ATTACK] = loadAnimationFromFile(LoadSave.ENEMY_PK_ATTACK, 4);
    }

    @Override
    protected int getAnimationCount() {
        return 2; 
    }

    @Override
    protected void attack() {
        if (player != null) {
            long currentTime = System.currentTimeMillis();
            long cooldownTime = currentTime - lastAttackingTime;

            if (!attacking && cooldownTime >= attackCoolDown) {
                attacking = true;
                enemyAction = ATTACK;
                attackAnimationStartTime = currentTime;

                // // Check if player is within attack range
                // float distanceToPlayer = Math.abs((x + enemyWidth / 2) - (player.getX() + player.getWidth() / 2));
                // if (distanceToPlayer <= attackRange && player.getHitbox().intersects(getHitbox())) {
                //     player.takeDamage(attackDamage);
                // }
                lastAttackingTime = currentTime;
                
            }
            if (attacking && (currentTime - attackAnimationStartTime >= attackAnimationDuration)) {
                attacking = false;
                enemyAction = IDLE;
            }   
    
        }
        attackMovement();
    }

    // Override setAnimation() if you want custom behavior
    @Override
    protected void setAnimation() {
        if (attacking) {
            enemyAction = ATTACK;
        } else {
            enemyAction = IDLE;
        }
    }

    private void attackMovement(){
        // Move towards the player while attacking if they are within attack range
        float distanceToPlayer = Math.abs((x + enemyWidth / 2) - (player.getX() + player.getWidth() / 2));
        if(distanceToPlayer <= attackRange && attacking){
            if (x < player.getX()) {
                x += enemySpeed; // Move right
            } else if (x > player.getX()) {
                x -= enemySpeed; // Move left
            }
        }
    }
}
