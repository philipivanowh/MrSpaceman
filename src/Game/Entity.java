package Game;

import java.awt.Graphics2D;

public abstract class Entity {
    
    public Entity(float x, float y){
        pos.x = x;
        pos.y = y;
    }

    protected Vector2D pos = new Vector2D();
    protected Vector2D vel = new Vector2D();

    protected float angle;

    protected float mass;

    //Abstract function for rendering
    public abstract void render(Graphics2D g2);

    //Functions that calculates the gravitational force between objects
    public Vector2D gravitationalForce(Entity obj1, Entity obj2){

        Vector2D delta = Vector2D.subtract(obj1.pos, obj2.pos);
        float distance = delta.length();
       float forceMagnitude =  Constant.G_Constant * obj1.mass * obj2.mass / (distance*distance); 

       delta.normalize();

       return delta.multiply(forceMagnitude);
        


    }
}
