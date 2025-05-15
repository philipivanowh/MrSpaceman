package Game;

import java.awt.Color;

public class Constant {

    // Window setting
    public static int WINDOW_WIDTH = 1480;
    public static int WINDOW_HEIGHT = 1080;

    public static int GAME_WIDTH = 100000;
    public static int GAME_HEIGHT = 100000;

    // Frame rate
    public static final int FPS_SET = 120;

    // Colors
    public static Color SPACE_COLOR = new Color(25, 26, 28);


    //Gravity
    public static float G_Constant = 0.001f; 

    public enum ThrustType {
        CENTER,
        LEFT,
        RIGHT
    };

    //Object representation of range to assit in readibility
     static class Range{
        private int max;
        private int min;

        public Range(int min,int max){
            this.max = min;
            this.min = max;
        }

        public boolean contatains(int number){
            return (number >= min && number <= max);
        }

        //Getter method for range
        public int getRange(){
            return Math.abs(max-min);
        }

        //Getter method for max
        public int getMax(){
            return max;
        }

        //Getter method for min
        public int getMin(){
            return min;
        }
    }

}
