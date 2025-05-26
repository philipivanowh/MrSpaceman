package Game;

import Game.utils.Range;
import java.awt.Color;

/*
 * Constant class contains all the constants used in the game   
 * It includes game constants, physics constants, player constants, and celestrial body types.
 * It provides a centralized place to manage these constants for easy access and modification.
 * The constants are organized into nested classes for better structure and readability.
 * The GAME_CONSTANT class contains constants related to the game window size, frame rate, and colors.
 * The PHYSICS_CONSTANT class contains constants related to physics calculations, such as gravitational constant, astronomical unit, and distance scales.
 * The PLAYER_CONST class contains constants related to the player ship's properties, such as speed, size, and rotational dampening.
 * The CELESTRIAL_BODY_TYPE enum defines the types of celestrial bodies in the game, such as sun, planet, moon, and black hole.
 * The ThrustType enum defines the types of thrust available for the player ship, such as center, left, and right.
 * This class is used throughout the game to access these constants without hardcoding values, making it easier to maintain and update the game.
 */
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

        public static final double TIMESTEP_FAST = 3600 * 6; // half day time
        public static final double TIMESTEP = 3600 / 4; // Update the orbit based on quater a seconds
        // step
        // Our solar system's data

        // Each planets's distance to the sun
        public static final double EARTH_DISTANCE_TO_SUN_AU = AU;

        public static final double MARS_DISTANCE_TO_SUN_AU = 1.524 * AU;

        public static final double MERCURY_DISTANCE_TO_SUN_AU = 0.387 * AU;

        public static final double VENUS_DISTANCE_TO_SUN_AU = 0.723 * AU;

        public static final Range AMOUNT_OF_ORBIT_RANGE = new Range(3, 4);

        // The radius of the celestrial body in pixels
        public static final Range CELESTRIAL_BODY_RADIUS_RANGE = new Range(8, 16);

        // The distance of the planets from the sun based on its orbit index/
        // The values are in AU scale
        public static final Range FIRST_PLANET_DISTNACE_TO_SUN_RANGE = new Range(.4, 0.5);

        public static final Range SECOND_PLANET_DISTNACE_TO_SUN_RANGE = new Range(.75, 0.9);

        public static final Range THIRD_PLANET_DISTNACE_TO_SUN_RANGE = new Range(1.15, 1.3);

        public static final Range FOURTH_PLANET_DISTNACE_TO_SUN_RANGE = new Range(1.6, 1.7);

        public static final Range FIFTH_PLANET_DISTNACE_TO_SUN_RANGE = new Range(1.9, 1.95);

    }

    // The type of celestrial body
    public enum CELESTRIAL_BODY_TYPE {
        SUN,
        PLANET,
        MOON,
        BLACK_HOLE
    };

    /*
     * PLAYER_CONST class contains constants related to the player ship's properties.
     * It includes constants for velocity decay, ship dimensions, ship speed, side speed,
     * maximum turn speed, and angle deadzone.
     * These constants are used to define the behavior and appearance of the player ship in the game.
     * The ship dimensions are defined in pixels after scaling, and the speeds are defined in pixels per second or radians per second.
     * The maximum turn speed is defined in radians per second, and the angle deadzone is defined in radians.
     * This class provides a centralized place to manage these constants for easy access and modification.
     */
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

    // The type of thrust for the player ship
    public enum ThrustType {
        CENTER,
        LEFT,
        RIGHT
    };

}
