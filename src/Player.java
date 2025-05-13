import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player extends Entity {

    private static final float decays = .9f;

    private static final int   SHIP_W        = 200;               // px after scaling
    private static final int   SHIP_H        = 200;
    private static final double SHIP_SPEED   = 250.0;            // px / s
    private static final double TURN_SPEED   = Math.toRadians(180); // rad / s
    private static final double ANIM_FPS     = 12.0;             // frames / s

    private final BufferedImage[] frames   = new BufferedImage[3];
    private int   frameIndex               = 0;
    private double frameTimer              = 0; // seconds until next frame


    //Rotational dampening PID
      // how fast you can turn (radians per second)
    private static final double MAX_TURN_SPEED = Math.toRadians(360);

    // dead-zone value
    private static final double ANGLE_DEADZONE = Math.toRadians(1); // ~1°
    public Player(float x, float y) {
        super(x, y);
        loadImages();
    }

    public void update(double dt) {

        // rotation 
   //     if (Input.keys[Input.LEFT])  angle -= TURN_SPEED * dt;
      //  if (Input.keys[Input.RIGHT]) angle += TURN_SPEED * dt;

    //Compute target rotation
    double dx = Input.mouseX - x;
    double dy = Input.mouseY - y;
    double targetAngle = Math.atan2(dy,dx);

    // 1) error between where you are and where you want to point
        double error = normalizeAngle(targetAngle - angle);

        // 2) only turn if the error is outside the dead-zone
        if (Math.abs(error) > ANGLE_DEADZONE) {
            // clamp your turn to MAX_TURN_SPEED * dt
            double maxDelta = MAX_TURN_SPEED * dt;
            double delta   = Math.signum(error) * Math.min(Math.abs(error), maxDelta);
            angle = (float)normalizeAngle(angle + delta);
        }




        //thrust
        if (Input.keys[Input.UP]) {
           // double radians = Math.toRadians(angle);

            vx += Math.cos(angle) * SHIP_SPEED * dt;
            vy += Math.sin(angle) * SHIP_SPEED * dt;
        }

        //Damping the velocity
          velocityDecay(dt);

        // integrate position
          x += vx * dt;
    y += vy * dt;
     
      if (x < 0) x += Constant.WINDOW_WIDTH;
    if (x > Constant.WINDOW_WIDTH) x -= Constant.WINDOW_WIDTH;
    if (y < 0) y += Constant.WINDOW_HEIGHT;
    if (y > Constant.WINDOW_HEIGHT) y -= Constant.WINDOW_HEIGHT;

        // engine animation 
        boolean thrusting = Input.keys[Input.UP];
        frameTimer -= dt;
        if (thrusting && frameTimer <= 0) {
            frameIndex = (frameIndex + 1) % 3;
            frameTimer = 1.0 / ANIM_FPS;
        }
        if (!thrusting) {
            frameIndex = 0;          // no frame when you’re not thrusting
        }

      

        
        
    }

 // keep angle within (-π,π]
   private double normalizeAngle(double a) {
        while (a <= -Math.PI) a += 2*Math.PI;
        while (a >   Math.PI) a -= 2*Math.PI;
        return a;
    }

    private  void velocityDecay(double dt){

     if(Math.abs(vx) > .1|| Math.abs(vy) > .1){   
       float D = 0.90f;                        // velocity after 1 second
    float factor = (float)Math.pow(D, dt); // e.g. if dt=0.016s, factor≈0.997
    if(Math.abs(vx) > .1)
    vx *= factor;
    else
    vx = 0;

    if(Math.abs(vy) > .1)
    vy *= factor;
    else
    vy = 0;
     }
     else{
        vx = 0;
        vy = 0;
     }

    }
    
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        AffineTransform old = g2.getTransform();      // save
        g2.translate(x, y);                           // move to ship’s centre
        g2.rotate(angle+Math.PI/2);                             // rotate about (0,0)

        BufferedImage sprite = frames[frameIndex];
        g2.drawImage(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2, null);

        g2.setTransform(old);                         // restore
    }

    // Load the images from the source
    private void loadImages() {
              
        try {
            frames[0] = scale(ImageIO.read(getClass().getResource("/Rocket/Layer 1_rocket1.png")));
            frames[1] = scale(ImageIO.read(getClass().getResource("/Rocket/Layer 1_rocket2.png")));
            frames[2] = scale(ImageIO.read(getClass().getResource("/Rocket/Layer 1_rocket3.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);          // fail fast if sprites are missing
        }
    }

    //Converting the png to a Buffer Image
    private static BufferedImage scale(BufferedImage src) {
        BufferedImage dst = new BufferedImage(SHIP_W, SHIP_H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dst.createGraphics();
        g2.drawImage(src, 0, 0, SHIP_W, SHIP_H, null);
        g2.dispose();
        return dst;
    }
}
