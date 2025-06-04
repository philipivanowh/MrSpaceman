package Game;

import Game.Constant.GAME_CONSTANT;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;

public class GameMenu extends JPanel{

    public Rectangle playButton = new Rectangle(GAME_CONSTANT.GAME_WIDTH/2 + 120, 150, 100, 50);

    public Rectangle guideButton = new Rectangle(GAME_CONSTANT.GAME_WIDTH/2 + 120, 250, 100, 50);

    public Rectangle quitButton = new Rectangle(GAME_CONSTANT.GAME_WIDTH/2 + 120, 350, 100, 50);

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        Font font = new Font("Arial", Font.BOLD, 30);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Orbitor", GAME_CONSTANT.GAME_WIDTH/2 - 50, 100);

        Font fn1 = new Font("Arial", Font.BOLD, 20);
        g.setFont(fn1);
        g.drawString("Play", playButton.x + 30, playButton.y + 30);
        g2.draw(playButton);
         g.drawString("Guide", guideButton.x + 30, guideButton.y + 30);
        g2.draw(guideButton);
         g.drawString("Quit", quitButton.x + 30, quitButton.y + 30);
        g2.draw(quitButton);


    } 
    
}
