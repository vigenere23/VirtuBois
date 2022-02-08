package virtubois.domain.dtos;

import virtubois.domain.entities.Drawable;
import virtubois.helpers.Point2D;

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
