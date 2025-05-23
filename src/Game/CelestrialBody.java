package Game;

import Game.Constant.PHYSICS_CONSTANT;
import Game.utils.Vector2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

public class CelestrialBody extends Entity{
    public double radius = 0;

    Vector2D force = new Vector2D();

    private Color color;

    private int glowSize;

    private double mass;

    public double distance_to_sun;
    // Is the celetrial body a sun
    public boolean sun;

    public CelestrialBody parent;

    public SolarSystem system;

    public ArrayList<Vector2D> orbits = new ArrayList<>();

    public CelestrialBody(double x, double y, double radius, double mass, boolean sun, Color color,
            CelestrialBody parent, SolarSystem system) {
       super(x,y,mass);
        this.radius = radius;
        this.sun = sun;
        glowSize = (int) (radius * 0.2);
        this.color = color;
        this.mass = mass;
        if (!sun) {
            vel.y = optimalOrbitalVelocity(parent).length();
        }

        this.parent = parent;
        this.system = system;

    }

    public void update(ArrayList<CelestrialBody> other) {

        updateNetGravitationalForce(other);
        vel.x += force.x / mass * system.TIMESTEP;
        vel.y += force.y / mass * system.TIMESTEP;

        pos.x += vel.x * system.TIMESTEP;
        pos.y += vel.y * system.TIMESTEP;

        // orbit cord update
        double px = (int) ((pos.x) * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE);
        double py = (int) ((pos.y) * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE);
        orbits.add(new Vector2D(px, py));

    }

    public void updateNetGravitationalForce(ArrayList<CelestrialBody> planets) {
        // clear last frame’s total
        force.x = 0;
        force.y = 0;
        for (CelestrialBody planet : planets) {
            if (planet == this)
                continue;
            Vector2D f = attraction(planet);
            force.x += f.x;
            force.y += f.y;
        }
    }

    // Render a pixalated oval
    public void render(Graphics2D g2) {

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (orbits.size() > 3) {
            // drawOrbit
            drawOrbit(g2, orbits, 3f);
        }

        // Draw glow effect
        if (sun == true)
            g2.setColor(Color.orange);
        else {
            g2.setColor(color);
        }

        int centerX = (int) (pos.x * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE - radius / 2);
        int centerY = (int) (pos.y * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE - radius / 2);

        g2.fillOval(centerX, centerY, (int) radius, (int) radius);

    }

    // Draw an orbit based on its travelled path
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

    public Vector2D attraction(CelestrialBody body) {
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

    @Override
    public String toString() {
        return "Pos: [" + pos.x * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE + ","
                + pos.y * PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE + "]" + " Radius:" + radius + " Mass:" + mass;
    }
}