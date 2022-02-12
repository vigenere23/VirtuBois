package glo2004.virtubois.domain.dtos;

import glo2004.virtubois.domain.entities.Bundle;
import java.time.LocalDate;
import java.time.LocalTime;

public class BundleDto extends Drawable3DDto {
    public String id;
    public String color;
    public LocalDate date;
    public LocalTime time;
    public String essence;
    public String plankSize;
    public String barcode;

    public BundleDto(Bundle bundle) {
        super(bundle);
        id = bundle.getId();
        color = bundle.getColor();
        date = bundle.getDate();
        time = bundle.getTime();
        essence = bundle.getEssence();
        plankSize = bundle.getPlankSize();
        barcode = bundle.getBarcode();
    }

    public boolean equals(BundleDto other) {
        if (other == null) return false;
        return id.equals(other.id);
    }

    public String getBarcode() { return barcode; }

    public String getEssence() { return essence; }

    public String getPlankSize() { return plankSize; }

    public double getX() { return position.getX(); }

    public double getY() { return position.getY(); }

    public double getZ() { return z; }
}
