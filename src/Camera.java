

public class Camera {

    private float x, y;

    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void tick(Entity object) {

      // target so that the player sits in the center of the window
    float targetX = object.pos.x - Constant.WINDOW_WIDTH  / 2f;
    float targetY = object.pos.y - Constant.WINDOW_HEIGHT / 2f;

    // smooth follow
    x += (targetX - x) * 0.05f;
    y += (targetY - y) * 0.05f;

    if(x <= 0) x = 0;
    if(x >= Constant.GAME_WIDTH) x = Constant.GAME_WIDTH;
    if(y <= 0) y = 0;
    if(y >= Constant.GAME_HEIGHT) y = Constant.GAME_HEIGHT;


    }

    public float getX() {
        return x;
    }

    public void setX(float x) {

    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
