package Game;

// Particle.java
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class Particle {
    protected double x, y;
    protected double vx, vy;
    protected int   life, maxLife;
    protected BufferedImage image;

    public Particle(double x, double y, double vx, double vy, int lifespan, BufferedImage img) {
        this.x       = x;
        this.y       = y;
        this.vx      = vx;
        this.vy      = vy;
        this.maxLife = lifespan;
        this.life    = lifespan;
        this.image   = img;
    }

    /** move and age */
    public void update() {
        x   += vx;
        y   += vy;
        life = Math.max(0, life - 1);
    }

    /** draw with fading alpha */
    public void render(Graphics2D g2) {
        double alpha = life / (double)maxLife;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));

        AffineTransform at = AffineTransform.getTranslateInstance(
            x - image.getWidth()/2f,
            y - image.getHeight()/2f
        );
        g2.drawImage(image, at, null);

        // restore full‐opacity for whatever draws next
        g2.setComposite(AlphaComposite.SrcOver);
    }

    public boolean isAlive() {
        return life > 0;
    }
}
