package domain.dtos;

import domain.entities.Pack;
import domain.entities.Stack;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import java.time.LocalDate;
import java.time.LocalTime;

public class PackDto {
    private double height;
    private double width;
    private double length;
    private Point2D position;
    private double z;
    private LocalDate date;
    private LocalTime time;
    private Pair<String, String> type;
    private int barcode;
    private Stack stack;

    public PackDto(Pack pack) {
        this.height = pack.getHeight();
        this.width = pack.getWidth();
        this.length = pack.getLength();
        this.position = pack.getPosition();
        this.z = pack.getZ();
        this.date = pack.getDate();
        this.time = pack.getTime();
        this.type = pack.getType();
        this.barcode = pack.getBarcode();
        // this.stack = new StackDTO(pack.stack); TODO really needed?
    }
}
