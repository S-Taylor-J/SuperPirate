package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import levels.Level;
import utilz.Camera;
import utilz.HelpMethod;
import utilz.LoadSave;

public class enemy extends Entity {
    // Animations
    private BufferedImage[][] animations;
    private int animTick, animIndex, animSpeed = 15;
    private int enemyAction = 0; // 0 = idle
    private boolean moving = false;
    private final int frameWidth = 34;
    private final int frameHeight = 30;

    // Enemy attributes (same scale as player: 2x original size)
    private final int enemyWidth = 80;
    private final int enemyHeight = 70;
    private final float enemySpeed = 1.0f;

    // Enemy Hitbox
    private final int hitboxOffsetX = 15;
    private final int hitboxOffsetY = 15;
    private final int hitBoxWidth = 50;
    private final int hitBoxHeight = 50;

    // Health
    private int health = 3;
    private boolean alive = true;

    // Gravity and falling
    private float airSpeed = 0f;
    private final float gravity = 0.04f;
    private final float maxFallSpeed = 10f;
    private boolean inAir = false;

    // Direction
    private boolean isFacingRight = true;

    public enemy(float x, float y, Level level) {
        super(x, y, level);
        loadAnimations();
    }

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g, Camera camera) {
        int drawX = (int) (x - camera.getXOffset());
        int drawY = (int) (y - camera.getYOffset());

        if (isFacingRight) {
            g.drawImage(animations[enemyAction][animIndex], drawX, drawY, enemyWidth, enemyHeight, null);
        } else {
            // Flip horizontally
            g.drawImage(animations[enemyAction][animIndex], drawX + enemyWidth, drawY, -enemyWidth, enemyHeight, null);
        }

        // Draw hitbox (for debugging)
        g.setColor(Color.GREEN);
        g.drawRect((int) (x + hitboxOffsetX - camera.getXOffset()), (int) (y + hitboxOffsetY - camera.getYOffset()), hitBoxWidth, hitBoxHeight);
    }

    private void updatePos() {
        moving = false;
        float nextY = y;
        float nextX = x;

        if(inAir) {
            airSpeed += gravity;
            if(airSpeed > maxFallSpeed) {
                airSpeed = maxFallSpeed;
            }
            nextY += airSpeed;
        }
        // } else {
        //     // Simple horizontal patrol movement
        //     if(isFacingRight) {
        //         nextX += enemySpeed;
        //     } else {
        //         nextX -= enemySpeed;
        //     }
        // }



        // Check X movement separately
        if(nextX != x) {
            float hitBoxX = nextX + hitboxOffsetX;
            float hitBoxY = y + hitboxOffsetY;
            
            if(HelpMethod.canMoveHere(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight, level)){
                x = nextX;
                moving = true;
            }
        }

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

    private void setAnimation() {
        // Currently only idle animation
        enemyAction = 0;
    }

    private void updateAnimationTick() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if (animIndex >= 6) {
                animIndex = 0;
            }
        }
    }

    private void loadAnimations() {
        animations = new BufferedImage[1][];
        animations[0] = loadAnimationFromFile(LoadSave.ENEMY_PK_IDLE, 6);
    }

    private BufferedImage[] loadAnimationFromFile(String path, int frameCount) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        BufferedImage img = LoadSave.getSpriteAtlas(path);

        for (int i = 0; i < frameCount; i++) {
            frames[i] = img.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
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
}
