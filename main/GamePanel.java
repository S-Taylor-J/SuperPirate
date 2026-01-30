package main;

import inputs.KeyBoardInputs;
import inputs.MouseInputs;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;



public class GamePanel extends JPanel {
    
    private MouseInputs mouseInputs;
    private Game game;

    public  GamePanel(Game game){
        mouseInputs = new MouseInputs(this);
        this.game = game;
        setSizePanel();
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }
    private void setSizePanel() {
        Dimension size = new Dimension(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void updateGame(){
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame(){
        return game;
    }
}
