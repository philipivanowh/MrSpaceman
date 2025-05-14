import java.awt.Color;
import java.awt.Graphics2D;

public class Planet {
        public Vector2D pos = new Vector2D();
        public int radius = 0;
        
        Color color;

        public Planet(int x, int y, int radius){
            pos.x = x;
            pos.y = y;
            this.radius = radius;
        }

        public void render(Graphics2D g2){
            g2.fillOval((int)pos.x, (int)pos.y, radius, radius);
        }
    }