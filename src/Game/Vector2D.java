package Game;

/*
* Implementation of 2D Vector to assit in readibility and calculation within the program
*/
public class Vector2D {
    public float x=0;
    public float y=0;
    public final static Vector2D ZERO = new Vector2D();
    public final static Vector2D UP   = new Vector2D(0, 1);
    public final static Vector2D RIGHT = new Vector2D(1, 0);

    // Constructor with no arguments sets the componnets to zero
    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    // Constructor with arguments sets the components in accordance to the value
    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Multiply the vector
    public Vector2D multiply(float factor) {
        x *= factor;
        y *= factor;
        return this;
    }

    // Divide the vector
    public void divide(float factor) {
        x /= factor;
        y /= factor;
    }

    // Calculate the length of the vector
    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    // Returns the x and y component of the vector
    @Override
    public String toString() {
        return "[ " + x + "," + y + "]";
    }

    // Return the angle of the vector;
    public double getAngle() {
        return Math.atan2(y, x);
    }

    // Normalize the vector
    public void normalize() {
        float len = length();
        if (len != 0) {
            x /= len;
            y /= len;
        }
    }

       public Vector2D perpendicular() {
        return new Vector2D(-y, x);
    }

     /**
     * Compute the “up” direction for a given rotation θ (in radians).
     * Equivalent to rotating the world-up (0,1) by θ.
     */
    public static Vector2D upFromAngle(double angle) {
        return new Vector2D(
            (float)-Math.sin(angle),
            (float) Math.cos(angle)
        );
    }

    /**
     * If you prefer working in degrees:
     */
    public static Vector2D upFromDegrees(double deg) {
        return upFromAngle(Math.toRadians(deg));
    }

    public static Vector2D normalize(Vector2D a){
        float len = a.length();
        if(len == 0)
        return null;
        return new Vector2D(a.x/len, a.y/len);
    }

    // Static method: subtract vector b from vector a
    public static Vector2D subtract(Vector2D a, Vector2D b) {
        return new Vector2D(a.x - b.x, a.y - b.y);
    }

    // Static method: add vector a and vector b
    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.x + b.x, a.y + b.y);
    }

    public static Vector2D multiply(Vector2D a, float b) {

        return new Vector2D(a.x * b, a.y * b);
    }

    public static Vector2D divide(Vector2D a, float b) {
        if (b == 0)
            return a;

        return new Vector2D(a.x / b, a.y / b);
    }

    public static Vector2D zero() {
        return new Vector2D(0, 0);
    }

}
