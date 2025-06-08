package Game;

import Game.utils.Range;
import Game.utils.Vector2D;
import java.awt.Color;

/*
 * Constant class contains all the constants used in the game   
 * It includes game constants, physics constants, player constants, and celestial body types.
 * It provides a centralized place to manage these constants for easy access and modification.
 * The constants are organized into nested classes for better structure and readability.
 * The GAME_CONSTANT class contains constants related to the game window size, frame rate, and colors.
 * The PHYSICS_CONSTANT class contains constants related to physics calculations, such as gravitational constant, astronomical unit, and distance scales.
 * The PLAYER_CONST class contains constants related to the player ship's properties, such as speed, size, and rotational dampening.
 * The CELESTIAL_BODY_TYPE enum defines the types of celestial bodies in the game, such as sun, planet, moon, and black hole.
 * The ThrustType enum defines the types of thrust available for the player ship, such as center, left, and right.
 * This class is used throughout the game to access these constants without hardcoding values, making it easier to maintain and update the game.
 */
public class Constant {

    //UI
    public static class MENU {
        //Buttons
		public static class BT {
			public static final int MENU_BUTTON_WIDTH = 200;
			public static final int MENU_BUTTON_HEIGHT = 50;
            public static final int MENU_CENTER_X = GAME_CONSTANT.WINDOW_WIDTH / 2 - 100;

            //Buttons
            public static final int playButtonY = 150;

            public static final int instructionButtonY = 250;

            public static final int quitButtonY = 350;



            public static final int settingsButtonX = GAME_CONSTANT.WINDOW_WIDTH / 2 + 150;
            public static final int settingsButtonY = 370;

            public static final int settingsButtonWidth = 50;
            public static final int settingsButtonHeight = 50; 
		}

        public static class BG{
            //SUN
            public static final int SUN_RADIUS = 200;
            public static final int SUN_X = GAME_CONSTANT.WINDOW_WIDTH / 2 - 100;
            public static final int SUN_Y = 500;
            public static final Color SUN_COLOR = new Color(255, 204, 0); // Orange color for the sun

            public static final Color HIGHLIGHT_SUN_COLOR = new Color(255, 254, 0); // Orange color for the sun

            //Astroid
            public static final int ASTROID_RADIUS = 30;
            public static final Color ASTROID_COLOR = new Color(128, 128, 128); // Gray color for asteroids
        }
	}

    public static class GAME_CONSTANT {
        // Window size
        public static int WINDOW_WIDTH = 1080;
        public static int WINDOW_HEIGHT = 1080;

        // Game window size
        public static int GAME_WIDTH = 100000;
        public static int GAME_HEIGHT = 100000;

        //SOLAR SYSTEM GRID
        public static int SOLAR_SYSTEM_SIZE = 26500; // px

        public static int  GAME_WIDTH_GRID = (int)GAME_WIDTH/SOLAR_SYSTEM_SIZE; // number
        public static int  GAME_HEIGHT_GRID = (int)GAME_HEIGHT/SOLAR_SYSTEM_SIZE; // number

        // Frame rate
        public static final int FPS_SET = 60;
        public static final double G_Constant = 0;

        //Zoom
        public static final Range scaleRange = new Range(0.05,1.5);
        public static final double defaultScale = 1.0;
        public static final Vector2D scaleScrollRateOfChange = new Vector2D(0.02,0.02);

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

        //Sun information
        public static final double SUN_MASS = 1.98892e30;

        public static final int SUN_RADIUS = 30;

        //Black hole information
        public static final double BLACK_HOLE_MASS = 1.5e30;
        public static final int BLACK_HOLE_RADIUS = 13;
        
        // Each planets distance to the sun
        public static final double EARTH_DISTANCE_TO_SUN_AU = AU;

        public static final double MARS_DISTANCE_TO_SUN_AU = 1.524 * AU;

        public static final double MERCURY_DISTANCE_TO_SUN_AU = 0.387 * AU;

        public static final double VENUS_DISTANCE_TO_SUN_AU = 0.723 * AU;

        public static final Range BLACK_DISTANCE_TO_SUN_AU = new Range(2,2.2);

        public static final Range AMOUNT_OF_ORBIT_RANGE = new Range(3, 4);

        // The radius of the celestial body in pixels
        public static final Range CELESTIAL_BODY_RADIUS_RANGE = new Range(5, 15);

        // The distance of the planets from the sun based on its orbit index/
        // The values are in AU scale
        public static final Range FIRST_PLANET_DISTNACE_TO_SUN_RANGE = new Range(.4, 0.5);

        public static final Range SECOND_PLANET_DISTNACE_TO_SUN_RANGE = new Range(.75, 0.9);

        public static final Range THIRD_PLANET_DISTNACE_TO_SUN_RANGE = new Range(1.15, 1.3);

        public static final Range FOURTH_PLANET_DISTNACE_TO_SUN_RANGE = new Range(1.6, 1.7);

        public static final Range FIFTH_PLANET_DISTNACE_TO_SUN_RANGE = new Range(1.9, 1.95);

    }

    // The type of celestial body
    public enum CELESTIAL_BODY_TYPE {
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
