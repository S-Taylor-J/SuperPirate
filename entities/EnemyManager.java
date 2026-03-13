package entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import levels.Level;
import utilz.Camera;

// Manages all enemies in the game.
// Handles updating, rendering, and spawning of enemies.
public class EnemyManager {

    private List<Enemy> enemies;
    private Level level;
    private Player player;

    public EnemyManager(Level level) {
        this.level = level;
        this.enemies = new ArrayList<>();
    }

    // Set player reference so enemies can attack
    public void setPlayer(Player player) {
        this.player = player;
        // Update all existing enemies with player reference
        for (Enemy enemy : enemies) {
            enemy.setPlayer(player);
        }
    }

    // Add an enemy to the manager
    public void addEnemy(Enemy enemy) {
        if (player != null) {
            enemy.setPlayer(player);
        }
        enemies.add(enemy);
    }

    // Spawn a PinkFish enemy at the given position
    public void spawnPinkFish(float x, float y) {
        PinkFishEnemy enemy = new PinkFishEnemy(x, y, level);
        if (player != null) {
            enemy.setPlayer(player);
        }
        enemies.add(enemy);
    }

    // Spawn a Crab enemy at the given position
    public void spawnCrab(float x, float y) {
        CrabEnemy enemy = new CrabEnemy(x, y, level);
        if (player != null) {
            enemy.setPlayer(player);
        }
        enemies.add(enemy);
    }

    // Remove dead enemies and update alive ones
    public void update() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (enemy.isAlive()) {
                enemy.update();
            } else {
                iterator.remove();
            }
        }
    }

    // Render all alive enemies
    public void render(Graphics g, Camera camera) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                enemy.render(g, camera);
            }
        }
    }

    // Get the list of all enemies (for collision checks, etc.)
    public List<Enemy> getEnemies() {
        return enemies;
    }

    // Get only alive enemies
    public List<Enemy> getAliveEnemies() {
        List<Enemy> alive = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                alive.add(enemy);
            }
        }
        return alive;
    }

    // Get the number of alive enemies
    public int getAliveCount() {
        int count = 0;
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                count++;
            }
        }
        return count;
    }

    // Clear all enemies
    public void clear() {
        enemies.clear();
    }

    //Set the level
    public void setLevel(Level level) {
        this.level = level;
    }
}
