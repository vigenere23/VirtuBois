package glo2004.virtubois.helpers.export;

import javafx.geometry.Point3D;

import java.util.List;

public interface TriangleGenerator {
    List<Triangle> triangulate(List<Point3D> points);
}
