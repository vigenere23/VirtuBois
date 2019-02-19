package domain.entities;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public abstract class Drawable
{
    protected Point2D position;
    protected double angle;
    protected double width;
    protected double length;
    protected Rectangle boundaries;

    public Drawable(Point2D position) {
        setWidth(20.0); //defaultValue
        setLength(20.0); //defaultValue
        setAngle(0.0); //defaultValue
        setPosition(position);
    }

    public double getLength()
    {
        return length;
    }

    public void setLength(double length)
    {
        this.length = length;
        calculateBoudaries();
    }

    public double getWidth()
    {
        return width;
    }

    public void setWidth(double width)
    {
        this.width = width;
        calculateBoudaries();
    }

    public Point2D getPosition() { return position; }

    public void setPosition(Point2D position) {
        this.position = position;
        calculateBoudaries();
    }

    public double getAngle() { return angle; }

    public void setAngle(double angle) { this.angle = angle; }

    public Rectangle getBoundaries() { return boundaries; }

    private void calculateBoudaries() {
        // TODO
    }
}
