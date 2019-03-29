package domain.entities;

import helpers.ColorHelper;
import helpers.ConfigHelper;
import helpers.Point2D;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Bundle extends Drawable3D implements Serializable
{
    private static final long serialVersionUID = 15641321L;
    private String id;
    private String color;
    private double height;
    private LocalDate date;
    private LocalTime time;
    private String essence;
    private String plankSize;
    private String barcode;

    public Bundle(Point2D position) {
        super(position);
        initId();
        initColor();
        setWidth(ConfigHelper.bundleWidth);
        setLength(ConfigHelper.bundleLength);
        setHeight(ConfigHelper.bundleHeight);
        setAngle(ConfigHelper.bundleAngle);
        setEssence(ConfigHelper.bundleEssence);
        setPlanckSize(ConfigHelper.bundlePlankSize);
        setDate(ConfigHelper.bundleDate);
        setTime(ConfigHelper.bundleTime);
        setBarcode(ConfigHelper.bundleBarcode);
    }

    public String getId() { return id; }

    private void initId() {
        id = UUID.randomUUID().toString();
    }

    public String getColor() { return color; }

    private void initColor() {
        Color color = ColorHelper.randomColor(ConfigHelper.bundleSaturation, ConfigHelper.bundleBrightness);
        this.color = ColorHelper.toWeb(color);
    }

    @Override
    public void setWidth(double width) {
        if (width > 0) {
            super.setWidth(width);
            ConfigHelper.bundleWidth = this.width;
        }
    }

    @Override
    public void setLength(double length) {
        if (length > 0) {
            super.setLength(length);
            ConfigHelper.bundleLength = this.length;
        }
    }

    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        if (height > 0) {
            this.height = height;
            ConfigHelper.bundleHeight = this.height;
        }
    }

    @Override
    public void setAngle(double angle) {
        super.setAngle(angle);
        ConfigHelper.bundleAngle = this.angle;
    }

    public String getEssence()
    {
        return essence;
    }

    public void setEssence(String essence)
    {
        if (!essence.isEmpty()) {
            this.essence = essence;
            ConfigHelper.bundleEssence = this.essence;
        }
    }

    public String getPlankSize() { return plankSize; }

    public void setPlanckSize(String plankSize) {
        if (plankSize.matches("^[1-9]+[0-9]*x[1-9]+[0-9]*$")) {
            this.plankSize = plankSize;
            ConfigHelper.bundlePlankSize = this.plankSize;
        }
    }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) {
        this.date = date;
        ConfigHelper.bundleDate = this.date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
        ConfigHelper.bundleTime = this.time;
    }

    public String getBarcode() { return barcode; }

    public void setBarcode(String barcode) {
        if (!barcode.isEmpty()) {
            this.barcode = barcode;
            ConfigHelper.bundleBarcode = this.barcode;
        }
    }
}
