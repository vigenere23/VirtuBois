package domain.entities;

import domain.dtos.LiftDto;
import helpers.ConfigHelper;
import helpers.Point2D;

import java.io.Serializable;

public class Lift extends Drawable3D implements Serializable {

    private static final long serialVersionUID = 15641321L;
    private double armsHeight;
    private double armsWidth;
    private double armsLength;
    private Point2D armsPosition;

    public Lift(Point2D position) {
        super(position);
        setWidth(ConfigHelper.liftWidth);
        setLength(ConfigHelper.liftLenth);
        setArmsHeight(ConfigHelper.armsHeight);
        setArmsWidth(ConfigHelper.armsWidth);
        setArmsLength(ConfigHelper.armsLength);
        setAngle(ConfigHelper.liftAngle);
        setArmsPosition(position);
    }

    private void setArmsPosition(Point2D position) {
        this.armsPosition = new Point2D(position.getX(), position.getY() + length / 2);
    }

    public Point2D getArmsPosition() {
        return armsPosition;
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

    @Override
    public void setAngle(double angle) {
        super.setAngle(angle);
        ConfigHelper.liftAngle = this.angle;
    }

    public void setMovement(LiftDto newLift) {
        setPosition(newLift.position);
        setAngle(newLift.angle);
    }
}
