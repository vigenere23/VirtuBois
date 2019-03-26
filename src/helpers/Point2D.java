package helpers;
import java.io.Serializable;

public class Point2D implements Serializable {
    private double x;
    private double y;

    public Point2D() {
        x = 0;
        y = 0;
    }

    public Point2D(double xpos, double ypos){
        x = xpos;
        y =ypos;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Point2D add(Point2D pointadd) {
        return new Point2D(x + pointadd.getX(), y + pointadd.getY());
    }

    public Point2D subtract(Point2D pointsubstract){
        return new Point2D(x - pointsubstract.getX(), y - pointsubstract.getY());

    }

    public Point2D multiply(double factor){
        return new Point2D(x * factor, y * factor);
    }
}
