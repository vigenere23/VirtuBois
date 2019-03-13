package domain.entities;

import helpers.ConfigHelper;
import javafx.geometry.Point2D;

import java.time.LocalDate;
import java.time.LocalTime;

public class Bundle extends Drawable3D
{
    private long id;
    private double height;
    private LocalDate date;
    private LocalTime time;
    private String essence;
    private String plankSize;
    private String barcode;

    public Bundle(Point2D position) {
        super(position);
        initId();
        setWidth(ConfigHelper.bundleWidth);
        setLength(ConfigHelper.bundleLength);
        setHeight(ConfigHelper.bundleHeight);
        setAngle(ConfigHelper.bundleAngle);
        setEssence(ConfigHelper.bundleEssence);
        setPlanckSize(ConfigHelper.bundlePlankSize);
        setDate(ConfigHelper.bundleDate);
        setTime(ConfigHelper.bundleTime);
        setBarcode(ConfigHelper.bundleBarcode);
        System.out.println("New bundle created");
    }

    private void initId() {
        // TODO get unique id
    }

    public long getId() { return id; }

    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public String getEssence()
    {
        return essence;
    }

    public void setEssence(String essence)
    {
        this.essence = essence;
        ConfigHelper.bundleEssence = this.essence;
    }

    public String getPlankSize() { return plankSize; }

    public void setPlanckSize(String plankSize) {
        this.plankSize = plankSize;
        ConfigHelper.bundlePlankSize = this.plankSize;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
        ConfigHelper.bundleTime = this.time;
    }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) {
        this.date = date;
        ConfigHelper.bundleDate = this.date;
    }

    public String getBarcode() { return barcode; }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
        ConfigHelper.bundleBarcode = this.barcode;
    }
}
