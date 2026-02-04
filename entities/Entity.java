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
}
