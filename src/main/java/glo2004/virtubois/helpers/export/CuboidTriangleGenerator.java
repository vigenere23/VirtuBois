package glo2004.virtubois.helpers.export;

import javafx.geometry.Point3D;

import java.util.List;
import java.util.stream.Collectors;

public class CuboidTriangleGenerator implements TriangleGenerator {

    private final SquareTriangleGenerator squareTriangleGenerator;

    public CuboidTriangleGenerator(SquareTriangleGenerator squareTriangleGenerator) {
        this.squareTriangleGenerator = squareTriangleGenerator;
    }

    /**
     * Generated triangles from a cuboid 3D points.
     *
     * @param points List of 3D points. MUST follow this order :
     * <pre>{@code
     *   5----6  BACK
     *   |\   |\
     *   1-\--2 \
     *    \ \  \ \
     *     \ 4----7
     *      \|   \|
     *       0----3  FRONT
     * }</pre>
     * @return List of generated triangles (clockwise when facing)
     */
    @Override
    public List<Triangle> triangulate(List<Point3D> points) {
        if (points.size() != 8) {
            throw new IllegalArgumentException("Must have exactly 8 points.");
        }

        List<List<Point3D>> sides = List.of(
            List.of(points.get(0), points.get(4), points.get(7), points.get(3)), // front
            List.of(points.get(5), points.get(6), points.get(2), points.get(1)), // back
            List.of(points.get(1), points.get(5), points.get(4), points.get(0)), // left
            List.of(points.get(3), points.get(7), points.get(6), points.get(2)), // right
            List.of(points.get(4), points.get(5), points.get(6), points.get(7)), // top
            List.of(points.get(3), points.get(2), points.get(1), points.get(0)) // bottom
        );


        return sides.stream()
            .map(squareTriangleGenerator::triangulate)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
}
