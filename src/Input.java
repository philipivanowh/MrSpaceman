
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener{

    //Keyboard inputs
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;
    public static final int SPACE = 4;

    public static boolean[] keys = new boolean[5];


    //Mouse inputs
    public static int mouseX, mouseY;
    public static boolean mouseDown;




   @Override
public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
    
         //Going Left
        case KeyEvent.VK_LEFT:
            keys[LEFT] = true;
            break;
        case  KeyEvent.VK_A:
            keys[LEFT] = true;
            break;

        //Going Right
        case KeyEvent.VK_RIGHT:
            keys[RIGHT] = true;
            break;
        case KeyEvent.VK_D:
            keys[RIGHT] = true;
            break;
        //Going Up
        case KeyEvent.VK_UP:
            keys[UP] = true;
            break;
        case KeyEvent.VK_W:
            keys[UP] = true;
            break;

        //Going Down
        case KeyEvent.VK_DOWN:
            keys[DOWN] = true;
            break;
        case KeyEvent.VK_S:
            keys[DOWN] = true;
            break;
     
    
        case KeyEvent.VK_SPACE:
            keys[SPACE] = true;
            break;
    

    }
}

// You can leave keyTyped empty or remove the exception
@Override
public void keyTyped(KeyEvent e) { }

@Override
public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {

        //Going Left
        case KeyEvent.VK_LEFT:
            keys[LEFT] = false;
            break;
        case  KeyEvent.VK_A:
            keys[LEFT] = false;
            break;

        //Going Right
        case KeyEvent.VK_RIGHT:
            keys[RIGHT] = false;
            break;
        case KeyEvent.VK_D:
            keys[RIGHT] = false;
            break;
        //Going Up
        case KeyEvent.VK_UP:
            keys[UP] = false;
            break;
        case KeyEvent.VK_W:
            keys[UP] = false;
            break;

        //Going Down
        case KeyEvent.VK_DOWN:
            keys[DOWN] = false;
            break;
        case KeyEvent.VK_S:
            keys[DOWN] = false;
            break;
     
    
        case KeyEvent.VK_SPACE:
            keys[SPACE] = false;
            break;
    
        
    }
}

@Override
public void mouseDragged(MouseEvent e) {
}

@Override
public void mouseMoved(MouseEvent e) {
    mouseX = e.getX();
   
    mouseY = e.getY();

     System.out.println(mouseX+" "+ mouseY);
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
