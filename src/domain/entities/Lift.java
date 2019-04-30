package domain.entities;

import helpers.ConfigHelper;
import helpers.GeomHelper;
import helpers.MathHelper;
import helpers.Point2D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Lift extends Drawable3D implements Serializable {

    private static final long serialVersionUID = 15641321L;
    private double armsHeight;
    private double armsWidth;
    private double armsLength;
    private Point2D armsPosition;
    private double scale;
    private List<Bundle> bundlesOnLift;

    public Lift(Point2D position) {
        super(position);
        bundlesOnLift = new ArrayList<>();
        setWidth(ConfigHelper.liftWidth);
        setLength(ConfigHelper.liftLength);
        setHeight(ConfigHelper.liftHeight);
        setArmsHeight(ConfigHelper.armsHeight);
        setArmsWidth(ConfigHelper.armsWidth);
        setArmsLength(ConfigHelper.armsLength);
        setAngle(ConfigHelper.liftAngle);
        this.scale = ConfigHelper.liftScale;
        repositionArms();
    }

    public Point2D getArmsPosition() {
        return armsPosition;
    }

    public double getArmsHeight() {
        return armsHeight;
    }

    public void setArmsHeight(double armsHeight) {
        double difference =  armsHeight - this.armsHeight;
        this.armsHeight = armsHeight;
        if(!bundlesOnLift.isEmpty()){
            for(Bundle bundle : bundlesOnLift){
                bundle.setZ(bundle.getZ() + difference);
            }
        }
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
    public double getScale(){
        return scale;
    }

    public void setScale(double scale){
        this.scale = scale;
        setWidth(ConfigHelper.liftWidth * scale);
        setLength(ConfigHelper.liftLength * scale);
        setHeight(ConfigHelper.liftHeight * scale);
        setArmsWidth(ConfigHelper.armsWidth * scale);
        setArmsLength(ConfigHelper.armsLength * scale);
        repositionArms();
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
        setAngle(angle - ConfigHelper.liftAngleIncrement);
        repositionArms();
    }

    public void turnLeft() {
        setAngle(angle + ConfigHelper.liftAngleIncrement);
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
        Point2D increment = new Point2D(ConfigHelper.liftPositionIncrement * getScale());
        Point2D rotatedIncrement = GeomHelper.getRotatedVector(increment, angle);
        if (moveForward) setPosition(position.add(rotatedIncrement));
        else setPosition(position.substract(rotatedIncrement));
    }

    public void repositionArms() {
        Point2D halfLiftVector = new Point2D(width / 2.5,length / 2.0);
        Point2D distanceVector = new Point2D(armsWidth / 2.0).add(halfLiftVector);
        Point2D rotatedDistanceVector = GeomHelper.getRotatedVector(distanceVector, angle);
        armsPosition = position.add(rotatedDistanceVector);
    }

    public void setBundlesOnLift(List<Bundle> bundlesOnLift) {
        if(!bundlesOnLift.isEmpty()) {
            this.bundlesOnLift = bundlesOnLift;
        }
    }

    public List<Bundle> getBundlesOnLift(){
        return bundlesOnLift;
    }

    public void clearBundles(){
        bundlesOnLift.clear();
    }
}
