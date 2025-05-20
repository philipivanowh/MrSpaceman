package Game;

import java.awt.Color;

//Constant class
public class Constant {

    public static class GAME_CONSTANT {
        // Window size
        public static int WINDOW_WIDTH = 1480;
        public static int WINDOW_HEIGHT = 1080;

        // Game window size
        public static int GAME_WIDTH = 100000;
        public static int GAME_HEIGHT = 100000;

            // Frame rate
    public static final int FPS_SET = 60;
    public static final double G_Constant = 0;

    // Colors
    public static Color SPACE_COLOR = new Color(25, 26, 28);

    }



    // Gravity
    public static class PHYSICS_CONSTANT {
        
    public static final double AU = 149.6e6 * 1000; // Distance from the sun
    public static final double G = 6.67428e-11;
    public static final double AU_TO_PIXELS_SCALE = 250/AU; // 1AU = 100 pixels
    
    public static final double PIXELS_TO_AU_SCALE = AU/250; // 1AU = 100 pixels

    public static final double TIMESTEP = 3600*24; // Simulating how much time is elapsed in the simulation. One day of time
                                              // step
    }

    public static class PLAYER_CONST {
        public static final double VEL_DECAY = .9f;

        public static final int SHIP_W = 100; // px after scaling
        public static final int SHIP_H = 100;
        public static final double SHIP_SPEED = 300.0; // px / s
        public static final double SIDE_SPEED = 150; // rad / s

        // Rotational dampening PID
        // how fast you can turn (radians per second)
        public static final double MAX_TURN_SPEED = Math.toRadians(360);

        // dead-zone value
        public static final double ANGLE_DEADZONE = Math.toRadians(1); // ~1°
    }

    public enum ThrustType {
        CENTER,
        LEFT,
        RIGHT
    };

}
