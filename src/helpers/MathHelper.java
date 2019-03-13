package helpers;

public class MathHelper {
    public static double clamp(double value, double min, double max) {
        if (value < min) return min;
        else if (value > max) return max;
        else return value;
    }

    public static double round(double value, int decimals) {
        double roundFactor = Math.pow(10, decimals);
        return Math.round(value * roundFactor) / roundFactor;
    }
}
