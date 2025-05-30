package Game;

import Game.utils.Vector2D;
import java.awt.Graphics2D;

/*
 * Star class represents the star in the game
 * Star consist of the x,y coord properties and depth
 * Depth are use to create a parrallex effect on the screen
 */
public class Star extends Entity{

        final float depth;
        Star(double x, double y, float depth) {
            super(x,y,1,1,0);
            this.depth = depth;
        }
        @Override
        public void render(Graphics2D g2) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'render'");
        }
        @Override
        public Vector2D getPos() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getPos'");
        }
        @Override
        public Vector2D getVel() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getVel'");
        }
        @Override
        public Vector2D getAcc() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getAcc'");
        }
        @Override
        public Vector2D getForce() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getForce'");
        }
}