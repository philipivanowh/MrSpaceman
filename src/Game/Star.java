package Game;

import Game.utils.Vector2D;
import java.awt.Graphics2D;
import java.util.Vector;

/*
 * Star class represents the star in the game
 * Star consist of the x,y coord properties and depth
 * Depth is used to create a parrallex effect on the screen
 */
public class Star {
    final Vector2D pos;
    final float depth;

    public Star(double x, double y, float depth) {
        this.pos = new Vector2D(x, y);
        this.depth = depth;
    }
}