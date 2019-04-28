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

    // LIFT
    public static final double liftWidth = 1.5;
    public static final double liftLenth = 2.0;
    public static double liftAngle = 90.0;
    public static final double armsWidth = 1.0;
    public static final double armsLength = 1.25;
    public static double armsHeight = 0.0;
    public static final double liftAngleIncrement = 5.0;
    public static final double liftPositionIncrement = 0.2;
    public static final double liftHeight = 5.0;
    public static final double armsHeightIncrement = MathHelper.round(0.2, 1);

    // DRAWING
    public static final double defaultZoom = 50.0;
    public static final double zoomFactor = 1.25;
    public static final double bundleOpacity = 0.3;
    public static final double bundleSaturation = 1;
    public static final double bundleBrightness = 0.6;
    public static final double bundleBorderWidth = 2;
}
