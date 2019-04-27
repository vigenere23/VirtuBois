package domain.entities;

import helpers.ConfigHelper;
import helpers.GeomHelper;
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
        setHeight(ConfigHelper.liftHeight);
        setArmsHeight(ConfigHelper.armsHeight);
        setArmsWidth(ConfigHelper.armsWidth);
        setArmsLength(ConfigHelper.armsLength);
        setAngle(ConfigHelper.liftAngle);
        repositionArms();
    }

    public double getliftHeight() { return super.height;}

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

    public void turnRight() {
        angle += ConfigHelper.liftAngleIncrement;
        repositionArms();
    }

    public void turnLeft() {
        angle -= ConfigHelper.liftAngleIncrement;
        repositionArms();
    }

    public void moveForward() {
        move(true);
    }

    public void moveBackward() {
        move(false);
    }

    public void riseArms(){armsHeight += ConfigHelper.armsHeightIncrement;}

    public void lowerArms() {armsHeight -= ConfigHelper.armsHeightIncrement;}

    private void move(boolean moveForward) {
        Point2D increment = new Point2D(ConfigHelper.liftPositionIncrement);
        Point2D rotatedIncrement = GeomHelper.getRotatedVector(increment, -angle + 90);
        if (moveForward) setPosition(position.add(rotatedIncrement));
        else setPosition(position.substract(rotatedIncrement));
        repositionArms();
    }

    private void repositionArms() {
        Point2D halfLiftVector = new Point2D(length / 2, width /2);
        Point2D distanceVector = new Point2D(armsLength / 2).add(halfLiftVector);
        Point2D rotatedDistanceVector = GeomHelper.getRotatedVector(distanceVector, -angle + 90);
        armsPosition = position.add(rotatedDistanceVector);
    }
}
