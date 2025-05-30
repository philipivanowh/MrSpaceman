package Game;

import Game.Constant.GAME_CONSTANT;
import Game.utils.*;

/*
 * Camera class represents the camera in the game.
 * It follows a target entity (e.g., player) and keeps it centered in the window.
 * The camera's position is updated smoothly to create a following effect.
 * The camera's position can be retrieved and set, allowing for dynamic camera movements.
 * The camera's position is used to adjust the rendering of entities in the game world.
 * * The camera is initialized with a position, and it can follow an entity by adjusting its position based on the entity's position.
 */
public class Camera {
    final private Vector2D pos = new Vector2D();

    private final Vector2D currentOffset = new Vector2D(0, 0);


    // Look-ahead settings
    private static final double DISTANCE_MULTIPLIER = 0.8;    // similar to Unity distanceMultiplier
    private static final double MAX_OFFSET = 100.0;          // max pixels ahead
    private static final double RESPONSIVENESS = 0.7;   

    /*
     * Constructor for the Camera class.
     * Initializes the camera with based on x and y position
     * @param x The x-coordinate of the camera's position.
     * @param y The y-coordinate of the camera's position.
     */
    public Camera(double x, double y) {
        pos.x = x;
        pos.y = y;
    }

    /*
     * Follows the specified entity by adjusting the camera's position.
     * The camera centers itself on the entity, creating a smooth following effect.
     * 
     * @param player The entity to follow.
     */
    public void follow(Entity player, double dt) {
         // Base target so player is centered
        double targetX = player.pos.x - GAME_CONSTANT.WINDOW_WIDTH  / 2.0;
        double targetY = player.pos.y - GAME_CONSTANT.WINDOW_HEIGHT / 2.0;

        // Compute forward direction
        Vector2D forward = new Vector2D(Math.cos(player.angle), Math.sin(player.angle));

        // Project velocity onto forward vector: dot product
        Vector2D velocity = player.getVel();
        double forwardSpeed = Vector2D.dot(velocity,forward);

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
        currentOffset.x += (targetOffset.x - currentOffset.x) * t;
        currentOffset.y += (targetOffset.y - currentOffset.y) * t;

        // Final camera position
        pos.x = targetX + currentOffset.x;
        pos.y = targetY + currentOffset.y;
    }

    /*
     * Getter method for camera's x position.
     */
    public double getX() {
        return pos.x;
    }

    /*
     * Setter method for camera's x position.
     * 
     * @param x The new x position of the camera's position.
     */
    public void setX(double x) {
        this.pos.x = x;
    }

     /*
     * Getter method for camera's y position.
     */
    public double getY() {
        return pos.y;
    }

     /*
     * Setter method for camera's y position.
     * 
     * @param y The new y position of the camera's position.
     */
    public void setY(double y) {
        this.pos.y = y;
    }
}
