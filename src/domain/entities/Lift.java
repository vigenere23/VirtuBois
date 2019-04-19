package domain.entities;

import helpers.ConfigHelper;
import helpers.Point2D;

import java.awt.*;
import java.io.Serializable;

public class Lift extends Drawable3D implements Serializable {
    
    private static final long serialVersionUID = 15641321L;
    private double dy = 0, dx = 0;
    private double xArms = 0, yArms = 0;
    private double armsHeight;
    private double armsWidth;
    private double armsLength;
    private Point2D armsPosition;

    public Lift(Point2D position) {
        super(position);
        setWidth(ConfigHelper.chargerWidth);
        setLength(ConfigHelper.chargerLenth);
        setArmsHeight(ConfigHelper.armsHeight);
        setArmsWidth(ConfigHelper.armsWidth);
        setArmsLength(ConfigHelper.armsLength);
        setAngle(ConfigHelper.chargerAngle);
        setArmsPosition(position);
    }

    private void setArmsPosition(Point2D position) {
        this.armsPosition = new Point2D(position.getX(), position.getY() + length/2);
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
        ConfigHelper.chargerAngle = this.angle;
    }

    public Lift moveForward(Point2D position) {
       dx += Math.cos((this.angle * (Math.PI/180)) + Math.PI/2);
       dy += Math.sin((this.angle * (Math.PI/180))+ Math.PI/2);
       xArms += Math.cos(angle);
       yArms += Math.sin(angle) + length/2;
       setArmsPosition(new Point2D(xArms, yArms));
       return new Lift(new Point2D(position.getX() + dx, position.getY() + dy));
    }

    public Lift moveBackward(Point2D position) {
        dx -= Math.cos((this.angle * (Math.PI/180)) + Math.PI/2);
        dy -= Math.sin((this.angle * (Math.PI/180))+ Math.PI/2);
        return new Lift(new Point2D(dx + position.getX(), dy + position.getY()));
    }

    public Lift turnLeft(Point2D position) {
        double angle = getAngle();
        angle += 2;
        setAngle(angle);
        return new Lift(new Point2D(dx + position.getX(), dy + position.getY()));
    }

    public Lift turnRight(Point2D position) {
        double angle = getAngle();
        angle -= 2;
        setAngle(angle);
        return new Lift(new Point2D(dx + position.getX(), dy + position.getY()));
    }

    public Lift raiseArms() {
        double height = getArmsHeight();
        height += 1;
        setArmsHeight(height);
        return new Lift(new Point2D(dx, dy));

    }

    public Lift lowerArms() {
        double height = getArmsHeight();
        height -= 1;
        setArmsHeight(height);
        return new Lift(new Point2D(dx, dy));

    }
}
