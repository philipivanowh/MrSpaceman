package Game;

import Game.utils.Range;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

/**
 * A smoke-like trail particle that drifts, shrinks, and fades to transparent.
 */

public class TrailParticle extends Entity{
    private int life;
    private final int maxLife;
    private float radius;
    private float alpha;

    private static final float DECAY = 0.9f;
    private static final float EXPAND = 1.04f;

    private static final Range CENTER_THRUST_RADIUS_RANGE = new Range(5, 8);

    private static final Range SIDE_THRUST_RADIUS_RANGE = new Range(4, 7);

    private static Range CURRENT_RANGE;

    private static final int BASE_LIFE = 30;

    private final float CENTER_OFFSET = -23f; // how far back from ship center

    private final float CENTER_OFFSET_SIDE = -2f;
    private final float LATERAL_OFFSET = 14; // half the ship’s width to the side

    private float xOffset;
    private float yOffset;

    /**
     * @param x         start x
     * @param y         start y
     * @param shipAngle ship’s current rotation (radians)
     */
    public TrailParticle(float x, float y, double shipAngle, Constant.ThrustType thrustType) {

        super(x, y,0);

        float cosA = (float) Math.cos(shipAngle);
        float sinA = (float) Math.sin(shipAngle);

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
                + (float) (Math.random() * (CURRENT_RANGE.getMax() - CURRENT_RANGE.getMin()));
        this.alpha = 1f;

        float spread = (float) ((Math.random() - 0.5) * (Math.PI / 6)); // +/-15°
        double velAngle;
        float baseSpeed;

        switch (thrustType) {
            case CENTER:
                // straight backwards
                velAngle = shipAngle + Math.PI + spread;
                baseSpeed = 3.0f + (float) (Math.random() * 1.0); // 3.0–4.0 px/frame
                break;
            case LEFT:
                // squirting off to the left
                velAngle = shipAngle + Math.PI / 2 + spread;
                baseSpeed = 2.0f + (float) (Math.random() * 0.5); // 2.0–2.5 px/frame
                break;
            case RIGHT:
                // squirting off to the right
                velAngle = shipAngle - Math.PI / 2 + spread;
                baseSpeed = 2.0f + (float) (Math.random() * 0.5); // 2.0–2.5 px/frame
                break;
            default:
                throw new AssertionError();
        }

        vel.x = (float) (Math.cos(velAngle) * baseSpeed);
        vel.y = (float) (Math.sin(velAngle) * baseSpeed);
    }

    public void update() {
        pos.x += vel.x;
        pos.y += vel.y;
        vel.x *= DECAY;
        vel.y *= DECAY;
        radius *= EXPAND;
        alpha = (float) life / maxLife;
        life--;
    }

    public boolean isAlive() {
        return life > 0 && alpha > 0f;
    }

    public void render(Graphics2D g2) {
        Composite oldComp = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        Color oldColor = g2.getColor();
        g2.setColor(new Color(200, 200, 200));
        int r = Math.max(1, (int) radius);
        g2.fillOval((int) (pos.x - r), (int) (pos.y - r), r * 2, r * 2);

        g2.setComposite(oldComp);
        g2.setColor(oldColor);
    }
}