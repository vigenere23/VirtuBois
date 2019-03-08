package domain.dtos;

import domain.entities.Bundle;
import javafx.geometry.Point2D;
import java.time.LocalDate;
import java.time.LocalTime;

public class BundleDto {
    public double height;
    public double width;
    public double length;
    public double angle;
    public Point2D position;
    public LocalDate date;
    public LocalTime time;
    public String essence;
    public String plankSize;
    public int barcode;

    public BundleDto(Bundle bundle) {
        height = bundle.getHeight();
        width = bundle.getWidth();
        length = bundle.getLength();
        angle = bundle.getAngle();
        position = bundle.getPosition();
        date = bundle.getDate();
        time = bundle.getTime();
        essence = bundle.getEssence();
        plankSize = bundle.getPlankSize();
        barcode = bundle.getBarcode();
    }
}
