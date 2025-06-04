package Game.ui;

import Game.Constant.GAME_CONSTANT;
import Game.Constant.MENU.BG;
import Game.Constant.MENU.BT;
import Game.GameState;
import Game.Input;
import Game.utils.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GameMenu {

    public Rectangle playButton = new Rectangle(BT.MENU_CENTER_X, BT.playButtonY, BT.MENU_BUTTON_WIDTH,
            BT.MENU_BUTTON_HEIGHT);

    public Rectangle instructionButton = new Rectangle(BT.MENU_CENTER_X, BT.instructionButtonY, BT.MENU_BUTTON_WIDTH,
            BT.MENU_BUTTON_HEIGHT);

    public Rectangle quitButton = new Rectangle(BT.MENU_CENTER_X, BT.quitButtonY, BT.MENU_BUTTON_WIDTH,
            BT.MENU_BUTTON_HEIGHT);

    public Rectangle settingsButton = new Rectangle(BT.settingsButtonX, BT.settingsButtonY, BT.settingsButtonWidth,
            BT.MENU_BUTTON_HEIGHT);

    float astroidAngle = 0;
    float distanceFromSun = 170;

    Vector2D mousePos = new Vector2D();

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Font font = new Font("Arial", Font.BOLD, 50);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Orbitor", GAME_CONSTANT.WINDOW_WIDTH / 2 - 100, 100);

        Font fn1 = new Font("Arial", Font.BOLD, 20);
        g.setFont(fn1);
        g.drawString("Play", playButton.x + 75, playButton.y + 30);
        g2.draw(playButton);
        g.drawString("Instruction", instructionButton.x + 75, instructionButton.y + 30);
        g2.draw(instructionButton);
        g.drawString("Setting", settingsButton.x + 75, settingsButton.y + 30);
        g2.draw(settingsButton);
        g.drawString("Quit", quitButton.x + 75, quitButton.y + 30);
        g2.draw(quitButton);

        // Update mouse position
        mousePos = Input.getMouseRelativeToScreen();

        handleButtons();

        // Draw the background

        // Draw the sun

        Vector2D delta = Vector2D.subtract(mousePos, new Vector2D(BG.SUN_X, BG.SUN_Y));
        if (delta.length() <= BG.SUN_RADIUS + 2) {
            g2.setColor(BG.HIGHLIGHT_SUN_COLOR);
            g2.fillOval(BG.SUN_X - 12, BG.SUN_Y - 12, BG.SUN_RADIUS + 25, BG.SUN_RADIUS + 25);

        } else {
            g2.setColor(BG.SUN_COLOR);
            g2.fillOval(BG.SUN_X, BG.SUN_Y, BG.SUN_RADIUS, BG.SUN_RADIUS);
        }

        // Draw the astroid
        int astroidX = (int) (Math.cos(astroidAngle) * distanceFromSun + BG.SUN_X + BG.SUN_RADIUS / 2);
        int astroidY = (int) (Math.sin(astroidAngle) * distanceFromSun + BG.SUN_Y + BG.SUN_RADIUS / 2);

        g2.setColor(BG.ASTROID_COLOR);
        g2.fillOval(astroidX, astroidY, BG.ASTROID_RADIUS, BG.ASTROID_RADIUS);

        astroidAngle += 0.01f; // Adjust the speed of rotation as needed

        if (astroidAngle >= 2 * Math.PI) {
            astroidAngle = 0; // Reset angle to keep it within bounds
        }

    }

    public void handleButtons() {

        if (mousePos.x >= GAME_CONSTANT.WINDOW_WIDTH / 2 - 100 && mousePos.x <= GAME_CONSTANT.WINDOW_WIDTH / 2 + 100
                && Input.mouseIsClicked()) {

            // Play Button
            if (mousePos.y >= BT.playButtonY && mousePos.y <= BT.playButtonY + BT.MENU_BUTTON_HEIGHT) {
                GameState.state = GameState.PLAYING;
            }
            // Instructions
            if (mousePos.y >= BT.instructionButtonY && mousePos.y <= BT.instructionButtonY + BT.MENU_BUTTON_HEIGHT) {
                GameState.state = GameState.INSTRUCTION;
            }
            // Settings
            if (mousePos.y >= BT.settingsButtonY && mousePos.y <= BT.settingsButtonY + BT.settingsButtonHeight) {
                GameState.state = GameState.SETTINGS;
            }

        }
    }

}
