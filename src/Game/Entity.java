package Game;

import Game.Constant.PHYSICS_CONSTANT;
import Game.utils.Vector2D;
import java.awt.Graphics2D;

//game object
public abstract class Entity {
    
    
    protected Vector2D pos = new Vector2D();
    protected Vector2D vel = new Vector2D();

    protected float angle;

    protected float mass;


    public Entity(float x, float y,float mass){
        pos.x = x;
        pos.y = y;
        mass = mass;
    }


    //Abstract function for rendering
    public abstract void render(Graphics2D g2);

    //Functions that calculates the gravitational force between objects
    public Vector2D gravitationalForce(Entity obj1, Entity obj2){

        Vector2D delta = Vector2D.subtract(obj1.pos, obj2.pos);
        float distance = delta.length();
       float forceMagnitude =  PHYSICS_CONSTANT.G_Constant * obj1.mass * obj2.mass / (distance*distance); 

       delta.normalize();

       return delta.multiply(forceMagnitude);
        


    }
}
