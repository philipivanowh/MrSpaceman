import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class Orbitor extends JPanel implements ActionListener, Runnable{
    private boolean runGame = true;
    private Input input;
    private Player player;

    Thread gameThread;

    //Player ship


    



    Orbitor(){

       
        setPreferredSize(new Dimension(Constant.WINDOW_WIDTH,Constant.WINDOW_HEIGHT));
        this.setBackground(Constant.SPACE_COLOR);

       
        // make sure we can get key events
        setFocusable(true);
        requestFocusInWindow();

        input = new Input();
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
        //Input focus
        requestFocus();

        initClasses();
      

        StartGame();
       
    }

    private void initClasses(){
        player = new Player(700,500);
    }

    private void StartGame(){

       gameThread = new Thread(this);
       gameThread.start();
       
    }

    @Override
    public void run(){

        final double nsPerSecond = 1_000_000_000.0;
    long lastTime = System.nanoTime();
    double accumulator = 0;
    double nsPerFrame = nsPerSecond / Constant.FPS_SET;

    while (runGame) {
        long now = System.nanoTime();
        double delta = now - lastTime;
        lastTime = now;
        accumulator += delta;

        // only update & repaint when enough time has passed
        while (accumulator >= nsPerFrame) {
            double seconds = nsPerFrame / nsPerSecond;
            update(seconds);
            repaint();
            accumulator -= nsPerFrame;
        }
    }
}

 
   public void update(double deltaSeconds) {
    player.update(deltaSeconds);
}

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        render(g);
    }

    public void render(Graphics g){
      player.render(g);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        repaint();
    }


}