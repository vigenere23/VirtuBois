package domain.entities;

import helpers.Point2D;

import java.io.Serializable;

public abstract class Drawable3D extends Drawable implements Serializable
{
    private static final long serialVersionUID = 4468277169307936906L;
    protected double z;
    protected double height;

    public Drawable3D(Point2D position) {
        super(position);
        setZ(0.0);
        setHeight(0.0);
    }

    public Drawable3D() {
        super(new Point2D(0, 0));
        setZ(0.0);
        setHeight(0.0);
    }

    public double getZ() { return z; }

    public void setZ(double z) { this.z = z; }

    public double getHeight() { return height; }

    public void setHeight(double height) { this.height = height; }

    public double getTopZ() { return z + height; }
}
