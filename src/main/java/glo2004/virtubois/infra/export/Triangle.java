package glo2004.virtubois.infra.export;

import javafx.geometry.Point3D;

public class Triangle {
    public final Point3D first;
    public final Point3D second;
    public final Point3D third;

    public Triangle(Point3D first, Point3D second, Point3D third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
