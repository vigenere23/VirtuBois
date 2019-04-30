package helpers;

import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class ColorHelper {

    public static Color lastColor;

    public static Color randomColor(double saturation, double brightness) {
        double hue = ThreadLocalRandom.current().nextDouble(0, 360);
        lastColor = Color.hsb(hue, saturation, brightness);
        return lastColor;
    }

    public static Color nextHueRandomColor(double saturation, double brightness) {
        if (lastColor == null) return randomColor(saturation, brightness);
        double hue = lastColor.getHue() + ThreadLocalRandom.current().nextDouble(30, 60);
        lastColor = Color.hsb(hue, saturation, brightness);
        return lastColor;
    }

    public static Color setOpacity(Color color, double opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    public static String toWeb(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16) / 255.0d,
                Integer.valueOf(colorStr.substring(3, 5), 16) / 255.0d,
                Integer.valueOf(colorStr.substring(5, 7), 16) / 255.0d,
                1.0);
    }
}
