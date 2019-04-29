package domain.dtos;

import domain.entities.Lift;
import helpers.Point2D;

public class LiftDto extends Drawable3DDto {
    public double armsWidth;
    public double armsLength;
    public double armsHeight;
    public Point2D armsPosition;
    public double scale;

    public LiftDto(Lift lift) {
        super(lift);
        armsWidth = lift.getArmsWidth();
        armsLength = lift.getArmsLength();
        armsHeight = lift.getArmsHeight();
        armsPosition = lift.getArmsPosition();
        scale = lift.getScale();
    }
}
