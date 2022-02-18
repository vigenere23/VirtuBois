package glo2004.virtubois.helpers;

import glo2004.virtubois.domain.dtos.DrawableDto;
import glo2004.virtubois.domain.entities.Drawable;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CenteredRectangle {
    protected Rectangle rectangle;

    private double x;
    private double y;
    private double width;
    private double height;
    private double scale;

    public CenteredRectangle(Drawable drawable) {
        this(
            drawable.getPosition(),
            drawable.getWidth(),
            drawable.getLength(),
            drawable.getAngle()
        );
    }

    public CenteredRectangle(DrawableDto drawableDto) {
        this(
            drawableDto.position,
            drawableDto.width,
            drawableDto.length,
            drawableDto.angle
        );
    }

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

    public void setHeight(double height) {
        this.height = height;
        updateY();
    }

    public double area() {
        return width * height;
    }

    // https://stackoverflow.com/questions/2259476/rotating-a-point-about-another-point-2d
    public List<Point2D> get2DPoints() {
        // 0 --- 1
        // |     |
        // 3 --- 2
        double cosAngle = Math.cos((rectangle.getRotate() * 2.0 * Math.PI) / 360.0);
        double sinAngle = Math.sin((rectangle.getRotate() * 2.0 * Math.PI) / 360.0);

        return Stream.of(
            new Point2D(x - width / 2.0, y + height / 2.0),
            new Point2D(x + width / 2.0, y + height / 2.0),
            new Point2D(x + width / 2.0, y - height / 2.0),
            new Point2D(x - width / 2.0, y - height / 2.0)
        ).map(point -> {
            Point2D centeredPoint = point.substract(getPosition());
            Point2D rotatedCenteredPoint = new Point2D(
                centeredPoint.getX() * cosAngle - centeredPoint.getY() * sinAngle,
                centeredPoint.getX() * sinAngle + centeredPoint.getY() * cosAngle
            );
            return rotatedCenteredPoint.add(getPosition());
        }).collect(Collectors.toList());
    }
}
