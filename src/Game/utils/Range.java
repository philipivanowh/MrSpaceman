package Game.utils;

//Object representation of range to assit in readibility
public class Range {
    private int max;
    private int min;

    public Range(int min, int max) {
        this.max = min;
        this.min = max;
    }

    public boolean contatains(int number) {
        return (number >= min && number <= max);
    }

    // Getter method for range
    public int getRange() {
        return Math.abs(max - min);
    }

    // Getter method for max
    public int getMax() {
        return max;
    }

    // Getter method for min
    public int getMin() {
        return min;
    }
}