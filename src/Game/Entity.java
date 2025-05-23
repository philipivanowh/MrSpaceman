package Game;

import Game.Constant.PHYSICS_CONSTANT;
import Game.utils.Vector2D;
import java.awt.Graphics2D;

public abstract class Entity {
    protected Vector2D pos = new Vector2D();
    protected Vector2D vel = new Vector2D();
<<<<<<< Updated upstream
=======
    protected Vector2D force = new Vector2D();
    protected double width;
    protected double height;
>>>>>>> Stashed changes

    protected double angle;
    protected double mass;

    public Entity(double x, double y, double width,double height ,double mass) {

        this.pos.x = x;
        this.pos.y = y;
        this.mass = mass;
        this.width = width;
        this.height = height;

    }

    // abstract function for rendering
    public abstract void render(Graphics2D g2);

    // calculates the gravitational force between objects
<<<<<<< Updated upstream
    public Vector2D attraction(Entity body) {
        Vector2D delta = Vector2D.subtract(body.pos, this.pos);
=======
    // F = G*m1*m2 / (r^2)
    // G = 6.674 * 10^-11
    // m1 = mass of the body
    // m2 = mass of the subject
    // r = distance between the two objects

    // Calculate the distance vector between the two objects
    // and its magnitude
    public Vector2D attraction(CelestrialBody body, Vector2D subject) {

        Vector2D delta = Vector2D.subtract(body.pos, subject);
>>>>>>> Stashed changes

        double distSq = delta.x * delta.x + delta.y * delta.y;
        if (distSq == 0) {
            // overlapping bodies? no gravity
            return new Vector2D(0, 0);
        }

        // F = G*m1*m2 / (r^2)
        double magnitude = PHYSICS_CONSTANT.G * body.mass * this.mass / distSq;

        Vector2D newForce = Vector2D.normalize(delta);
        newForce.multiply(magnitude);

        return newForce;
<<<<<<< Updated upstream
=======

    }

    // Getter method to retrieve the mass of the entity
    public double getMass() {
        return mass;
>>>>>>> Stashed changes
    }

    //Not finshed
    public boolean checkCollision(Entity other){
        return false;

    }
}
