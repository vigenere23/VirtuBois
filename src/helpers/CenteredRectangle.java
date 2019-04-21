package helpers;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class CenteredRectangle {
    protected Rectangle rectangle;

    private double x;
    private double y;
    private double width;
    private double height;
    private double scale;

    public CenteredRectangle(Point2D centerPos, double width, double height, double angle) {
        this(centerPos.getX(), centerPos.getY(), width, height, -angle);
    }

    public CenteredRectangle(double centerX, double centerY, double width, double height, double angle) {
        rectangle = new Rectangle();
        setWidth(width);
        setHeight(height);
        setX(centerX);
        setY(centerY);
        rectangle.setRotate(-angle);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Point2D getPosition() {
        return new Point2D(getX(), getY());
    }

    public void setPosition(Point2D position) {
        setX(position.getX());
        setY(position.getY());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        updateX();
    }

    private void updateX() {
        rectangle.setWidth(width * scale);
        rectangle.setX(x - width * scale / 2.0);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        updateY();
    }

    private void updateY() {
        rectangle.setHeight(height * scale);
        rectangle.setY(y - height * scale / 2.0);
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
        updateX();
        updateY();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
        updateX();
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
        updateY();
    }

    public double area() {
        return width * height;
    }

    // https://stackoverflow.com/questions/2259476/rotating-a-point-about-another-point-2d
    public List<Point2D> getPoints() {
        // 0 --- 1
        // |     |
        // 3 --- 2
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(x - width / 2.0, y + height / 2.0));
        points.add(new Point2D(x + width / 2.0, y + height / 2.0));
        points.add(new Point2D(x + width / 2.0, y - height / 2.0));
        points.add(new Point2D(x - width / 2.0, y - height / 2.0));

        double cosAngle = Math.cos((rectangle.getRotate() * 2.0 * Math.PI) / 360.0);
        double sinAngle = Math.sin((rectangle.getRotate() * 2.0 * Math.PI) / 360.0);

        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i).subtract(getPosition());
            Point2D transformedPoint = new Point2D(
                    point.getX() * cosAngle - point.getY() * sinAngle,
                    point.getX() * sinAngle + point.getY() * cosAngle
            );
            points.set(i, transformedPoint.add(getPosition()));
        }

        return points;

    }
}
