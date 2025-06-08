package Game;

import Game.Constant.CELESTIAL_BODY_TYPE;
import Game.Constant.PHYSICS_CONSTANT;
import Game.Constant.PLAYER_CONST;
import Game.Constant.ThrustType;
import Game.utils.FrameAnimation;
import Game.utils.Vector2D;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Player class represents the player-controlled spaceship in the game
 * Player consist of three movement modes: main thrust, left thrust, right thrust
 * Player is affected by gravity from celestial bodies in the solar system
 * Player can collide with celestial bodies, which are planets or the sun
 * Player can leave a trail of particles when thrusting
 * Player can rotate towards the mouse cursor when not colliding with a celestial body
 * Player can decay velocity over time
 * Player can be controlled using the keyboard and mouse
 * Player can be rendered on the screen
 * Player can be updated based on the time delta
 */
public class Player extends Entity {

    private final FrameAnimation currAnimation;
    private final FrameAnimation idleAnimation;
    private final List<TrailParticle> particles = new ArrayList<>();
    private CelestialBody collidingBody;

    // gravitational strength adjuster applied for smoother gameplay
    private final double GravityStrengthModifier = 11 * 5e1;
    private final double PlanetGravityStrengthModifier = this.GravityStrengthModifier * 3.5;
    private final double BlackHoleGravityStrengthModifier = this.PlanetGravityStrengthModifier * 2;

    // sticking state
    private CelestialBody stuckBody = null;
    private double stuckAngle = 0; // angle relative to planet center at landing
    private double stuckDistance = 0; // combined radius distance

    private double health = 100;
    private double maxHealth = 100;

    private int width = PLAYER_CONST.SHIP_W;
    private int height = PLAYER_CONST.SHIP_H;
    /**
     * Constructor for the Player class.
     * Initializes the player at a given position with a default size and mass.
     *
     * @param x The x-coordinate of the player's position.
     * @param y The y-coordinate of the player's position.
     */
    public Player(double x, double y) {
        super(x, y, 1e10);
        this.idleAnimation = new FrameAnimation(0.1f, false);
        this.idleAnimation.loadFrames(new String[] { "Images/rocket.png" }, 1);
        this.currAnimation = this.idleAnimation;
    }

    /**
     * Updating the trail particles left by the player.
     */
    private void updateParticles() {
        Iterator<TrailParticle> it = this.particles.iterator();
        while (it.hasNext()) {
            TrailParticle p = it.next();
            p.update();
            if (!p.isAlive()) {
                it.remove();
            }
        }
    }

    /*
     * Always apply gravity (converted to px/s²) before checking collisions.
     */
    private void updateGravity(SolarSystem currentSolarSystem) {
        this.updateNetGravitationalForce(currentSolarSystem.getCelestrialBodies());
        this.acc = Vector2D.divide(this.force, this.mass);
    }

    private void updateEnemyCollision(ArrayList<Enemy> enemies) {
        for(Enemy enemy : enemies) {

        }
    }

    public void update(ArrayList<Enemy> enemies, SolarSystem currentSolarSystem, double dt) {
        // update the player's particle
        this.updateParticles();
        this.updateEnemyCollision(enemies);

        // if currently stuck to a planet
        if (this.stuckBody != null && !Input.isThrusting()) {
            // recompute absolute position by body center + fixed offset
            Vector2D center = this.stuckBody.getPos();
            double px = center.x + Math.cos(this.stuckAngle) * this.stuckDistance;
            double py = center.y + Math.sin(this.stuckAngle) * this.stuckDistance;
            this.pos.x = px;
            this.pos.y = py;
            this.vel.x = 0;
            this.vel.y = 0;
            return;
        }

        // detect landing
        if (currentSolarSystem != null) {
            CelestialBody land = this.checkCollisionWithPlanets(currentSolarSystem);

            if (land != null && !Input.isThrusting()) {
                // stick
                this.stuckBody = land;
                Vector2D cp = land.getPos();
                // vector from planet center to player
                double dx = this.pos.x - cp.x;
                double dy = this.pos.y - cp.y;
                this.stuckAngle = Math.atan2(dy, dx);
                this.stuckDistance = Math.hypot(dx, dy);
                this.vel.x = this.vel.y = 0;
                return;
            }

            // if thrusting while stuck, release
            if (this.stuckBody != null && Input.isThrusting()) {
                Vector2D center = this.stuckBody.getPos();
                double px = center.x + Math.cos(this.stuckAngle) * this.stuckDistance;
                double py = center.y + Math.sin(this.stuckAngle) * this.stuckDistance;
                this.pos.x = px;
                this.pos.y = py;
                Vector2D diff = Vector2D.subtract(this.pos, this.stuckBody.getPos());
                diff.normalize();
                diff.multiply(this.getAcc().length());

                this.vel.x = Math.cos(this.angle) * PLAYER_CONST.SHIP_SPEED * dt;
                this.vel.y = Math.sin(this.angle) * PLAYER_CONST.SHIP_SPEED * dt;
                this.stuckBody = null;
            }

            // ---Free-flight state---
            // update the gravitational force
            this.updateGravity(currentSolarSystem);
        }

        // Rotation
        this.applyRotation(dt);

        // thrust inputs
        this.handleThrust(dt);

        // velocity decay
        this.velocityDecay(dt);

        // apply acceleration and velocity
        this.vel.add(Vector2D.multiply(this.acc, dt));
        this.pos.add(Vector2D.multiply(this.vel, dt));

        if (Input.isThrusting() && currentSolarSystem != null) {
            CelestialBody collided = this.checkCollisionWithPlanets(currentSolarSystem);
            if (collided != null) {
                Vector2D center = collided.getPos();
                Vector2D off = Vector2D.subtract(this.pos, center);
                off.normalize();
                double minDist = (this.width/5 + collided.radius / 2);
                this.pos.x = center.x + off.x * minDist;
                this.pos.y = center.y + off.y * minDist;
            }
        }

    }

    /**
     * Apply rotation to the player
     */
    public void applyRotation(double dt) {
        Vector2D diff = Vector2D.subtract(Input.getMouseRelativeToScreen(), new Vector2D(Constant.GAME_CONSTANT.WINDOW_WIDTH / 2.0, Constant.GAME_CONSTANT.WINDOW_HEIGHT / 2.0));
        double targetAngle = diff.getAngle();
        double err = this.clampAngle(targetAngle - this.angle);
        if (Math.abs(err) > PLAYER_CONST.ANGLE_DEADZONE) {
            double maxD = PLAYER_CONST.MAX_TURN_SPEED * dt;
            this.angle = this.clampAngle(this.angle + Math.signum(err) * Math.min(Math.abs(err), maxD));
        }
    }

    public void handleThrust(double dt) {
        // main thrust
        if (Input.isThrusting()) {
            this.vel.x += Math.cos(this.angle) * PLAYER_CONST.SHIP_SPEED * dt;
            this.vel.y += Math.sin(this.angle) * PLAYER_CONST.SHIP_SPEED * dt;
            this.particles.add(new TrailParticle(this.pos.x, this.pos.y, this.angle, ThrustType.CENTER));
        }

        // Side thrust is only available while in space
        if (this.collidingBody == null) {
            // side thrust
            if (Input.isLeftThrusting()) {
                this.vel.x += Math.cos(this.angle - Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;
                this.vel.y += Math.sin(this.angle - Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;
                this.particles.add(new TrailParticle(this.pos.x, this.pos.y, this.angle, ThrustType.LEFT));
            }
            if (Input.isRightThrusting()) {
                this.vel.x += Math.cos(this.angle + Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;
                this.vel.y += Math.sin(this.angle + Math.PI / 2) * PLAYER_CONST.SIDE_SPEED * dt;
                this.particles.add(new TrailParticle(this.pos.x, this.pos.y, this.angle, ThrustType.RIGHT));
            }

        }

    }

    /**
     * Compute sum of gravitational forces (N) from all bodies.
     */
    public void updateNetGravitationalForce(List<CelestialBody> bodies) {
        this.force.x = this.force.y = 0;
        for (CelestialBody body : bodies) {
            Vector2D g = this.attraction(body, Vector2D.multiply(this.pos, PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE));

            if (body.bodyType == CELESTIAL_BODY_TYPE.SUN)
                g.multiply(this.GravityStrengthModifier);
            else if (body.bodyType == CELESTIAL_BODY_TYPE.PLANET)
                g.multiply(this.PlanetGravityStrengthModifier);
            else if(body.bodyType == CELESTIAL_BODY_TYPE.BLACK_HOLE)
                g.multiply(this.BlackHoleGravityStrengthModifier);

            this.force.add(g);
        }
    }

    /**
     * Checks if the player collides with any planets in the solar system.
     * If a collision is detected, it returns the colliding planet.
     * Otherwise, it returns null.
     *
     * @param currentSolarSystem The SolarSystem object containing all celestial bodies.
     * @return The CelestialBody that the player collides with, or null if no
     *         collision occurs.
     */
    public CelestialBody checkCollisionWithPlanets(SolarSystem currentSolarSystem) {
        for (CelestialBody body : currentSolarSystem.getCelestrialBodies()) {
            if(body.bodyType == Constant.CELESTIAL_BODY_TYPE.BLACK_HOLE)
                continue;

            Vector2D bodyPosPx = body.getPos();
            bodyPosPx.x = (int) bodyPosPx.x;
            bodyPosPx.y = (int) bodyPosPx.y;
            double d = Vector2D.subtract(this.pos, bodyPosPx).length();

            if (d < (this.width/5 + body.radius/2)) {
                return body;
            }
        }
        return null;
    }

    /**
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
        g2.translate(this.pos.x, this.pos.y);
        g2.rotate(this.angle + Math.PI / 2);
        BufferedImage img = this.currAnimation.getFrame();
        this.width = img.getWidth();
        this.height = img.getHeight();
        g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);
        g2.setTransform(old);

        for (TrailParticle p : this.particles) {
            p.render(g2);
        }
    }

    public void renderHealthBar(Graphics2D hud) {
        hud.setColor(Color.green);
        hud.fillRect(10, 40, (int)(this.health / this.maxHealth * 300), 20);

        hud.setColor(Color.white);
        hud.drawRect(10, 40, 300, 20);
    }

    /**
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

    /**
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
        if (this.vel.length() <= 0.1) {
            this.vel = Vector2D.ZERO;
            return;
        }
        double f = Math.pow(PLAYER_CONST.VEL_DECAY, dt);
        this.vel.x = Math.abs(this.vel.x) > 0.1 ? this.vel.x * f : 0;
        this.vel.y = Math.abs(this.vel.y) > 0.1 ? this.vel.y * f : 0;
    }

    /**
     * Get position of the player ship in pixel units
     */
    @Override
    public Vector2D getPos() {
        return this.pos;
    }

    /**
     * get velocity of the player ship in pixel/s.
     */
    @Override
    public Vector2D getVel() {
        return this.vel;
    }

    /**
     * get acceleration of the player ship in pixel/s**2.
     */
    @Override
    public Vector2D getAcc() {
        return this.acc;

    }

    /**
     * get force of the player ship in newton
     */
    @Override
    public Vector2D getForce() {
        return this.force;
    }
}
