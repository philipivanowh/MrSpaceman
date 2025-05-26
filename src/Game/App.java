package Game;
import Game.Constant.GAME_CONSTANT;
import javax.swing.JFrame;
//Executing the game

/*
 * App class is the entry point of the Orbitor game.
 * It sets up the main game window, initializes the game, and starts the game loop.
 * The game window is created with a specified title, size, and close operation.
 * The game instance is added to the JFrame, and the window is made visible.
 * The game runs in a JFrame, which is a standard window in Java Swing.
 * The game is initialized with a fixed size and a non-resizable window.
 * The game can be extended to include more features such as settings, menus, and more complex game mechanics.  
 */
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
