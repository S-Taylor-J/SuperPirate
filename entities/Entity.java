package entities;

import levels.Level;

public abstract class Entity {

    protected float x, y;
    protected Level level;

    public Entity(float x, float y, Level level) {
        this.x = x;
        this.y = y;
        this.level = level;
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void setLevel(Level level) {
        this.level = level;
    }
}
