package entities;

import java.awt.image.BufferedImage;
import levels.Level;
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

        this.patrolEnabled  = true;

        this.animationFrameCounts = new int[]{6, 4, 4, 4}; 
    }

    @Override
    protected void loadAnimations() {
        animations = new BufferedImage[getAnimationCount()][];
        // Idle animation
        animations[0] = loadAnimationFromFile(LoadSave.ENEMY_PK_IDLE, 6);
        animations[1] = loadAnimationFromFile(LoadSave.ENEMY_PK_ATTACK, 4);
        animations[2] = loadAnimationFromFile(LoadSave.ENEMY_PK_DEATH, 4);
        animations[3] = loadAnimationFromFile(LoadSave.ENEMY_PK_HIT, 4);
    }

    @Override
    protected int getAnimationCount() {
        return 4; 
    }

    @Override
    protected void attack() {
        if (player != null) {
            long currentTime = System.currentTimeMillis();
            long cooldownTime = currentTime - lastAttackingTime;

            if (!attacking && cooldownTime >= attackCoolDown) {
                attacking = true;
                enemyAction = 1;
                attackAnimationStartTime = currentTime;
                lastAttackingTime = currentTime;
            }
            
            // Check for attack collision during attack animation
            if (attacking) {
                checkAttackHit();
            }
            
            if (attacking && (currentTime - attackAnimationStartTime >= attackAnimationDuration)) {
                attacking = false;
                enemyAction = 0;
            }   
    
        }
        attackMovement();
    }

    // Override setAnimation()
    @Override
    protected void setAnimation() {
        if (attacking) {
            enemyAction = 1;
        }else if (!alive) {
            enemyAction = 2;
        }else if (stunned) {
            enemyAction = 3;
        } else {
            enemyAction = 0;
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
