package virtubois.helpers;

import virtubois.enums.Comparison;

public class MathHelper {
    private static final double DELTA = 0.00001;

    public static double round(double value, int decimals) {
        double roundFactor = Math.pow(10, decimals);
        return Math.round(value * roundFactor) / roundFactor;
    }

    public static Comparison compareDoubles(double d1, double d2) {
        double diff = d1 - d2;
        if (Math.abs(diff) < DELTA) return Comparison.EQUAL;
        else if (diff < 0) return Comparison.SMALLER;
        else return Comparison.GREATER;
    }
}
