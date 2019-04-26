package domain.dtos;

import domain.entities.Lift;
import helpers.Point2D;
import javafx.scene.shape.Rectangle;

public class LiftDto {
    public double width;
    public double length;
    public double angle;
    public double height;
    public Point2D position;
    public Rectangle boundaries;
    public double armsWidth;
    public double armsLength;
    public double armsHeight;
    public Point2D armsPosition;

    public LiftDto(Lift lift) {
        width = lift.getWidth();
        length = lift.getLength();
        height = lift.getliftHeight();
        angle = lift.getAngle();
        position = lift.getPosition();
        boundaries = lift.getBoundaries();
        armsWidth = lift.getArmsWidth();
        armsLength = lift.getArmsLength();
        armsHeight = lift.getArmsHeight();
        armsPosition = lift.getArmsPosition();
    }
}
