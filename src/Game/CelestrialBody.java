package Game;

import Game.Constant.CELESTRIAL_BODY_TYPE;
import Game.Constant.PHYSICS_CONSTANT;
import Game.utils.Vector2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

/*
 * CelestrialBody class represents a celestrial body in the solar system
 * It can be a planet,sun,black hole, or a moon depending on its CelestrialBodyType
 * It has a position, velocity, radius, mass, and color
 * It can update its position based on gravitational forces from other celestrial bodies
 * It can render itself on the screen as a circle
 * It can calculate its optimal orbital velocity around a central body
 * It can draw its orbit based on its travelled path
 */
public class CelestrialBody extends Entity {
    // radius of the celestrial body in pixels
    public double radius = 0;

    // color of the celestrial body
    private Color color;

    private int glowSize;

    // mass of the celestrial body in kg
    private double mass;

    // Is the celetrial body a sun
    public CELESTRIAL_BODY_TYPE bodyType;

    // The parent celestrial body, e.g., the sun for planets
    public CelestrialBody parent;

    // track the path of the celestrial body
    // This is used to draw the orbit of the celestrial body
    public ArrayList<Vector2D> orbits = new ArrayList<>();

    /*
     * Constructor for the CelestrialBody class.
     * Initializes a celestrial body with a position, radius, mass, type, color,
     * parent body, and solar system.
     *
     * @param x The x-coordinate of the celestrial body's position in AU scale.
     * 
     * @param y The y-coordinate of the celestrial body's position in AU scale.
     * 
     * @param radius The radius of the celestrial body in pixels.
     * 
     * @param mass The mass of the celestrial body in kg.
     * 
     * @param bodyType The type of the celestrial body (e.g., planet, sun).
     * 
     * @param color The color of the celestrial body.
     * 
     * @param parent The parent celestrial body (e.g., the sun for planets).
     * 
     * @param system The solar system to which this celestrial body belongs.
     */
    public CelestrialBody(double x, double y, double radius, double mass, CELESTRIAL_BODY_TYPE bodyType, Color color,
            CelestrialBody parent, SolarSystem system) {
        super(x, y, radius, radius, mass);
        this.radius = radius;
        this.bodyType = bodyType;
        glowSize = (int) (radius * 0.2);
        this.color = color;
        this.mass = mass;
        if (bodyType == CELESTRIAL_BODY_TYPE.PLANET) {
            vel.y = optimalOrbitalVelocity(parent).length();
        }

        this.parent = parent;

    }

    /*
     * Update the position and velocity of the celestrial body based on
     * gravitational forces from other celestrial bodies.
     * updates its velocity based on that force, and then updates its position
     * accordingly.
     * It also updates the orbit path of the celestrial body.
     */
    public void update(ArrayList<CelestrialBody> other) {

        updateNetGravitationalForce(other);
        // Vel is in AU scale
        vel.x += force.x / mass * PHYSICS_CONSTANT.TIMESTEP;
        vel.y += force.y / mass * PHYSICS_CONSTANT.TIMESTEP;

        pos.x += vel.x * PHYSICS_CONSTANT.TIMESTEP;
        pos.y += vel.y * PHYSICS_CONSTANT.TIMESTEP;

        // orbit cord update
        double px = (int) ((pos.x) * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE);
        double py = (int) ((pos.y) * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE);
        orbits.add(new Vector2D(px, py));

    }

    /*
     * Calculate the gravitational attraction force (N) between this body and
     * another body.
     * Uses Newton's law of universal gravitation: F = G * (m1 * m2) / r^2
     *
     * @param other The other celestrial body to which this body is attracted.
     * 
     * @param pos The position of this celestrial body.
     * 
     * @return A Vector2D representing the gravitational force vector.
     */
    public void updateNetGravitationalForce(ArrayList<CelestrialBody> planets) {
        // clear last frame’s total
        force.x = 0;
        force.y = 0;
        for (CelestrialBody planet : planets) {
            if (planet == this)
                continue;
            Vector2D f = attraction(planet, pos);
            force.x += f.x;
            force.y += f.y;
        }
    }

    /*
     * Render the celestrial body on the screen.
     * It draws the orbit if there are more than 3 points in the orbit path.
     * It draws the celestrial body as a filled circle at its position.
     * it sets the color for the celestrial body
     * the x and y coordinates are used as the center of the circle instead of the top-left corner
     * @param g2 The Graphics2D object used for rendering.
     */
    @Override
    public void render(Graphics2D g2) {

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (orbits.size() > 3) {
            // drawOrbit
            drawOrbit(g2, orbits, 3f);
        }

        //set the color for of the planet
        g2.setColor(color);

        int centerX = (int) (pos.x * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE - radius / 2);
        int centerY = (int) (pos.y * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE - radius / 2);

        g2.fillOval(centerX, centerY, (int) radius, (int) radius);

    }

    /*
     * Draws the orbit of the celestrial body as a polyline.
     * It takes a list of Vector2D points representing the orbit in game coordinates
     * and draws them as a polyline with the specified thickness.
     */
    private void drawOrbit(Graphics2D g2, List<Vector2D> orbitGameCoords, float thickness) {

        // save old stroke
        Stroke oldStroke = g2.getStroke();
        // set new thickness
        g2.setStroke(new BasicStroke(thickness));

        int n = orbitGameCoords.size();
        int[] xPts = new int[n];
        int[] yPts = new int[n];

        // build the polygon points, offset by the center
        for (int i = 0; i < n; i++) {
            Vector2D p = orbitGameCoords.get(i);
            xPts[i] = (int) p.x;
            yPts[i] = (int) p.y;
        }

        g2.setColor(Color.white);
        // draw as an open polyline (won’t force a straight line back to the start)
        g2.drawPolyline(xPts, yPts, n);

        // restore original stroke
        g2.setStroke(oldStroke);
    }

    /**
     * @param centralBody the body you want to orbit (e.g. the Sun)
     * @return a Vector2D giving the initial velocity for a circular orbit
     */
    public Vector2D optimalOrbitalVelocity(CelestrialBody centralBody) {
        // 1) compute the vector from centralBody to this body
        Vector2D delta = Vector2D.subtract(this.pos, centralBody.pos);

        // 2) radial distance
        double r = delta.length();
        if (r == 0) {
            // overlapping bodies → no defined orbit
            return new Vector2D(0, 0);
        }

        // 3) speed magnitude for a circular orbit: v = sqrt(G * M / r)
        double speed = Math.sqrt(PHYSICS_CONSTANT.G * centralBody.mass / r);

        // 4) build a unit‐tangent vector perpendicular to delta
        // (rotate delta by +90°: (x,y) → (-y, x), then normalize)
        float ux = (float) (-delta.y / r);
        float uy = (float) (delta.x / r);

        // 5) scale the unit‐tangent by the desired speed
        return new Vector2D(ux * (float) speed,
                uy * (float) speed);
    }

    /*
     * Display the position,radius and mass of the celestrial body
     * in a string format.
     * @return A string representation of the celestrial body.
     * This method is useful for debugging and logging purposes.
     */
    @Override
    public String toString() {
        return "Pos: " + "[" + pos.x * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE + " " + "," + " "
                + pos.y * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE + "]" + " Radius:" + radius + " Mass:" + mass;
    }
}