package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import main.Game;

public abstract class State {
    
    protected Game game;
    
    public State(Game game) {
        this.game = game;
    }
    
    public abstract void update();
    
    public abstract void render(Graphics g);
    
    public abstract void keyPressed(KeyEvent e);
    
    public abstract void keyReleased(KeyEvent e);
    
    public abstract void mousePressed(MouseEvent e);
    
    public abstract void mouseReleased(MouseEvent e);
    
    public abstract void mouseMoved(MouseEvent e);
    
    public Game getGame() {
        return game;
    }
}
