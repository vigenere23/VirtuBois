package helpers;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GeomHelper {

    public static boolean pointIsInsideRectangle(Point2D point, CenteredRectangle rectangle) {
        List<Point2D> points = rectangle.getPoints();

        Point2D a = new Point2D(-1, -1);
        Point2D b = new Point2D(2, -1);
        Point2D c = new Point2D(-1, -1);

        double totalArea = 0;
        for (int i = 0; i < points.size(); i++) {
            totalArea += getTriangleArea(points.get(i), points.get((i+1) % points.size()), point);
        }
        return MathHelper.round(totalArea - rectangle.area(), 2) == 0;
    }

    public static boolean rectangleCollidesRectangle(CenteredRectangle rectangle1, CenteredRectangle rectangle2) {
        for (Point2D point : rectangle1.getPoints()) {
            if (pointIsInsideRectangle(point, rectangle2)) return true;
        }
        return false;
    }

    public static Point2D invertY(Point2D point) {
        return new Point2D(point.getX(), -point.getY());
    }

    // https://en.wikipedia.org/wiki/Shoelace_formula
    public static double getTriangleArea(Point2D point1, Point2D point2, Point2D point3) {
        return 0.5 * Math.abs(
                point1.getX() * point2.getY()
                + point2.getX() * point3.getY()
                + point3.getX() * point1.getY()
                - point2.getX() * point1.getY()
                - point3.getX() * point2.getY()
                - point1.getX() * point3.getY()
        );
    }

}
