package main;

import inputs.KeyBoardInputs;
import inputs.MouseInputs;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;



public class GamePanel extends JPanel {
    
    private MouseInputs mouseInputs;
    private Game game;
    private BufferedImage backgroundImg;

    public  GamePanel(Game game){
        mouseInputs = new MouseInputs(this);
        this.game = game;
        setSizePanel();
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        try {
            backgroundImg = ImageIO.read(getClass().getResourceAsStream("/res/Tilesets/BGimage1.png"));
        } catch (IOException e) {
            e.printStackTrace();
}
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
        g.drawImage(backgroundImg, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, null);
        game.render(g);
    }

    public Game getGame(){
        return game;
    }
}
