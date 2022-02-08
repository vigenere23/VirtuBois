package virtubois.domain.entities;

import virtubois.helpers.Point2D;

import java.io.Serializable;

public abstract class Drawable implements Serializable
{
    private static final long serialVersionUID = 8150050354543139242L;
    protected Point2D position;
    protected double angle;
    protected double width;
    protected double length;

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

    private void calculateBoundaries() {
    }
}
