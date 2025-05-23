package Game;

import Game.utils.Range;
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
        public static final double AU_TO_PIXELS_SCALE = 7000 / AU; // 1AU = 100 pixels

        public static final double PIXELS_TO_AU_SCALE = AU / 7000; // 1AU = 100 pixels
                                                        
        public static final double TIMESTEP_FAST = 3600 * 6;    // half day  time
        public static final double TIMESTEP_DEFAULT = 3600;
        // step
        // Our solar system's data

        // Each planets's distance to the sun
        public static final double EARTH_DISTANCE_TO_SUN_AU = AU;

        public static final double MARS_DISTANCE_TO_SUN_AU = 1.524 * AU;

        public static final double MERCURY_DISTANCE_TO_SUN_AU = 0.387 * AU;

        public static final double VENUS_DISTANCE_TO_SUN_AU = 0.723 * AU;

        
        public static final Range AMOUNT_OF_ORBIT_RANGE = new Range(3, 4);

        
        public static final Range CELESTRIAL_BODY_RADIUS_RANGE = new Range(8, 16);


        public static final Range FIRST_PLANET_DISTNACE_TO_SUN_RANGE = new Range(.3, 0.5);
        
        public static final Range SECOND_PLANET_DISTNACE_TO_SUN_RANGE = new Range(.75, 0.9);
        
        public static final Range THIRD_PLANET_DISTNACE_TO_SUN_RANGE = new Range(1.15, 1.3);
        
        public static final Range FOURTH_PLANET_DISTNACE_TO_SUN_RANGE = new Range(1.6, 1.7);
        
        public static final Range FITH_PLANET_DISTNACE_TO_SUN_RANGE = new Range(1.9, 1.95);

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
