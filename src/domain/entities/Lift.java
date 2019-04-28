package domain.entities;

import helpers.ConfigHelper;
import helpers.GeomHelper;
import helpers.MathHelper;
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
        angle %= 360;
        if (angle < 0) angle += 360;
        super.setAngle(angle);
        ConfigHelper.liftAngle = this.angle;
        repositionArms();
    }

    public void turnRight() {
        setAngle(angle + ConfigHelper.liftAngleIncrement);
        repositionArms();
    }

    public void turnLeft() {
        setAngle(angle - ConfigHelper.liftAngleIncrement);
        repositionArms();
    }

    public void moveForward() {
        move(true);
    }

    public void moveBackward() {
        move(false);
    }

    public void riseArms() {
        armsHeight += ConfigHelper.armsHeightIncrement;
    }

    public void lowerArms() {
        armsHeight -= ConfigHelper.armsHeightIncrement;
        if (armsHeight < 0) armsHeight = 0;
    }

    @Override
    public void setPosition(Point2D position) {
        super.setPosition(new Point2D(MathHelper.round(position.getX(),2),MathHelper.round(position.getY(),2)));
        repositionArms();
    }

    private void move(boolean moveForward) {
        Point2D increment = new Point2D(ConfigHelper.liftPositionIncrement);
        Point2D rotatedIncrement = GeomHelper.getRotatedVector(increment, -angle + 90);
        if (moveForward) setPosition(position.add(rotatedIncrement));
        else setPosition(position.substract(rotatedIncrement));
    }

    public void repositionArms() {
        Point2D halfLiftVector = new Point2D(length / 2.45,width / 2);
        Point2D distanceVector = new Point2D(armsLength / 2).add(halfLiftVector);
        Point2D rotatedDistanceVector = GeomHelper.getRotatedVector(distanceVector, -angle + 90);
        armsPosition = position.add(rotatedDistanceVector);
    }
}
