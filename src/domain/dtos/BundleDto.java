package domain.dtos;

import domain.entities.Bundle;
import javafx.geometry.Point2D;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class BundleDto implements Serializable {
    public String id;
    public String color;
    public double height;
    public double width;
    public double length;
    public double angle;
    transient public Point2D position;
    public double z;
    public LocalDate date;
    public LocalTime time;
    public String essence;
    public String plankSize;
    public String barcode;

    public BundleDto(Bundle bundle) {
        id = bundle.getId();
        color = bundle.getColor();
        height = bundle.getHeight();
        width = bundle.getWidth();
        length = bundle.getLength();
        angle = bundle.getAngle();
        position = bundle.getPosition();
        z = bundle.getZ();
        date = bundle.getDate();
        time = bundle.getTime();
        essence = bundle.getEssence();
        plankSize = bundle.getPlankSize();
        barcode = bundle.getBarcode();
    }
}
