package Game;

import Game.utils.Vector2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Input class handles keyboard and mouse inputs for the game.
 * It implements KeyListener and MouseListener interfaces to capture key presses,
 * mouse clicks, and mouse movements.
 * It provides methods to check if specific keys are pressed (e.g., thrusting keys)
 * and to get the mouse position relative to the screen and world coordinates.
 * It also maintains a camera reference to adjust mouse coordinates based on the camera's position.
 * * The class uses static arrays to store the state of keys and mouse buttons, 
 */
public class Input implements KeyListener, MouseListener, MouseMotionListener {

    // key mask that stores if a key is pressed
    public static boolean[] keys = new boolean[1000];

    // Mouse inputs
    public static Vector2D mousePos = new Vector2D();

    // mouse button mask that stores if a button is pressed
    private static boolean[] mouseButtons = new boolean[4];

    // a reference to camera
    private static Camera camera;

    public static void setCamera(Camera camera) {
        Input.camera = camera;
    }

    public static boolean isThrusting() {
        return keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
    }

    public static boolean isLeftThrusting() {
        return keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
    }

    public static boolean isRightThrusting() {
        return keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
    }

    public static Vector2D getMouseRelativeToScreen() {
        return new Vector2D(mousePos.x, mousePos.y);
    }

    public static Vector2D getMouseRelativeToWorld() {
        return new Vector2D(mousePos.x + camera.getX(), mousePos.y + camera.getY());
    }

    public static boolean mouseIsClicked(){
        return mouseButtons[MouseEvent.BUTTON1];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos = new Vector2D(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseButtons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseButtons[e.getButton()] = false;
    }
}
