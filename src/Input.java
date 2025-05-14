
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

    // Store Keyboard keys state pressed = true, false otherwise
    public static boolean[] keys = new boolean[1000];
    public static int UP = 87;
    public static int W = 38;

    // Mouse inputs
    public static Vector2D mouseInput = new Vector2D();
    
    private static boolean mouseDown;

     // a reference to camera
    private static Camera camera;

       /** call this once after you create your camera: */
    public static void setCamera(Camera cam) {
        camera = cam;
    }


    public static boolean isThrusting() {
        return keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
    }

    public static boolean isMouseClicked(){
        return mouseDown;
    }

    public static Vector2D getMouseInput(){
         if (camera != null) {
            return new Vector2D(
                mouseInput.x + camera.getX(),
                 mouseInput.y + camera.getY()
            );
        } else {
            // fallback if camera isn’t set yet
            return new Vector2D( mouseInput.x,  mouseInput.y);
        }
    }



    @Override
    public void keyPressed(KeyEvent e) {
       keys[e.getKeyCode()] = true;
    }

    // You can leave keyTyped empty or remove the exception
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseInput = new Vector2D(e.getX(),e.getY());

   

        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked!");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
