package domain.entities;

import javafx.geometry.Point2D;

public class Charger extends Drawable
{
    private double armsHeight;
    private double armsWidth;
    private double armsLenght;

    public Charger(Point2D position) {
        super(position);
        setArmsHeight(0.0); //defaultValue
        setArmsWidth(20); //defaultValue
        setArmsLenght(20); //defaultValue
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

    public double getArmsLenght() {
        return armsLenght;
    }

    public void setArmsLenght(double armsLenght) {
        this.armsLenght = armsLenght;
    }
}
