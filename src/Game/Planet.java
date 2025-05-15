package Game;
import java.awt.Color;
import java.awt.Graphics2D;

public class Planet extends Entity{
        public int radius = 0;

        private Color ovalColor;
        private int pixelSize;
        
        Color color;

        public Planet(int x, int y, int radius){
            super(x,y);
            pos.x = x;
            pos.y = y;
            this.radius = radius;
        }

        //Render a pixalated oval
        @Override
        public void render(Graphics2D g2){
            g2.fillOval((int)pos.x, (int)pos.y, radius, radius);
        }

        //Method use for finding the velocity of the orbiting planet
        public float OptimalOrbitalVelocity(Entity parentPlanet){

            float distance = Vector2D.subtract(parentPlanet.pos,this.pos).length();

            //Use the equation v = sqrt( G * M / r)
            float velocity = (float)Math.sqrt(Constant.G_Constant * parentPlanet.mass /distance);

            return velocity;
        }
    }