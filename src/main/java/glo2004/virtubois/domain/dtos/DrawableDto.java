package glo2004.virtubois.domain.dtos;

import glo2004.virtubois.domain.entities.Drawable;
import glo2004.virtubois.helpers.Point2D;

public abstract class DrawableDto {
    public double width;
    public double length;
    public double angle;
    public Point2D position;

    public DrawableDto(Drawable drawable) {
        width = drawable.getWidth();
        length = drawable.getLength();
        angle = drawable.getAngle();
        position = drawable.getPosition();
    }
}
