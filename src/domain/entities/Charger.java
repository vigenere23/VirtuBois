package domain.entities;

import helpers.ConfigHelper;
import javafx.geometry.Point2D;

public class Charger extends Drawable
{
    private double armsHeight;
    private double armsWidth;
    private double armsLength;

    public Charger(Point2D position) {
        super(position);
        setWidth(ConfigHelper.chargerWidth);
        setLength(ConfigHelper.chargerLenth);
        setArmsHeight(ConfigHelper.armsHeight);
        setArmsWidth(ConfigHelper.armsWidth);
        setArmsLength(ConfigHelper.armsLength);
        setAngle(ConfigHelper.chargerAngle);
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
