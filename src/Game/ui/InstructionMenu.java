package Game.ui;

import Game.Constant.GAME_CONSTANT;
import Game.GameState;
import Game.Input;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class InstructionMenu {

    private Rectangle content = new Rectangle(GAME_CONSTANT.WINDOW_WIDTH / 2 - 300,
            GAME_CONSTANT.WINDOW_HEIGHT / 2 - 400, 700, 1080);

    private final int spacing = 45;
    private int contentLine = 0;

    public void update(){
        if(Input.isESCPressed())
            GameState.state = GameState.MENU;
    }

    public void render(Graphics g) {

        Font font = new Font("Arial", Font.BOLD, 50);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Instruction", GAME_CONSTANT.WINDOW_WIDTH / 2 - 100, 70);

        // Resetting the content space

        contentLine = content.y;

        Font fn1 = new Font("Arial", Font.BOLD, 20);
        g.setFont(fn1);
        g.drawString("                  Welcome to Orbitor!", content.x + 75, contentLine);

        contentLine += spacing;
        g.drawString("Use the mouse to control the rotation of the spaceship.", content.x + 75, contentLine);

        contentLine += spacing;
        g.drawString("Use your thrust engine to move your spaceship.", content.x + 75, contentLine);

        contentLine += spacing;
        g.drawString("W - Run the Center Thrust Engine.", content.x + 75, contentLine);

        contentLine += spacing;
        g.drawString("A - Run the Left Thrust Engine.", content.x + 75, contentLine);

        contentLine += spacing;
        g.drawString("D - Run the Right Thrust Engine.", content.x + 75, contentLine);

        contentLine += spacing;
        g.drawString("You can use your mouse wheel to zoom in-out of the game", content.x + 75,contentLine);

        
        contentLine += spacing;
        g.drawString("R - to reset your zoom scale", content.x + 75,contentLine);


        contentLine += spacing + 10;
        g.drawString("WINNING CONDITION:", content.x + 75, contentLine);

        contentLine += spacing + 10;
        g.drawString("     Deliver 10 packages to the specific planet.", content.x + 75, contentLine);

        contentLine += spacing + 30;
        g.drawString("LOSING Condition:", content.x + 75, contentLine);

        contentLine += spacing;
        g.drawString("      Getting crush by enemy ship.", content.x + 75, contentLine);

        contentLine += spacing;
        g.drawString("      Crushing into planets.", content.x + 75, contentLine);

        contentLine += spacing;
        g.drawString("Press 'ESC' to return to the main menu.", content.x + 75, contentLine);

    }
}
