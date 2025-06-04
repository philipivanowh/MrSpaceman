package Game;

import Game.utils.Range;
import Game.utils.Vector2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

/**
 * TrailParticle class represents a particle that trails behind a ship when it is thrusting.
 * It simulates the effect of exhaust or particles being expelled from the ship.
 * Each particle has a lifespan, radius, and alpha value that determines its visibility.
 * The particle's size increases over time, and its alpha value decreases as it ages.
 * The particle's position is determined by the ship's angle and thrust type.
 * The particle is rendered as a circle with a color and alpha transparency.
 * The particle decays in velocity and expands in size over time.
 * The particle can be created with different thrust types: center, left, or right.
 * The particle's lifespan is randomly determined within a specified range.
 * The particle's radius is also randomly determined within a specified range based on the thrust type.
 * The particle's position is offset from the ship's position based on the thrust type and ship angle.
 * The particle's velocity is calculated based on the ship's angle and thrust type.
 * The particle's alpha value is calculated based on its remaining life.
 * The particle is updated each frame, moving its position, decaying its velocity, expanding its size, and reducing its alpha value.
 * The particle is rendered with a semi-transparent color, allowing it to blend with the background.
 * The particle is considered alive as long as its life is greater than zero and its alpha value is greater than zero.
 * The particle can be updated and rendered independently of the ship.
 * The particle's behavior can be customized by changing the decay, expansion, and lifespan values.
 * The particle's radius can also be adjusted based on the thrust type, creating different visual effects.
 */

public class TrailParticle extends Entity {
    private int life;
    private final int maxLife;
    private double radius;
    private double alpha;

    private static final double DECAY = 0.9f;
    private static final double EXPAND = 1.04f;

    private static final Range CENTER_THRUST_RADIUS_RANGE = new Range(5, 8);

    private static final Range SIDE_THRUST_RADIUS_RANGE = new Range(4, 7);

    private static Range CURRENT_RANGE;

    private static final int BASE_LIFE = 30;

    private final double CENTER_OFFSET = -23f; // how far back from ship center

    private final double CENTER_OFFSET_SIDE = -2f;
    private final double LATERAL_OFFSET = 14; // half the ship’s width to the side

    private double xOffset;
    private double yOffset;

    /**
     * @param x         start x
     * @param y         start y
     * @param shipAngle ship’s current rotation (radians)
     */
    public TrailParticle(double x, double y, double shipAngle, Constant.ThrustType thrustType) {

        super(x, y,0);

        double cosA = (double) Math.cos(shipAngle);
        double sinA = (double) Math.sin(shipAngle);

        // offset the particle position based on the ship angle and thrust type
        switch (thrustType) {
            case CENTER:
                xOffset = cosA * CENTER_OFFSET;
                yOffset = sinA * CENTER_OFFSET;
                CURRENT_RANGE = CENTER_THRUST_RADIUS_RANGE;
                break;
            case LEFT:
                xOffset = cosA * CENTER_OFFSET_SIDE + (-sinA) * LATERAL_OFFSET;
                yOffset = sinA * CENTER_OFFSET_SIDE + (cosA) * LATERAL_OFFSET;
                CURRENT_RANGE = SIDE_THRUST_RADIUS_RANGE;
                break;
            case RIGHT:
                xOffset = cosA * CENTER_OFFSET_SIDE + (sinA) * LATERAL_OFFSET;
                yOffset = sinA * CENTER_OFFSET_SIDE + (-cosA) * LATERAL_OFFSET;
                CURRENT_RANGE = SIDE_THRUST_RADIUS_RANGE;
                break;
            default:
                throw new AssertionError();
        }

        pos.x = pos.x + xOffset;
        pos.y = pos.y + yOffset;

        this.maxLife = BASE_LIFE + (int) (Math.random() * 15);
        this.life = maxLife;
        this.radius = CURRENT_RANGE.getMax()
                + (double) (Math.random() * (CURRENT_RANGE.getMax() - CURRENT_RANGE.getMin()));
        this.alpha = 1f;

        double spread = (double) ((Math.random() - 0.5) * (Math.PI / 6)); // +/-15°
        double velAngle;
        double baseSpeed;

        // calculate the velocity angle and speed based on the thrust type
        switch (thrustType) {
            case CENTER:
                // straight backwards
                velAngle = shipAngle + Math.PI + spread;
                baseSpeed = 3.0f + (double) (Math.random() * 1.0); // 3.0–4.0 px/frame
                break;
            case LEFT:
                // squirting off to the left
                velAngle = shipAngle + Math.PI / 2 + spread;
                baseSpeed = 2.0f + (double) (Math.random() * 0.5); // 2.0–2.5 px/frame
                break;
            case RIGHT:
                // squirting off to the right
                velAngle = shipAngle - Math.PI / 2 + spread;
                baseSpeed = 2.0f + (double) (Math.random() * 0.5); // 2.0–2.5 px/frame
                break;
            default:
                throw new AssertionError();
        }

        // calculate the velocity based on the angle and speed
        vel.x = (double) (Math.cos(velAngle) * baseSpeed);
        vel.y = (double) (Math.sin(velAngle) * baseSpeed);
    }

    /**
     * Updates the particle's position, velocity, radius, alpha value, and life.
     * The particle moves based on its velocity, decays its velocity over time,
     * expands its radius, and reduces its alpha value based on its remaining life.
     */
    public void update() {
        pos.x += vel.x;
        pos.y += vel.y;
        vel.x *= DECAY;
        vel.y *= DECAY;
        radius *= EXPAND;
        alpha = (double) life / maxLife;
        life--;
    }

    /**
     * Checks if the particle is still alive based on its life and alpha value.
     * A particle is considered alive if its life is greater than 0 and its alpha value is greater than 0.
     *
     * @return true if the particle is alive, false otherwise
     */
    public boolean isAlive() {
        return life > 0 && alpha > 0f;
    }

    /**
     * Renders the particle on the screen.
     * The particle is drawn as a filled oval with a semi-transparent color based on its alpha value.
     * The size of the oval is determined by the particle's radius.
     *
     * @param g2 the Graphics2D object used for rendering
     */
    public void render(Graphics2D g2) {
        Composite oldComp = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));

        Color oldColor = g2.getColor();
        g2.setColor(new Color(200, 200, 200));
        int r = Math.max(1, (int) radius);
        g2.fillOval((int) (pos.x - r), (int) (pos.y - r), r * 2, r * 2);

        g2.setComposite(oldComp);
        g2.setColor(oldColor);
    }

      @Override
    public Vector2D getPos() {
        return this.pos;
    }

    @Override
    public Vector2D getVel() {
        return this.vel;
    }

    @Override
    public Vector2D getAcc() {
        return this.acc;
       
    }

    @Override
    public Vector2D getForce() {
        return this.force;
    }
}