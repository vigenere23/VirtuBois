package domain.dtos;

import domain.entities.Charger;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class ChargerDto {
    public double width;
    public double length;
    public double angle;
    public Point2D position;
    public Rectangle boundaries;
    public double armsWidth;
    public double armsLength;
    public double armsHeight;

    public ChargerDto(Charger charger) {
        width = charger.getWidth();
        length = charger.getLength();
        angle = charger.getAngle();
        position = charger.getPosition();
        boundaries = charger.getBoundaries();
        armsWidth = charger.getArmsWidth();
        armsLength = charger.getArmsLength();
        armsHeight = charger.getArmsHeight();
    }
}
