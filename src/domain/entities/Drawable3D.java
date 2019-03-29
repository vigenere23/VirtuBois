package domain.entities;

import helpers.Point2D;

import java.io.Serializable;

public abstract class Drawable3D extends Drawable implements Serializable
{
    private static final long serialVersionUID = 4468277169307936906L;
    protected double z;

    public Drawable3D(Point2D position) {
        super(position);
        setZ(0.0); //defaultValue
    }

    public Drawable3D() {}

    public double getZ() { return z; }

    public void setZ(double z) { this.z = z; }
}
