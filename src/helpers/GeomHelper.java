package helpers;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GeomHelper {

    public static boolean pointIsInsideRectangle(Point2D point, Rectangle2D rectangle) {
        return rectangle.contains(point);
    }

    public static boolean rectangleIntersectsRectangle(Rectangle2D rectangle1, Rectangle2D rectangle2) {
        return rectangle1.intersects(rectangle2);
    }

    public static boolean rectangleIsInsideRectangle(Rectangle2D rectangle1, Rectangle2D rectangle2) {
        return rectangle1.contains(rectangle2);
    }

    public static boolean rectangleCollidesRectangle(Rectangle2D rectangle1, Rectangle2D rectangle2) {
        return rectangleIntersectsRectangle(rectangle1, rectangle2) ||
                rectangleIsInsideRectangle(rectangle1, rectangle2);
    }

    public static Point2D invertY(Point2D point) {
        return new Point2D(point.getX(), -point.getY());
    }
}
