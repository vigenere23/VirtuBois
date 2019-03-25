package helpers;

import javafx.geometry.Point2D;
import java.awt.geom.Line2D;
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
        List<Point2D> points1 = rectangle1.getPoints();
        List<Point2D> points2 = rectangle2.getPoints();

        // Check if any vertex of the smallest rectangle
        // is inside the biggest one
        if (rectangle1.area() < rectangle2.area()) {
            for (Point2D point : points1) {
                if (pointIsInsideRectangle(point, rectangle2)) return true;
            }
        } else {
            for (Point2D point : points2) {
                if (pointIsInsideRectangle(point, rectangle1)) return true;
            }
        }

        // Also check if any edges of rectangle1 intersects with any of rectangle2
        for (int i = 0; i < points1.size(); i++) {
            for (int j = 0; j < points2.size(); j++) {
                boolean linesIntersects = lineIntersectsOtherLine(
                        points1.get(i),
                        points1.get((i + 1) % points1.size()),
                        points2.get(j),
                        points2.get((j + 1) % points2.size())
                );
                if (linesIntersects) return true;
            }
        }
        return false;
    }

    public static boolean lineIntersectsOtherLine(Point2D line1start, Point2D line1end, Point2D line2start, Point2D line2end) {
        return Line2D.linesIntersect(
                line1start.getX(), line1start.getY(),
                line1end.getX(), line1end.getY(),
                line2start.getX(), line2start.getY(),
                line2end.getX(), line2end.getY()
        );
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
