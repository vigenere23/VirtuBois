package helpers;
import java.io.Serializable;

public class Point2D implements Serializable {
    private double x;
    private double y;

    public Point2D() {
        x = 0;
        y = 0;
    }

    public Point2D(double x, double y) {
        setX(x);
        setY(y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Point2D add(Point2D point) {
        return new Point2D(x + point.getX(), y + point.getY());
    }

    public Point2D subtract(Point2D point){
        return new Point2D(x - point.getX(), y - point.getY());

    }

    public Point2D multiply(double factor){
        return new Point2D(x * factor, y * factor);
    }
}
