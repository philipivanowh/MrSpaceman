package Game;

import Game.Constant.PHYSICS_CONSTANT;
import Game.utils.Vector2D;
import java.awt.Graphics2D;

public abstract class Entity {
    protected Vector2D pos = new Vector2D();
    protected Vector2D vel = new Vector2D();

    protected double angle;
    protected double mass;

    public Entity(double x, double y, double mass) {
        this.pos.x = x;
        this.pos.y = y;
        this.mass = mass;
    }

    // abstract function for rendering
    public abstract void render(Graphics2D g2);

    // calculates the gravitational force between objects
    public Vector2D attraction(Entity body) {
        Vector2D delta = Vector2D.subtract(body.pos, this.pos);

        double distSq = delta.x * delta.x + delta.y * delta.y;
        if (distSq == 0) {
            // overlapping bodies? no gravity
            return new Vector2D(0, 0);
        }

        // F = G*m1*m2 / (r^2)
        double magnitude = PHYSICS_CONSTANT.G * body.mass * this.mass / distSq;

        double theta = delta.getAngle();
        Vector2D newForce = new Vector2D();
        newForce.x = (Math.cos(theta) * magnitude);
        newForce.y = (Math.sin(theta) * magnitude);

        return newForce;
    }
}
