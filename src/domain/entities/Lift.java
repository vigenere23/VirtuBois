package domain.entities;

import helpers.ConfigHelper;
import helpers.Point2D;
import java.io.Serializable;

public class Lift extends Drawable3D implements Serializable {
    
    private static final long serialVersionUID = 15641321L;
    private double dy = 0, dx = 0;
    private double armsHeight;
    private double armsWidth;
    private double armsLength;

    public Lift(Point2D position) {
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

    @Override
    public void setAngle(double angle) {
        super.setAngle(angle);
        ConfigHelper.chargerAngle = this.angle;
    }

    public Lift moveForward() {
       dx += Math.cos((this.angle * (Math.PI/180)) + Math.PI/2);
       dy += Math.sin((this.angle * (Math.PI/180))+ Math.PI/2);
       return new Lift(new Point2D(dx, dy));
    }

    public Lift moveBackward() {
        dx -= Math.cos((this.angle * (Math.PI/180)) + Math.PI/2);
        dy -= Math.sin((this.angle * (Math.PI/180))+ Math.PI/2);
        return new Lift(new Point2D(dx, dy));
    }

    public Lift turnLeft() {
        double angle = getAngle();
        angle += 2;
        setAngle(angle);
        return new Lift(new Point2D(dx, dy));
    }

    public Lift turnRight() {
        double angle = getAngle();
        angle -= 2;
        setAngle(angle);
        return new Lift(new Point2D(dx, dy));
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
