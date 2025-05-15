package Game;

public class Camera {

    private Vector2D gamePos = new Vector2D();

    public Camera(float x, float y) {
      
        gamePos.x = x;
        gamePos.y = y;
    }

    public void tick(Entity object) {

      // target so that the player sits in the center of the window
    float targetX = object.pos.x - Constant.WINDOW_WIDTH  / 2f;
    float targetY = object.pos.y - Constant.WINDOW_HEIGHT / 2f;

    // smooth follow
    gamePos.x += (targetX - gamePos.x) * 0.05f;
    gamePos.y += (targetY - gamePos.y) * 0.05f;

    if(gamePos.x <= 0) gamePos.x = 0;
    if(gamePos.x >= Constant.GAME_WIDTH) gamePos.x = Constant.GAME_WIDTH;
    if(gamePos.y <= 0) gamePos.y = 0;
    if(gamePos.y >= Constant.GAME_HEIGHT) gamePos.y = Constant.GAME_HEIGHT;


    }

      public float getX() {
        return gamePos.x;
    }

    public void setX(float x) {

    }

    public float getY() {
        return gamePos.y;
    }

    public void setY(float y) {
        this.gamePos.y = y;
    }
}
