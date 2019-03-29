package domain.dtos;

import domain.entities.Bundle;
import helpers.Point2D;

import java.time.LocalDate;
import java.time.LocalTime;

public class BundleDto {
    public String id;
    public String color;
    public double height;
    public double width;
    public double length;
    public double angle;
    public Point2D position;
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

    public boolean equals(BundleDto other) {
        return id.equals(other.id);
    }

    public String getBarcode() {
        return barcode;
    }

    public String getEssence() {
        return essence;
    }

    public String getPlankSize() {
        return plankSize;
    }

    public double getX() {return position.getX();}

    public double getY() {return position.getY();}

}
