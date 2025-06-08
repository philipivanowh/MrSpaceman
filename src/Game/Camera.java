package Game;

import Game.Constant.GAME_CONSTANT;
import Game.utils.*;

/**
 * Camera class represents the camera in the game.
 * It follows a target entity (e.g., player) and keeps it centered in the
 * window.
 * The camera's position is updated smoothly to create a following effect.
 * The camera's position can be retrieved and set, allowing for dynamic camera
 * movements.
 * The camera's position is used to adjust the rendering of entities in the game
 * world.
 * * The camera is initialized with a position, and it can follow an entity by
 * adjusting its position based on the entity's position.
 */
public class Camera {
    final private Vector2D pos = new Vector2D();
    private Vector2D currScale = new Vector2D(1, 1);
    private final Vector2D currentOffset = new Vector2D(0, 0);

    // Look-ahead settings
    private static final double DISTANCE_MULTIPLIER = 0.8; // similar to Unity distanceMultiplier
    private static final double MAX_OFFSET = 100.0; // max pixels ahead
    private static final double RESPONSIVENESS = 0.7;

    /**
     * Constructor for the Camera class.
     * Initializes the camera with based on x and y position
     * 
     * @param x The x-coordinate of the camera's position.
     * @param y The y-coordinate of the camera's position.
     */
    public Camera(double x, double y) {
        this.pos.x = x;
        this.pos.y = y;
    }

    public void update(Entity player, double dt) {
        zoomUpdate(dt);
        follow(player, dt);

    }

    /*
     * update the current zoom status of the game
     */
    private void zoomUpdate(double dt) {

        if (Input.isResetZoomPressed()) {
            currScale.x = GAME_CONSTANT.defaultScale;
            currScale.y = GAME_CONSTANT.defaultScale;
        }

        // If the mouse scrolled down
        if (Input.getScrollRotation() > 0) {
            currScale.subtract(GAME_CONSTANT.scaleScrollRateOfChange);
            Input.resetScrollRotation();
        }
        // if the mouse scrolled up
        else if (Input.getScrollRotation() < 0) {
            currScale.add(GAME_CONSTANT.scaleScrollRateOfChange);
            Input.resetScrollRotation();
        }

        // Clamp the currScale within the max and min scale
        if (currScale.x > GAME_CONSTANT.scaleRange.getMax()) {
            currScale.x = GAME_CONSTANT.scaleRange.getMax();
            currScale.y = GAME_CONSTANT.scaleRange.getMax();

        } else if (currScale.x < GAME_CONSTANT.scaleRange.getMin()) {
            currScale.x = GAME_CONSTANT.scaleRange.getMin();
            currScale.y = GAME_CONSTANT.scaleRange.getMin();

        }

        System.out.println(currScale + "Scale:" + GAME_CONSTANT.scaleRange.getMin());

        // Clamp when it is min scale
    }

    /**
     * Follows the specified entity by adjusting the camera's position.
     * The camera centers itself on the entity, creating a smooth following effect.
     * 
     * @param player The entity to follow.
     */
    private void follow(Entity player, double dt) {

        // Base target so player is centered
        double targetX = player.pos.x;
        double targetY = player.pos.y;

        // Compute forward direction
        Vector2D forward = new Vector2D(Math.cos(player.angle), Math.sin(player.angle));

        // Project velocity onto forward vector: dot product
        Vector2D velocity = player.getVel();
        double forwardSpeed = Vector2D.dot(velocity, forward);

        // Desired offset in pixels
        Vector2D targetOffset = forward.multiply(forwardSpeed * DISTANCE_MULTIPLIER);

        // Clamp magnitude
        double dist = targetOffset.length();
        if (dist > MAX_OFFSET) {
            targetOffset = Vector2D.normalize(targetOffset);
            targetOffset.multiply(MAX_OFFSET);
        }

        // Smooth interpolation (Lerp)
        double t = 1 - Math.exp(-RESPONSIVENESS * dt);
        this.currentOffset.x += (targetOffset.x - this.currentOffset.x) * t;
        this.currentOffset.y += (targetOffset.y - this.currentOffset.y) * t;

        // Final camera position
        this.pos.x = targetX + this.currentOffset.x;
        this.pos.y = targetY + this.currentOffset.y;
    }

    /**
     * Getter method for camera's x position.
     */
    public double getX() {
        return this.pos.x;
    }

    /**
     * Setter method for camera's x position.
     * 
     * @param x The new x position of the camera's position.
     */
    public void setX(double x) {
        this.pos.x = x;
    }

    /**
     * Getter method for camera's y position.
     */
    public double getY() {
        return this.pos.y;
    }

    public Vector2D getScale() {
        return this.currScale;
    }

    /**
     * Setter method for camera's y position.
     * 
     * @param y The new y position of the camera's position.
     */
    public void setY(double y) {
        this.pos.y = y;
    }
}
