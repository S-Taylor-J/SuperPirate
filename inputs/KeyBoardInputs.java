package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel;

// implement an interface(mixin)
public class KeyBoardInputs implements KeyListener{

    private GamePanel gamePanel;

    public KeyBoardInputs(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used - keyPressed/keyReleased handle input
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.getGame().keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gamePanel.getGame().keyReleased(e);
    }
}
