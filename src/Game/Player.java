package Game;

import Game.Constant.GAME_CONSTANT;
import Game.Constant.PLAYER_CONST;
import Game.Constant.ThrustType;
import Game.utils.Vector2D;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;

public class Player extends Entity {

    //Plaer Image
    private final BufferedImage[] frames = new BufferedImage[3];

    private int frameIndex = 0;
    private double frameTimer = 0; // seconds until next frame

    private Vector2D mouseInput = new Vector2D();

    private List<TrailParticle> particles = new ArrayList<>();

    public Player(float x, float y) {
        super(x, y,10);
        loadImages();
    }

    //Update the input for the player
    private void updateInput() {
        mouseInput = Input.getMouseRelativeToWorld();

    }

    //Update the smoke particles
    private void updateParticles() {
        Iterator<TrailParticle> it = particles.iterator();
        while (it.hasNext()) {
            TrailParticle p = it.next();
            p.update();
            if (!p.isAlive()) {
                it.remove();
            }
        }
    }

    public void update(double dt) {

        updateInput();
        updateParticles();
        // rotation
        // if (Input.keys[Input.LEFT]) angle -= TURN_SPEED * dt;
        // if (Input.keys[Input.RIGHT]) angle += TURN_SPEED * dt;

        // Compute target rotation

        Vector2D diff = Vector2D.subtract(mouseInput, pos);
        double targetAngle = diff.getAngle();

        // 1) error between where you are and where you want to point
        double error = clampAngle(targetAngle - angle);

        // 2) only turn if the error is outside the dead-zone
        if (Math.abs(error) > PLAYER_CONST.ANGLE_DEADZONE) {
            // clamp your turn to MAX_TURN_SPEED * dt
            double maxDelta = PLAYER_CONST.MAX_TURN_SPEED * dt;
            double delta = Math.signum(error) * Math.min(Math.abs(error), maxDelta);
            angle = (float) clampAngle(angle + delta);
        }

        // main thrust
        if (Input.isThrusting()) {
            // double radians = Math.toRadians(angle);

            vel.x += Math.cos(angle) * PLAYER_CONST.SHIP_SPEED * dt;
            vel.y += Math.sin(angle) * PLAYER_CONST.SHIP_SPEED * dt;

            // spawn a new trail particle just below the ship
            particles.add(new TrailParticle(pos.x, pos.y, angle, ThrustType.CENTER));
        }
        //left thrust
        if (Input.isLeftThrusting()) {

            vel.x += Math.sin(angle) * PLAYER_CONST.SIDE_SPEED * dt;
            vel.y += -Math.cos(angle) * PLAYER_CONST.SIDE_SPEED * dt;

            particles.add(new TrailParticle(pos.x, pos.y, angle, ThrustType.LEFT));
        }
        //right thrust
        if (Input.isRightThrusting()) {
            vel.x += -Math.sin(angle) * PLAYER_CONST.SIDE_SPEED * dt;
            vel.y += Math.cos(angle) * PLAYER_CONST.SIDE_SPEED * dt;

            particles.add(new TrailParticle(pos.x, pos.y, angle, ThrustType.RIGHT));
        }

        // Damping the velocity
        velocityDecay(dt);

        // integrate position
        pos.x += vel.x * dt;
        pos.y += vel.y * dt;

        // Boarder control
        if (pos.x < 0) {
            pos.x = 0;
            vel.x = 0;
        }
        if (pos.x > GAME_CONSTANT.GAME_WIDTH - 100) {
            pos.x = GAME_CONSTANT.GAME_WIDTH - 100;
            vel.x = 0;
        }
        if (pos.y < 0) {
            pos.y = 0;
            vel.y = 0;
        }
        if (pos.y > GAME_CONSTANT.GAME_HEIGHT - 100) {
            pos.y = GAME_CONSTANT.GAME_HEIGHT - 100;
            vel.y = 0;
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

    //method to decay the velocity of the ship
    private void velocityDecay(double dt) {

        if (vel.length() > .1) {
            float factor = (float) Math.pow(PLAYER_CONST.VEL_DECAY, dt);
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

    // Rendering the player and create a sprite
    @Override
    public void render(Graphics2D g2) {
        AffineTransform old = g2.getTransform();

        // move to the ship’s center (world coords)
        g2.translate(pos.x, pos.y);
        // then rotate around its own center
        g2.rotate(angle + Math.PI / 2);

        BufferedImage sprite = frames[frameIndex];
        g2.drawImage(sprite,
                -sprite.getWidth() / 2,
                -sprite.getHeight() / 2,
                null);

        g2.setTransform(old);

        renderParticle(g2);
    }

    public void renderParticle(Graphics2D g2) {
        for (TrailParticle p : particles) {
            p.render(g2);
        }
    }

    // Load the images from the source
    private void loadImages() {

        try {

            frames[0] = scale(ImageIO.read(getClass().getResource("/Images/Rocket.png")));

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.print("Player Images are not properly loaded");
            System.exit(-1); // fail fast if sprites are missing
        }
    }

    // Converting the png to a Buffer Image
    private static BufferedImage scale(BufferedImage src) {
        BufferedImage dst = new BufferedImage(PLAYER_CONST.SHIP_W, PLAYER_CONST.SHIP_H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dst.createGraphics();
        g2.drawImage(src, 0, 0, PLAYER_CONST.SHIP_W, PLAYER_CONST.SHIP_H, null);
        g2.dispose();
        return dst;
    }
}
