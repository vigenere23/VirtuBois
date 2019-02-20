package domain.dtos;

import domain.entities.Pack;
import javafx.geometry.Point2D;
import java.time.LocalDate;
import java.time.LocalTime;

public class PackDto {
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

    public PackDto(Pack pack) {
        height = pack.getHeight();
        width = pack.getWidth();
        length = pack.getLength();
        angle = pack.getAngle();
        position = pack.getPosition();
        date = pack.getDate();
        time = pack.getTime();
        essence = pack.getEssence();
        plankSize = pack.getPlankSize();
        barcode = pack.getBarcode();
    }
}
