package domain.dtos;

import domain.entities.Pack;
import javafx.geometry.Point2D;
import java.time.LocalDate;
import java.time.LocalTime;

public class PackDto {
    public double height;
    public double width;
    public double length;
    public Point2D position;
    public LocalDate date;
    public LocalTime time;
    public String essence;
    public String plankSize;
    public int barcode;

    public PackDto(Pack pack) {
        this.height = pack.getHeight();
        this.width = pack.getWidth();
        this.length = pack.getLength();
        this.position = pack.getPosition();
        this.date = pack.getDate();
        this.time = pack.getTime();
        this.essence = pack.getEssence();
        this.plankSize = pack.getPlankSize();
        this.barcode = pack.getBarcode();
    }
}
