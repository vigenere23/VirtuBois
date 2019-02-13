package backend.domain;

import javafx.geometry.Point2D;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Pack
{
    private double height;
    private double width;
    private double length;
    private Point2D position;
    private Point2D z;
    private LocalDate date;
    private LocalTime time;
    private Pair<String, String> type;
    private int barcode;
    private Stack stack;
    private static final AtomicInteger count = new AtomicInteger(000000000000);

    public Pack(
            double height, double width, double length,
            LocalDate date, LocalTime time,
            Pair<String, String> type,
            Stack stack) {
        setStack(stack);
        setHeight(height);
        setWidth(width);
        setLength(length);
        setDate(date);
        setTime(time);
        setType(type);
        setBarcode();
    }

    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public double getLength()
    {
        return length;
    }

    public void setLength(double length)
    {
        this.length = length;
    }

    public double getWidth()
    {
        return width;
    }

    public void setWidth(double width)
    {
        this.width = width;
    }

    public Pair<String,String> getType()
    {
        return type;
    }

    public void setType(Pair<String,String> type)
    {
        this.type = type;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time) { this.time = time; }

    public LocalDate getDate() { return this.date; }

    public void setDate(LocalDate date) { this.date = date; }

    public int getBarcode() { return this.barcode; }

    public void setBarcode() {
        this.barcode = count.incrementAndGet();
    }

    public Stack getStack() {
        return stack;
    }

    public void setStack(Stack stack) { this.stack = stack; }

    public void replaceStackToStackFromPack(Pack pack) {
        this.stack.removePack(this);
        this.stack = pack.stack;
        this.stack.addPack(this);
    }

    public void move(Point2D position) {
        this.position = position;
        /* TODO
        Pack pack = Inventory.getFirstPackAtPosition(position);
        if (pack != null) {
            replaceStackToStackFromPack(pack);
        }
        */
    }
}
