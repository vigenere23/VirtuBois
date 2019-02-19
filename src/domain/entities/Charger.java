package domain.entities;

import javafx.geometry.Point2D;

public class Charger extends Drawable
{
    private double armsHeight;
    private double armsWidth;
    private double armsLength;

    public Charger(Point2D position) {
        super(position);
        setArmsHeight(0.0); //defaultValue
        setArmsWidth(20); //defaultValue
        setArmsLength(20); //defaultValue
    }

    public double getArmsHeight() {
        return armsHeight;
    }

    public void setArmsHeight(double armsHeight) {
        this.armsHeight = armsHeight;
    }

    public double getArmsWidth() {
        return armsWidth;
    }

    public void setArmsWidth(double armsWidth) {
        this.armsWidth = armsWidth;
    }

    public double getArmsLength() {
        return armsLength;
    }

    public void setArmsLength(double armsLenght) {
        this.armsLength = armsLenght;
    }
}
