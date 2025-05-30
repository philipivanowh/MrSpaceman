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

    public CelestrialBody moon;

    // track the path of the celestrial body
    // This is used to draw the orbit of the celestrial body
    public ArrayList<Vector2D> orbits = new ArrayList<>();

    // store initial orbital parameters for ellipse
    private final double initRau;
    private final double initVxau;
    private final double initVyau;

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
        super(x, y, radius * 2, radius * 2, mass);
        this.radius = radius;
        this.bodyType = bodyType;

        //Useless right now
        glowSize = (int) (radius * 0.2);
        this.color = color;
        this.mass = mass;
        if (bodyType == CELESTRIAL_BODY_TYPE.PLANET) {
            vel.y = optimalOrbitalVelocity(parent).length();
        }

        this.parent = parent;

        // record initial orbital state in AU units
        if (parent != null) {
            Vector2D deltaAU = Vector2D.subtract(this.pos, parent.pos);
            this.initRau = deltaAU.length();
            this.initVxau = vel.x;
            this.initVyau = vel.y;
        } else {
            this.initRau = 0;
            this.initVxau = 0;
            this.initVyau = 0;
        }

    }

    /*
     * Update the position and velocity of the celestrial body based on
     * gravitational forces from other celestrial bodies.
     * updates its velocity based on that force, and then updates its position
     * accordingly.
     * It also updates the orbit path of the celestrial body.
     */
    public void update(ArrayList<CelestrialBody> other) {

        if(bodyType==CELESTRIAL_BODY_TYPE.PLANET){
        updateNetGravitationalForce(other);
        // Acceleration is in AU scale
        acc.x = force.x / mass * PHYSICS_CONSTANT.TIMESTEP;
        acc.y = force.y / mass * PHYSICS_CONSTANT.TIMESTEP;
        // aply acceleration to velocity
        vel.x += acc.x;
        vel.y += acc.y;
        // apply velocity to position
        pos.x += vel.x * PHYSICS_CONSTANT.TIMESTEP;
        pos.y += vel.y * PHYSICS_CONSTANT.TIMESTEP;
        }

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
            if(planet.bodyType == CELESTRIAL_BODY_TYPE.BLACK_HOLE)
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
     * the x and y coordinates are used as the center of the circle instead of the
     * top-left corner
     * 
     * @param g2 The Graphics2D object used for rendering.
     */
    @Override
    public void render(Graphics2D g2) {

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw precomputed orbital ellipse if this has a parent
        if (bodyType == CELESTRIAL_BODY_TYPE.PLANET) {
            Vector2D sunPx = parent.getPos();
            OrbitUtils.drawOrbitEllipse(g2,
                    sunPx.x, sunPx.y,
                    initRau, initVxau, initVyau,
                    parent.mass);
        }

        // set the color for of the planet
        g2.setColor(color);

        int centerX = (int) (pos.x * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE - radius / 2);
        int centerY = (int) (pos.y * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE - radius / 2);

        g2.fillOval(centerX, centerY, (int) radius, (int) radius);

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
     * 
     * @return A string representation of the celestrial body.
     * This method is useful for debugging and logging purposes.
     */
    @Override
    public String toString() {
        return "Pos: " + getPos() + " Radius:" + radius + " Mass:" + mass + " Vel: " + getVel();
    }

    /*
     * Get position of the celestrial body in pixel units
     */
    @Override
    public Vector2D getPos() {
        return new Vector2D(pos.x * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE, pos.y * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE);
    }

    /*
     * get velocity of the celestrial body in pixel/s.
     */
    @Override
    public Vector2D getVel() {
        return new Vector2D(vel.x * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE, vel.y * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE);
    }

    /*
     * get acceleration of the celestrial body in pixel/s**2.
     */
    public Vector2D getAcc() {
        return new Vector2D(acc.x * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE, acc.y * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE);
    }

    /*
     * get force of the celestrial body in Newton
     */
    @Override
    public Vector2D getForce() {
        return new Vector2D(force.x * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE,
                force.y * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE);
    }

    /**
     * Utility class for rendering orbital ellipses.
     */
    private static class OrbitUtils {

        /**
         * Draws the orbital ellipse based on initial conditions.
         * Assumes the central body (focus) is at (sunPxX, sunPxY) in pixel coordinates,
         * and the orbit starts at perihelion on the positive x-axis relative to the
         * sun.
         *
         * @param g2       Graphics2D context to draw into
         * @param sunPxX   X-coordinate of the central body (pixels)
         * @param sunPxY   Y-coordinate of the central body (pixels)
         * @param initRau  Initial distance from sun in astronomical units (AU)
         * @param initVxau Initial velocity X-component (per AU unit in AU/s)
         * @param initVyau Initial velocity Y-component (in AU/s)
         * @param sunMass  Mass of the central body (kg)
         */
        public static void drawOrbitEllipse(Graphics2D g2,
                double sunPxX, double sunPxY,
                double initRau,
                double initVxau, double initVyau,
                double sunMass) {

            // Standard gravitational parameter μ = G * M
            double mu = PHYSICS_CONSTANT.G * sunMass;
            double v2 = initVxau * initVxau + initVyau * initVyau;

            // Specific orbital energy: ε = v²/2 - μ/r
            double energy = 0.5 * v2 - mu / initRau;

            // Semi-major axis: a = -μ / (2ε)
            double a = -mu / (2 * energy);

            // Specific angular momentum magnitude: h = |r × v|
            // If initial position is (r,0), cross product reduces to r * vy
            double h = Math.abs(initRau * initVyau);

            // Eccentricity: e = sqrt(1 - h²/(a μ))
            double e = Math.sqrt(1 - (h * h) / (a * mu));

            // Semi-minor axis: b = a * sqrt(1 - e²)
            double b = a * Math.sqrt(1 - e * e);

            // Focus offset from center: c = a * e
            double cAu = a * e;

            // Convert to pixels
            double scale = PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE;
            int pixA = (int) Math.round(a * scale);
            int pixB = (int) Math.round(b * scale);
            int pixOff = (int) Math.round(cAu * scale);

            // Compute ellipse center in pixel space
            double cx = sunPxX + pixOff;
            double cy = sunPxY;

            // Bounding rectangle for ellipse
            int rx = (int) Math.round(cx - pixA);
            int ry = (int) Math.round(cy - pixB);
            int w = pixA * 2;
            int hpx = pixB * 2;
            Stroke old = g2.getStroke();
            g2.setStroke(new BasicStroke(5));
            g2.setColor(Color.WHITE);

            // Draw ellipse
            g2.drawOval(rx, ry, w, hpx);
            g2.setStroke(old);
        }
    }

}