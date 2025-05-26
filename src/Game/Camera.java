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
     * @param object The entity to follow (e.g., player).
     */
    public void follow(Entity object) {
        // target so that the player sits in the center of the window
        double targetX = object.pos.x - GAME_CONSTANT.WINDOW_WIDTH / 2f;
        double targetY = object.pos.y - GAME_CONSTANT.WINDOW_HEIGHT / 2f;

        // smooth follow
        pos.x += (targetX - pos.x) * 0.05f;
        pos.y += (targetY - pos.y) * 0.05f;
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
