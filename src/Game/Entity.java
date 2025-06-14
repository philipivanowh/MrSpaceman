package Game;

import Game.Constant.PHYSICS_CONSTANT;
import Game.utils.Vector2D;
import java.awt.Graphics2D;

/**
 * Entity class represents a generic entity in the game.
 * It serves as a base class for all game entities, such as celestial bodies and players.
 * It contains properties like position, velocity, force, width, height, angle, and mass.
 * It provides methods for rendering, calculating gravitational attraction, and retrieving mass.  
 * * The attraction method calculates the gravitational force between this entity and a celestial body.
 * The gravitational force is calculated using Newton's law of universal gravitation.
 * The mass of the entity can be retrieved using the getMass method.
 * This class is abstract.
 */
public abstract class Entity {
    // position, velocity, acceleration, force, width, height, angle, and mass of the entity
    protected Vector2D pos = new Vector2D();
    protected Vector2D vel = new Vector2D();
    protected Vector2D acc = new Vector2D();
    protected Vector2D force = new Vector2D();

    protected double angle;
    protected double mass;

    /**
     * Constructor for the Entity class.
     * Initializes an entity with a position, width, height, and mass.
     * * @param x The x-coordinate of the entity's position.
     * @param y The y-coordinate of the entity's position.
     * @param mass The mass of the entity.
     * This constructor sets the position, width, height, and mass of the entity.
     */
    public Entity(double x, double y, double mass) {

        this.pos.x = x;
        this.pos.y = y;
        this.mass = mass;
    }

    // abstract function for rendering
    public abstract void render(Graphics2D g2);
    // abstract getter function 
    //Get pos
    public abstract Vector2D getPos();
    //Get vel
    public abstract Vector2D getVel();
    //Get acc
    public abstract Vector2D getAcc();
    //Get force
    public abstract Vector2D getForce();
    

    // calculates the gravitational force between objects
    // F = G*m1*m2 / (r^2)
    // G = 6.674 * 10^-11
    // m1 = mass of the body
    // m2 = mass of the subject
    // r = distance between the two objects

    // Calculate the distance vector between the two objects
    // and its magnitude
    public Vector2D attraction(CelestialBody body, Vector2D subject) {

        Vector2D delta = Vector2D.subtract(body.pos, subject);

        double distSq = delta.x * delta.x + delta.y * delta.y;
        if (distSq == 0) {
            // overlapping bodies? no gravity
            return new Vector2D(0, 0);
        }

        // F = G*m1*m2 / (r^2)
        double magnitude = PHYSICS_CONSTANT.G * body.getMass() * this.mass / distSq;

        Vector2D newForce = Vector2D.normalize(delta);
        newForce.multiply(magnitude);

        return newForce;

    }

    // Getter method to retrieve the mass of the entity
    public double getMass() {
        return this.mass;
    }
}
