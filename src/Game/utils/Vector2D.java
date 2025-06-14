package Game.utils;

/*
* Implementation of 2D Vector to assit in readibility and calculation within the program
*/
public class Vector2D {
    public double x = 0;
    public double y = 0;
    public final static Vector2D ZERO = new Vector2D();
    public final static Vector2D UP   = new Vector2D(0, 1);
    public final static Vector2D RIGHT = new Vector2D(1, 0);

    // Constructor with no arguments sets the componnets to zero
    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    // Constructor with arguments sets the components in accordance to the value
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Multiply the vector
    public Vector2D multiply(double factor) {
        this.x *= factor;
        this.y *= factor;
        return this;
    }

    // Divide the vector
    public void divide(double factor) {
        this.x /= factor;
        this.y /= factor;
    }

    public void add(Vector2D b) {
        this.x += b.x;
        this.y += b.y;
    }

    public void subtract(Vector2D b) {
        this.x -= b.x;
        this.y -= b.y;
    }

    // Calculate the length of the vector
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    // Returns the x and y component of the vector
    @Override
    public String toString() {
        return "[ " + this.x + "," + this.y + "]";
    }

    // Return the angle of the vector;
    public double getAngle() {
        return Math.atan2(this.y, this.x);
    }

    // Normalize the vector
    public void normalize() {
        double len = this.length();
        if (len != 0) {
            this.x /= len;
            this.y /= len;
        }
    }

    public Vector2D perpendicular() {
        return new Vector2D(-this.y, this.x);
    }

     /**
     * Compute the “up” direction for a given rotation θ (in radians).
     * Equivalent to rotating the world-up (0,1) by θ.
     */
    public static Vector2D upFromAngle(double angle) {
        return new Vector2D(
                -Math.sin(angle),
                Math.cos(angle)
        );
    }

    /**
     * If you prefer working in degrees:
     */
    public static Vector2D upFromDegrees(double deg) {
        return upFromAngle(Math.toRadians(deg));
    }

    public static Vector2D normalize(Vector2D a){
        double len = a.length();
        if(len == 0)
            return Vector2D.ZERO;
        return new Vector2D(a.x/len, a.y/len);
    }

    public static Vector2D multiply(Vector2D a, double b) {

        return new Vector2D(a.x * b, a.y * b);
    }

    public static Vector2D divide(Vector2D a, double b) {
        if (b == 0)
            return a;

        return new Vector2D(a.x / b, a.y / b);
    }

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.x + b.x, a.y + b.y);
    }

    public static Vector2D subtract(Vector2D a, Vector2D b) {
        return new Vector2D(a.x - b.x, a.y - b.y);
    }

    public static Vector2D zero() {
        return new Vector2D(0, 0);
    }

    public static double dot(Vector2D a, Vector2D b) {
        return  a.x * b.x + a.y * b.y;
    }
}
