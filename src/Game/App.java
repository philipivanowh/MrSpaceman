package Game;

import Game.Constant.GAME_CONSTANT;
import javax.swing.JFrame;

//Executing the game
public class App {
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("Orbitor");

        frame.setSize(GAME_CONSTANT.WINDOW_WIDTH, GAME_CONSTANT.WINDOW_HEIGHT);

        frame.setLocation(0, 0);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Orbitor game = new Orbitor();
        frame.add(game);
        frame.pack();
        frame.setVisible(true);
        frame.setLayout(null);

    }
}
