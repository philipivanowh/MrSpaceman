import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player extends Entity {

    private static final float decays = .9f;

    private static final int SHIP_W = 100; // px after scaling
    private static final int SHIP_H = 100;
    private static final double SHIP_SPEED = 250.0; // px / s
    private static final double TURN_SPEED = Math.toRadians(180); // rad / s
    private static final double ANIM_FPS = 12.0; // frames / s

    private final BufferedImage[] frames = new BufferedImage[3];
    private int frameIndex = 0;
    private double frameTimer = 0; // seconds until next frame

    private Vector2D mouseInput = new Vector2D();

    // Rotational dampening PID
    // how fast you can turn (radians per second)
    private static final double MAX_TURN_SPEED = Math.toRadians(360);

    // dead-zone value
    private static final double ANGLE_DEADZONE = Math.toRadians(1); // ~1°

    public Player(float x, float y) {
        super(x, y);
        loadImages();
    }

    private void updateInput() {
        mouseInput = Input.getMouseRelativeToWorld();
       

    }

    public void update(double dt) {

         System.out.println(pos);
        updateInput();
        // rotation
        // if (Input.keys[Input.LEFT]) angle -= TURN_SPEED * dt;
        // if (Input.keys[Input.RIGHT]) angle += TURN_SPEED * dt;

        // Compute target rotation

        Vector2D diff = Vector2D.subtract(mouseInput, pos);
        double targetAngle = diff.getAngle();

        // 1) error between where you are and where you want to point
        double error = clampAngle(targetAngle - angle);

        // 2) only turn if the error is outside the dead-zone
        if (Math.abs(error) > ANGLE_DEADZONE) {
            // clamp your turn to MAX_TURN_SPEED * dt
            double maxDelta = MAX_TURN_SPEED * dt;
            double delta = Math.signum(error) * Math.min(Math.abs(error), maxDelta);
            angle = (float) clampAngle(angle + delta);
        }

        // thrust
        if (Input.isThrusting()) {
            // double radians = Math.toRadians(angle);

            vel.x += Math.cos(angle) * SHIP_SPEED * dt;
            vel.y += Math.sin(angle) * SHIP_SPEED * dt;
        }

        // Damping the velocity
        velocityDecay(dt);

        // integrate position
        pos.x += vel.x * dt;
        pos.y += vel.y * dt;

        if (pos.x < 0)
            pos.x = 0;
        if (pos.x > Constant.GAME_WIDTH-100)
            pos.x = Constant.GAME_WIDTH-100;
        if (pos.y < 0)
            pos.y = 0;
        if (pos.y > Constant.GAME_HEIGHT-100)
            pos.y = Constant.GAME_HEIGHT-100;

        // engine animation
        boolean thrusting = Input.isThrusting();
        frameTimer -= dt;
        if (thrusting && frameTimer <= 0) {
            frameIndex = (frameIndex + 1) % 3;
            frameTimer = 1.0 / ANIM_FPS;
        }
        if (!thrusting) {
            frameIndex = 0; // no frame when you’re not thrusting
        }

    }

    // keep angle within (-π,π]
    private double clampAngle(double a) {
        while (a <= -Math.PI)
            a += 2 * Math.PI;
        while (a > Math.PI)
            a -= 2 * Math.PI;
        return a;
    }

    private void velocityDecay(double dt) {

        if (vel.length()>.1) {
            float D = 0.90f; // velocity after 1 second
            float factor = (float) Math.pow(D, dt); 
            if (Math.abs(vel.x) > .1)
                vel.x *= factor;
            else
                vel.x = 0;

            if (Math.abs(vel.y) > .1)
                vel.y *= factor;
            else
                vel.y = 0;
        } else {
           vel = Vector2D.ZERO;
        }

    }

    public void render(Graphics2D g2) {
          AffineTransform old = g2.getTransform();

    // move to the ship’s center (world coords)
    g2.translate(pos.x, pos.y);
    // then rotate around its own center
    g2.rotate(angle + Math.PI/2);

    BufferedImage sprite = frames[frameIndex];
    g2.drawImage(sprite,
        -sprite.getWidth()/2,
        -sprite.getHeight()/2,
        null);

    g2.setTransform(old);
    }

    // Load the images from the source
    private void loadImages() {

        try {

            frames[0] = scale(ImageIO.read(getClass().getResource("/Rocket/Layer 1_rocket1.png")));
            frames[1] = scale(ImageIO.read(getClass().getResource("/Rocket/Layer 1_rocket2.png")));
            frames[2] = scale(ImageIO.read(getClass().getResource("/Rocket/Layer 1_rocket3.png")));

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1); // fail fast if sprites are missing
        }
    }

    // Converting the png to a Buffer Image
    private static BufferedImage scale(BufferedImage src) {
        BufferedImage dst = new BufferedImage(SHIP_W, SHIP_H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dst.createGraphics();
        g2.drawImage(src, 0, 0, SHIP_W, SHIP_H, null);
        g2.dispose();
        return dst;
    }
}
