package Game;

import Game.Constant.PLAYER_CONST;
import Game.Constant.ThrustType;
import Game.utils.FrameAnimation;
import Game.utils.Vector2D;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends Entity {

    final private FrameAnimation currAnimation;
    final private FrameAnimation idleAnimation;

    private List<TrailParticle> particles = new ArrayList<>();

    public Player(double x, double y) {
        super(x, y, 1);

        idleAnimation = new FrameAnimation(0.1f, false);
        idleAnimation.loadFrames(new String[] {"Images/rocket.png"});

        currAnimation = idleAnimation;
    }

    // update the smoke particles
    private void updateParticles() {
        Iterator<TrailParticle> it = particles.iterator();
        while (it.hasNext()) {
            TrailParticle p = it.next();
            p.update();
            if (!p.isAlive()) {
                it.remove();
            }
        }
    }

    private void updateGravity(){
        force.x = 0;
        force.y = 0;

        Vector2D gForce = attraction(Orbitor.currentSolarSystem.root);
    
        force.x += gForce.x;
        force.y += gForce.y;
        System.out.println("gFoce" + gForce);

        vel.x += force.x/mass;
        vel.y += force.y/mass;
    }

    public void update(double dt) {

        updateParticles();
        updateGravity();
        
        // Compute target rotation
        Vector2D diff = Vector2D.subtract(Input.getMouseRelativeToWorld(), this.pos);
        double targetAngle = diff.getAngle();

        // 1) error between where you are and where you want to point
        double error = clampAngle(targetAngle - this.angle);

        // 2) only turn if the error is outside the dead-zone
        if (Math.abs(error) > PLAYER_CONST.ANGLE_DEADZONE) {
            // clamp your turn to MAX_TURN_SPEED * dt
            double maxDelta = PLAYER_CONST.MAX_TURN_SPEED * dt;
            double delta = Math.signum(error) * Math.min(Math.abs(error), maxDelta);
            this.angle = (double) clampAngle(this.angle + delta);
        }

        // main thrust
        if (Input.isThrusting()) {
            this.vel.x += Math.cos(this.angle) * PLAYER_CONST.SHIP_SPEED * dt;
            this.vel.y += Math.sin(this.angle) * PLAYER_CONST.SHIP_SPEED * dt;

            // spawn a new trail particle just below the ship
            particles.add(new TrailParticle(this.pos.x, this.pos.y, this.angle, ThrustType.CENTER));
        }
        // left thrust
        if (Input.isLeftThrusting()) {
            this.vel.x += Math.cos(this.angle - Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;
            this.vel.y += Math.sin(this.angle - Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;

            particles.add(new TrailParticle(this.pos.x, this.pos.y, this.angle, ThrustType.LEFT));
        }
        // right thrust
        if (Input.isRightThrusting()) {
            this.vel.x += Math.cos(this.angle + Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;
            this.vel.y += Math.sin(this.angle + Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;

            particles.add(new TrailParticle(this.pos.x, this.pos.y, this.angle, ThrustType.RIGHT));
        }

        velocityDecay(dt);
        this.pos.x += this.vel.x * dt;
        this.pos.y += this.vel.y * dt;
    }

    // keep this.angle within (-π,π]
    private double clampAngle(double a) {
        while (a <= -Math.PI)
            a += 2 * Math.PI;
        while (a > Math.PI)
            a -= 2 * Math.PI;
        return a;
    }

    //method to decay the velocity of the ship
    private void velocityDecay(double dt) {
        if (this.vel.length() > .1) {
            double factor = (double) Math.pow(PLAYER_CONST.VEL_DECAY, dt);

            if (Math.abs(this.vel.x) > .1)
                this.vel.x *= factor;
            else
                this.vel.x = 0;

            if (Math.abs(this.vel.y) > .1)
                this.vel.y *= factor;
            else
                this.vel.y = 0;
        } else {
            this.vel = Vector2D.ZERO;
        }

    }

    // Rendering the player and create a sprite
    @Override
    public void render(Graphics2D g2) {
        AffineTransform old = g2.getTransform();

        // move to the ship’s center (world coords)
        g2.translate(this.pos.x, this.pos.y);
        // then rotate around its own center
        g2.rotate(this.angle + Math.PI / 2);

        BufferedImage sprite = currAnimation.getFrame();
        g2.drawImage(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2, null);
        g2.setTransform(old);

        renderParticle(g2);
    }

    public void renderParticle(Graphics2D g2) {
        for (TrailParticle p : particles) {
            p.render(g2);
        }
    }
}
