package Game.ui;

import Game.Constant.GAME_CONSTANT;
import Game.Constant.MENU.BG;
import Game.Constant.MENU.BT;
import Game.GameState;
import Game.utils.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GameMenu {

    private MenuButton playButton;

    private MenuButton instructionButton;


    private MenuButton quitButton;

    private MenuButton settingsButton;

    float astroidAngle = 0;
    float distanceFromSun = 170;

    Vector2D mousePos = new Vector2D();

    /*
     * This is the constructor for the GameMenu class. It initializes the menu buttons with their respective
     * game states and positions.
     * The buttons are positioned based on predefined constants from the BT class.
     */
    public GameMenu() {
        // Initialize buttons with their respective game states
        playButton = new MenuButton(BT.MENU_CENTER_X, BT.playButtonY, BT.MENU_BUTTON_WIDTH, BT.MENU_BUTTON_HEIGHT, "PLAY",GameState.PLAYING);
        instructionButton = new MenuButton(BT.MENU_CENTER_X, BT.instructionButtonY, BT.MENU_BUTTON_WIDTH, BT.MENU_BUTTON_HEIGHT, "INSTRUCTION",GameState.INSTRUCTION);
        quitButton = new MenuButton(BT.MENU_CENTER_X, BT.quitButtonY, BT.MENU_BUTTON_WIDTH, BT.MENU_BUTTON_HEIGHT, "QUIT",GameState.QUIT);
        settingsButton = new MenuButton(BT.settingsButtonX, BT.settingsButtonY, BT.settingsButtonWidth, BT.settingsButtonHeight, "SETTINGS",GameState.SETTINGS);

    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Font font = new Font("Arial", Font.BOLD, 50);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Orbitor", GAME_CONSTANT.WINDOW_WIDTH / 2 - 100, 100);


        //Update the button status 
        playButton.render(g2);
        playButton.handleInput();

        instructionButton.render(g2);
        instructionButton.handleInput();

        quitButton.render(g2);
        quitButton.handleInput();

        settingsButton.render(g2);
        settingsButton.handleInput();
        
        // Draw the background

        drawBacground(g2);

    


    }

    /*
     * This method draws the background of the game menu, including the sun and an asteroid orbiting around it.
     */
    private void drawBacground(Graphics2D g2) {
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

}
