package Game.ui;

import Game.Constant.GAME_CONSTANT;
import Game.GameState;
import Game.Input;
import Game.MusicManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class SettingsMenu {

    // Container for all the elements
    private Rectangle content = new Rectangle(GAME_CONSTANT.WINDOW_WIDTH / 2 - 300,
            GAME_CONSTANT.WINDOW_HEIGHT / 2 - 400, 700, 1080);

    public JSlider audioSlider;

    public JLabel volumeLabel;
    // public JSlider slider;

    public SettingsMenu() {

        createControls();

    }

    // Handle slider setup and value update
    private void createControls() {

           int x = GAME_CONSTANT.WINDOW_WIDTH/2 - 150;
        int y = 210;
        int width = 300;
        int height = 15;

        // --- AUDIO SLIDER IN ORBITOR --		
        audioSlider = new JSlider(0, 100, 50);
        audioSlider.setBounds(x, y, width, height);
        audioSlider.setPaintTicks(false);
        audioSlider.setPaintLabels(false);

        volumeLabel = new JLabel(String.valueOf(audioSlider.getValue()));
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setBounds(x + width + 10, y, 50, height);

        audioSlider.addChangeListener(e -> {
            int val = audioSlider.getValue();
            volumeLabel.setText(Integer.toString(val));
            MusicManager.setVolume(val / 100f);
        });

        audioSlider.setFocusable(false);
        audioSlider.setRequestFocusEnabled(false);

    }

    /*
     * Update function handles the input while in the menu
     * Esc- going back to main menu
     */
    public void update() {
        if (Input.isESCPressed())
            GameState.state = GameState.MENU;
    }

    public void render(Graphics g) {

        Font font = new Font("Arial", Font.BOLD, 50);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Settings", GAME_CONSTANT.WINDOW_WIDTH / 2 - 100, 70);

        
        Font fn1 = new Font("Arial", Font.BOLD, 25);
        g.setFont(fn1);
        g.drawString("Music Audio Level", GAME_CONSTANT.WINDOW_WIDTH / 2 - 400, 225);

        
        g.drawString("Press 'ESC' to return to the main menu.", content.x + 75, 700);

    }

}
