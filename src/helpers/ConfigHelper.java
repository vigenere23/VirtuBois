package helpers;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConfigHelper {

    //GRID
    public static int gridSquareSize = 1;

    // BUNDLE
    public static double bundleWidth = 2.0;
    public static double bundleLength = 1.0;
    public static double bundleHeight = 0.5;
    public static double bundleAngle = 0.0;
    public static String bundleEssence = "Érable";
    public static String bundlePlankSize = "3x4";
    public static LocalDate bundleDate = LocalDate.now();
    public static LocalTime bundleTime = LocalTime.now();
    public static String bundleBarcode = "123456789abcdef";

    // CHARGER
    public static final double chargerWidth = 1.5;
    public static final double chargerLenth = 2.0;
    public static final double chargerAngle = 0.0;
    public static final double armsWidth = 1.0;
    public static final double armsLength = 1.0;
    public static final double armsHeight = 0.0;

    // DRAWING
    public static final double defaultZoom = 50.0;
    public static final double zoomFactor = 1.25;
    public static final double bundleOpacity = 0.3;
    public static final double bundleSaturation = 1;
    public static final double bundleBrightness = 0.6;
    public static final double bundleBorderWidth = 2;
}
