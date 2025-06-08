package Game.utils;

//Object representation of range to assit in readibility
public class Range {
    private double max;
    private double min;

    public Range(double min, double max) {
        if (min>max) {
            System.err.println("Your Min is larger than your max");
            System.exit(0);
        }
        this.max = max;
        this.min = min;
    }

    public boolean contatains(double number) {
        return (number >= min && number <= max);
    }

    // Getter method for range
    public double getRange() {
        return Math.abs(max - min);
    }

    // double method for max
    public double getMax() {
        return max;
    }

    // Getter method for min
    public double getMin() {
        return min;
    }
}