package Game;

import Game.Constant.GAME_CONSTANT;
import Game.utils.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GameMenu {

    public Rectangle playButton = new Rectangle(GAME_CONSTANT.WINDOW_WIDTH / 2 - 100, 150, 200, 50);

    public Rectangle guideButton = new Rectangle(GAME_CONSTANT.WINDOW_WIDTH / 2 - 100, 250, 200, 50);

    public Rectangle quitButton = new Rectangle(GAME_CONSTANT.WINDOW_WIDTH / 2 - 100, 350, 200, 50);

    Vector2D mousePos = new Vector2D();

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Font font = new Font("Arial", Font.BOLD, 50);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Orbitor", GAME_CONSTANT.WINDOW_WIDTH / 2-100, 100);

        Font fn1 = new Font("Arial", Font.BOLD, 20);
        g.setFont(fn1);
        g.drawString("Play", playButton.x + 75, playButton.y + 30);
        g2.draw(playButton);
        g.drawString("Guide", guideButton.x + 75, guideButton.y + 30);
        g2.draw(guideButton);
        g.drawString("Quit", quitButton.x + 75, quitButton.y + 30);
        g2.draw(quitButton);

        mousePos = Input.getMouseRelativeToScreen();

        if (mousePos.x >= GAME_CONSTANT.WINDOW_WIDTH / 2 - 100 && mousePos.x <= GAME_CONSTANT.WINDOW_WIDTH / 2 + 100
                && Input.mouseIsClicked()) {

            // Play Button
            if (mousePos.y >= 150 && mousePos.y <= 200) {
                GameState.state = GameState.PLAYING;
            }

            // Guide
            if (mousePos.y >= 250 && mousePos.y <= 300) {
                GameState.state = GameState.SETTINGS;
            }
            // Quit
            if (mousePos.y >= 350 && mousePos.y <= 400) {
                GameState.state = GameState.QUIT;
            }
        }

    }

}
