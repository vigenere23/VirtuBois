package helpers;

import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class ColorHelper {
    public static Color randomColor() {
        double saturation = ThreadLocalRandom.current().nextDouble(0, 1);
        double brightness = ThreadLocalRandom.current().nextDouble(0, 1);
        return randomColor(saturation, brightness);
    }

    public static Color randomColor(double saturation, double brightness) {
        double hue = ThreadLocalRandom.current().nextDouble(0, 360);
        return Color.hsb(hue, saturation, brightness);
    }

    public static Color setOpacity(Color color, double opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }
}
