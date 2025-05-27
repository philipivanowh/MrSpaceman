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

/*
 * Player class represents the player-controlled spaceship in the game
 * Player consist of three movement modes: main thrust, left thrust, right thrust
 * Player is affected by gravity from celestrial bodies in the solar system
 * Player can collide with celestrial bodies, which are planets or the sun
 * Player can leave a trail of particles when thrusting
 * Player can rotate towards the mouse cursor when not colliding with a celestrial body
 * Player can decay velocity over time
 * Player can be controlled using the keyboard and mouse
 * Player can be rendered on the screen
 * Player can be updated based on the time delta
 */
public class Player extends Entity {

    private final FrameAnimation currAnimation;
    private final FrameAnimation idleAnimation;
    private final List<TrailParticle> particles = new ArrayList<>();
    private CelestrialBody collidingBody;

    // gravitational strength adjuster applied for smoother gameplay
    private final float GravityStrengthModifier = 7;
    private final float PlanetGravityStrengthModifier = GravityStrengthModifier * 1e5f;

    // small fudge factor for collision radius
    private static final double COLLIDE_MOD = 0.97;
    private static boolean escapingGravity = false;

    /**
     * Constructor for the Player class.
     * Initializes the player at a given position with a default size and mass.
     *
     * @param x The x-coordinate of the player's position.
     * @param y The y-coordinate of the player's position.
     */
    public Player(double x, double y) {
        super(x, y, PLAYER_CONST.SHIP_W, PLAYER_CONST.SHIP_H, 1e14);
        idleAnimation = new FrameAnimation(0.1f, false);
        idleAnimation.loadFrames(new String[] { "Images/rocket.png" });
        currAnimation = idleAnimation;
    }

    /*
     * Updating the trail particles left by the player.
     */
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
        acc.x = (force.x / mass);
        acc.y = (force.y / mass);
    }

    public void update(double dt) {

        // determine if on-ground
        collidingBody = checkCollisionWithPlanets(Orbitor.currentSolarSystem);
        // update the player's particle
        updateParticles();
        // update the gravitational force
    
        if(collidingBody == null){
        updateGravity();
        } else {
            // if colliding with a planet, set acceleration to zero
            acc.x = 0;
            acc.y = 0;
        }
        
        // collision handling
        calculateCollisionWithPlanet(Orbitor.currentSolarSystem, dt);
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
            if(collidingBody != null){
                // if colliding with a planet, set velocity to zero
                escapingGravity = false;
            }

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

        // apply acceleration to velocity
        vel.x += acc.x;
        vel.y += acc.y;

        if(collidingBody != null){
            //vel.x = 0;
           // vel.y = 0;
        }

        // apply velocity to position
        this.pos.x += vel.x * dt;
        this.pos.y += vel.y * dt;
    }

    /*
     * Compute sum of gravitational forces (N) from all bodies.
     */
    public void updateNetGravitationalForce(List<CelestrialBody> bodies) {
        force.x = force.y = 0;
        for (CelestrialBody body : bodies) {
            Vector2D g = attraction(body, Vector2D.multiply(pos, PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE));
            if (body.bodyType == Constant.CELESTRIAL_BODY_TYPE.SUN) {
                g.multiply(GravityStrengthModifier);
            } else if (body.bodyType == Constant.CELESTRIAL_BODY_TYPE.PLANET) {
                g.multiply(PlanetGravityStrengthModifier);
            }
            force.x += g.x;
            force.y += g.y;
        }
    }

    /**
     * Collision resolution that handles moving platforms by aligning the player
     * with the same acceleratio of the planet
     * platform
     */
    private void calculateCollisionWithPlanet(SolarSystem system, double dt) {
        CelestrialBody body = checkCollisionWithPlanets(system);
        if (body == null) {
            return;
        }
        Vector2D bodyVel = body.getVel();
        Vector2D bodyAcc = body.getAcc();

        pos.x -= vel.x * dt;
        pos.y -= vel.y * dt;

        pos.x += bodyVel.x * PHYSICS_CONSTANT.TIMESTEP + bodyAcc.x;
        pos.y += bodyVel.y  * PHYSICS_CONSTANT.TIMESTEP + bodyAcc.y;

        vel.x = 0;
        vel.y = 0;

       // vel.x = bodyVel.x;
       // vel.y = bodyVel.y;

        ///   vel.x += bodyAcc.x;
        // vel.y += bodyAcc.y;

    }

    /**
     * Checks if the player collides with any planets in the solar system.
     * If a collision is detected, it returns the colliding planet.
     * Otherwise, it returns null.
     *
     * @param system The SolarSystem object containing all celestial bodies.
     * @return The CelestrialBody that the player collides with, or null if no
     *         collision occurs.
     */
    public CelestrialBody checkCollisionWithPlanets(SolarSystem system) {
        for (CelestrialBody body : system.getCelestrialBodies()) {
            double d = Vector2D.subtract(pos, Vector2D.multiply(body.pos, PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE))
                    .length();
            if (d < (this.width / 2 + body.width / 2) * COLLIDE_MOD) {
                return body;
            }
        }
        return null;
    }

    /*
     * Renders the player on the screen.
     * The player is drawn at its current position and angle, with the current
     * animation frame. The player also leaves a trail of particles behind.
     * 
     * @param g2 The Graphics2D object used for rendering.
     * This method applies a translation and rotation to the graphics context
     * before drawing the player image, and restores the original transformation
     * after drawing. It also iterates through the list of trail particles and
     * renders each one.
     */
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

    /*
     * Clamps the angle to the range [-PI, PI].
     * This is useful to ensure that the angle does not exceed the limits of
     * the trigonometric functions, which can lead to unexpected behavior.
     * 
     * @param a The angle to be clamped.
     * 
     * @return The clamped angle in radians, within the range [-PI, PI].
     * This method uses a simple while loop to adjust the angle until it falls
     * within the desired range. It adds or subtracts 2*PI as necessary.
     */
    private double clampAngle(double a) {
        while (a <= -Math.PI) {
            a += 2 * Math.PI;
        }
        while (a > Math.PI) {
            a -= 2 * Math.PI;
        }
        return a;
    }

    /*
     * Applies velocity decay to the player's velocity vector.
     * This method reduces the velocity over time to simulate friction or
     * drag in space. The decay factor is based on the PLAYER_CONST.VEL_DECAY
     * constant, which is a value between 0 and 1. The decay is applied
     * separately to the x and y components of the velocity vector.
     * If the velocity is below a certain threshold, it is set to zero.
     * 
     * @param dt The time delta since the last update, used to scale the decay.
     */
    private void velocityDecay(double dt) {
        if (vel.length() <= .1) {
            vel = Vector2D.ZERO;
            return;
        }
        double f = Math.pow(PLAYER_CONST.VEL_DECAY, dt);
        vel.x = Math.abs(vel.x) > .1 ? vel.x * f : 0;
        vel.y = Math.abs(vel.y) > .1 ? vel.y * f : 0;
    }

    @Override
    public Vector2D getPos() {
        return this.pos;
    }

    @Override
    public Vector2D getVel() {
        return this.vel;
    }

    @Override
    public Vector2D getAcc() {
        return this.acc;

    }

    @Override
    public Vector2D getForce() {
        return this.force;
    }
}
