package Game;

import Game.Constant.GAME_CONSTANT;
import Game.utils.*;

public class Camera {
    final private Vector2D pos = new Vector2D();

    public Camera(double x, double y) {
        pos.x = x;
        pos.y = y;
    }

    public void follow(Entity object) {
        // target so that the player sits in the center of the window
        double targetX = object.pos.x - GAME_CONSTANT.WINDOW_WIDTH / 2f;
        double targetY = object.pos.y - GAME_CONSTANT.WINDOW_HEIGHT / 2f;

        // smooth follow
        pos.x += (targetX - pos.x) * 0.05f;
        pos.y += (targetY - pos.y) * 0.05f;
    }

    public double getX() {
        return pos.x;
    }

    public void setX(double x) {
        this.pos.x = x;
    }

    public double getY() {
        return pos.y;
    }

    public void setY(double y) {
        this.pos.y = y;
    }
}
