package Game;

import Game.Constant.PHYSICS_CONSTANT;
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

    private final FrameAnimation currAnimation;
    private final FrameAnimation idleAnimation;
    private final List<TrailParticle> particles = new ArrayList<>();
    private CelestrialBody collidingBody;
    private final float GravityStrengthModifier = 7;

    // small fudge factor for collision radius
    private static final double COLLIDE_MOD = 0.97;

    public Player(double x, double y) {
        super(x, y, PLAYER_CONST.SHIP_W, PLAYER_CONST.SHIP_H, 1e14);
        idleAnimation = new FrameAnimation(0.1f, false);
        idleAnimation.loadFrames(new String[] { "Images/rocket.png" });
        currAnimation = idleAnimation;
    }

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

    /**
     * Always apply gravity (converted to px/s²) before checking collisions.
     */
    private void updateGravity() {
        updateNetGravitationalForce(Orbitor.currentSolarSystem.getCelestrialBodies());
        double ax = (force.x / mass);
        double ay = (force.y / mass);
        vel.x += ax;
        vel.y += ay;
    }

    public void update(double dt) {
        // update the player's particle
        updateParticles();
        // update the gravitational force
        updateGravity();
        // collision handling
        calculateCollisionWithPlanet(Orbitor.currentSolarSystem, dt);
        // determine if on-ground
        collidingBody = checkCollisionWithPlanets(Orbitor.currentSolarSystem);
        System.out.println(collidingBody);

        // rotation only when not colliding
        if (collidingBody == null) {
            // rotation PID
            Vector2D diff = Vector2D.subtract(Input.getMouseRelativeToWorld(), this.pos);
            double targetAngle = diff.getAngle();
            double err = clampAngle(targetAngle - this.angle);
            if (Math.abs(err) > PLAYER_CONST.ANGLE_DEADZONE) {
                double maxD = PLAYER_CONST.MAX_TURN_SPEED * dt;
                double d = Math.signum(err) * Math.min(Math.abs(err), maxD);
                this.angle = clampAngle(this.angle + d);
            }
        }
        // main thrust
        if (Input.isThrusting()) {
            this.vel.x += Math.cos(this.angle) * PLAYER_CONST.SHIP_SPEED * dt;
            this.vel.y += Math.sin(this.angle) * PLAYER_CONST.SHIP_SPEED * dt;
            particles.add(new TrailParticle(this.pos.x, this.pos.y, this.angle, ThrustType.CENTER));
        }

        if (collidingBody == null) {
            // side thrust
            if (Input.isLeftThrusting()) {
                this.vel.x += Math.cos(this.angle - Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;
                this.vel.y += Math.sin(this.angle - Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;
                particles.add(new TrailParticle(this.pos.x, this.pos.y, this.angle, ThrustType.LEFT));
            }
            if (Input.isRightThrusting()) {
                this.vel.x += Math.cos(this.angle + Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;
                this.vel.y += Math.sin(this.angle + Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;
                particles.add(new TrailParticle(this.pos.x, this.pos.y, this.angle, ThrustType.RIGHT));
            }

        }

        // velocity decay
        velocityDecay(dt);
        // apply velocity
        this.pos.x += vel.x * dt;
        this.pos.y += vel.y * dt;
    }

    /**
     * Compute sum of gravitational forces (N) from all bodies.
     */
    public void updateNetGravitationalForce(List<CelestrialBody> bodies) {
        force.x = force.y = 0;
        for (CelestrialBody body : bodies) {
            Vector2D g = attraction(body, Vector2D.multiply(pos, PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE));
            if (body.bodyType == Constant.CELESTRIAL_BODY_TYPE.SUN) {
                g.multiply(GravityStrengthModifier);
            } else if (body.bodyType == Constant.CELESTRIAL_BODY_TYPE.PLANET) {
                g.multiply(GravityStrengthModifier * 1e5);
            }
            force.x += g.x;
            force.y += g.y;
        }
    }

    /**
     * Collision resolution that handles moving platforms: - pushes out of
     * overlap - applies relative velocity bounce - carries the player with the
     * platform
     */
    private void calculateCollisionWithPlanet(SolarSystem system, double dt) {
        CelestrialBody body = checkCollisionWithPlanets(system);
        if (body == null) {
            return;
        }

        vel.x = 0;
        vel.y = 0;
        
    
    }

    /**
     * Simple circle-based collision detection.
     */
    public CelestrialBody checkCollisionWithPlanets(SolarSystem system) {
        for (CelestrialBody body : system.getCelestrialBodies()) {
            Vector2D bodyPx = Vector2D.multiply(body.pos, PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE);
            double d = Vector2D.subtract(pos, bodyPx).length();
            if (d < (this.width / 2 + body.width / 2) * COLLIDE_MOD) {
                return body;
            }
        }
        return null;
    }

    @Override
    public void render(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.translate(pos.x, pos.y);
        g2.rotate(angle + Math.PI / 2);
        BufferedImage img = currAnimation.getFrame();
        g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);
        g2.setTransform(old);
        for (TrailParticle p : particles) {
            p.render(g2);
        }
    }

    private double clampAngle(double a) {
        while (a <= -Math.PI) {
            a += 2 * Math.PI;
        }
        while (a > Math.PI) {
            a -= 2 * Math.PI;
        }
        return a;
    }

    private void velocityDecay(double dt) {
        if (vel.length() <= .1) {
            vel = Vector2D.ZERO;
            return;
        }
        double f = Math.pow(PLAYER_CONST.VEL_DECAY, dt);
        vel.x = Math.abs(vel.x) > .1 ? vel.x * f : 0;
        vel.y = Math.abs(vel.y) > .1 ? vel.y * f : 0;
    }
}
