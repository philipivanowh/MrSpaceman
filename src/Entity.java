public abstract class Entity {
    
    public Entity(float x, float y){
        pos.x = x;
        pos.y = y;
    }

    protected Vector2D pos = new Vector2D();
    protected Vector2D vel = new Vector2D();

    protected float angle;
}
