package domain.entities;

import javafx.geometry.Point2D;

public abstract class Drawable3D extends Drawable
{
    protected double z;

    public Drawable3D(Point2D position) {
        super(position);
        setZ(0.0); //defaultValue
    }

    public double getZ() { return z; }

    public void setZ(double z) { this.z = z; }
}
