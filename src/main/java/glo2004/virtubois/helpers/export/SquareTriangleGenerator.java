package glo2004.virtubois.helpers.export;

import javafx.geometry.Point3D;

import java.util.List;

public class SquareTriangleGenerator implements TriangleGenerator {

    /**
     * Generates triangles from a square 3D points.
     *
     * @param points List of 3D points. MUST be in clockwise order (when facing).
     * @return List of generated triangles.
     */
    @Override
    public List<Triangle> triangulate(List<Point3D> points) {
        // 0 --- 1
        // |     |
        // 3 --- 2

        return List.of(
            new Triangle(points.get(0), points.get(1), points.get(2)),
            new Triangle(points.get(0), points.get(2), points.get(3))
        );
    }
}
