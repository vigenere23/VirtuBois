package domain.entities;

import helpers.Point2D;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public abstract class Drawable implements Serializable
{
    protected Point2D position;
    protected double angle;
    protected double width;
    protected double length;
    protected Rectangle boundaries;

    public Drawable(Point2D position) {
        setPosition(position);
    }

    public Drawable() {}

    public double getLength()
    {
        return length;
    }

    public void setLength(double length)
    {
        this.length = length;
        calculateBoundaries();
    }

    public double getWidth()
    {
        return width;
    }

    public void setWidth(double width)
    {
        this.width = width;
        calculateBoundaries();
    }

    public Point2D getPosition() { return position; }

    public void setPosition(Point2D position) {
        this.position = position;
        calculateBoundaries();
    }

    public double getAngle() { return angle; }

    public void setAngle(double angle) { this.angle = angle; }

    public Rectangle getBoundaries() { return boundaries; }

    private void calculateBoundaries() {
    }
}
