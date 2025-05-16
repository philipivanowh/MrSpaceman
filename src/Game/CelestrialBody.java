package Game;

import Game.Constant.GAME_CONSTANT;
import Game.utils.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class CelestrialBody extends Entity {
    public int radius = 0;

    private Color ovalColor;
    private int pixelSize;

    private Color color;

    private int glowSize;

    // Is is the root celetrial body
    public boolean root;
    // Is the celetrial body a sun
    public boolean sun;



    public CelestrialBody(int x, int y, int radius, float mass, boolean sun, boolean root, Color color) {
        super(x, y, mass);
        pos.x = x;
        pos.y = y;
        this.radius = radius;
        this.sun = sun;
        this.root = root;
        glowSize = (int) (radius * 0.2);
        this.color = color;

    }

    // Render a pixalated oval
    @Override
    public void render(Graphics2D g2) {

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw glow effect
        if(root == true)
            g2.setColor(Color.orange);
        else{
            g2.setColor(color);
        }

        g2.fillOval((int) pos.x, (int) pos.y, radius, radius);
    }

    // Method use for finding the velocity of the orbiting planet
    public float OptimalOrbitalVelocity(Entity parentPlanet) {

        float distance = Vector2D.subtract(parentPlanet.pos, this.pos).length();

        // Use the equation v = sqrt( G * M / r)
        float velocity = (float) Math.sqrt(GAME_CONSTANT.G_Constant * parentPlanet.mass / distance);

        return velocity;
    }

    @Override
    public String toString(){
        return "Pos: "+pos.toString()+ " Radius:" + radius + " Mass:" + mass;
    }
}