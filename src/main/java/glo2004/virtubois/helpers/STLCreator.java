package glo2004.virtubois.helpers;

import glo2004.virtubois.domain.dtos.BundleDto;
import javafx.geometry.Point3D;
import glo2004.virtubois.presentation.presenters.BundlePresenter;

import java.util.ArrayList;
import java.util.List;

public class STLCreator {

    public String generateSTL(List<BundleDto> bundleDtos) {
        List<BundlePresenter> bundles = Converter.fromBundleDtosToBundlePresenters(bundleDtos);
        StringBuilder sb = new StringBuilder();
        sb.append("solid stl\n");
        for (BundlePresenter bundle : bundles) {
            List<Point3D> bundlePoints3D = generateBundlePoints3D(bundle);

            for (List<Point3D> triangle : generateBundleTriangles(bundlePoints3D)) {
                Point3D normal = triangle.get(0).subtract(triangle.get(2)).crossProduct(triangle.get(1).subtract(triangle.get(2))).normalize();
                sb.append("  facet normal ").append(normal.getX()).append(" ").append(normal.getY()).append(" ").append(normal.getZ()).append("\n");
                sb.append("    outer loop\n");
                sb.append("      vertex ").append(triangle.get(0).getX()).append(" ").append(triangle.get(0).getY()).append(" ").append(triangle.get(0).getZ()).append("\n");
                sb.append("      vertex ").append(triangle.get(1).getX()).append(" ").append(triangle.get(1).getY()).append(" ").append(triangle.get(1).getZ()).append("\n");
                sb.append("      vertex ").append(triangle.get(2).getX()).append(" ").append(triangle.get(2).getY()).append(" ").append(triangle.get(2).getZ()).append("\n");
                sb.append("    endloop\n");
                sb.append("  endfacet\n");
            }
        }
        sb.append("endsolid stl");
        return sb.toString();
    }

    private List<Point3D> generateBundlePoints3D(BundlePresenter bundle) {
        List<Point3D> bundlePoints3D = new ArrayList<>();

        bundle.getPoints().forEach(point2D -> {
            bundlePoints3D.add(new Point3D(point2D.getX(), point2D.getY(), bundle.dto.z));
        });
        bundle.getPoints().forEach(point2D -> {
            bundlePoints3D.add(new Point3D(point2D.getX(), point2D.getY(), bundle.dto.topZ));
        });

        return bundlePoints3D;
    }

    private List<Point3D> generateTriangle(Point3D p1, Point3D p2, Point3D p3) {
        return List.of(p1, p2, p3);
    }

    private List<List<Point3D>> generateBundleTriangles(List<Point3D> bundlePoints3D) {
        List<List<Point3D>> triangles = new ArrayList<>();
        triangles.add(generateTriangle(bundlePoints3D.get(0), bundlePoints3D.get(2), bundlePoints3D.get(3)));
        triangles.add(generateTriangle(bundlePoints3D.get(0), bundlePoints3D.get(1), bundlePoints3D.get(2)));
        triangles.add(generateTriangle(bundlePoints3D.get(4), bundlePoints3D.get(7), bundlePoints3D.get(6)));
        triangles.add(generateTriangle(bundlePoints3D.get(4), bundlePoints3D.get(5), bundlePoints3D.get(6)));
        triangles.add(generateTriangle(bundlePoints3D.get(2), bundlePoints3D.get(3), bundlePoints3D.get(7)));
        triangles.add(generateTriangle(bundlePoints3D.get(2), bundlePoints3D.get(6), bundlePoints3D.get(7)));
        triangles.add(generateTriangle(bundlePoints3D.get(1), bundlePoints3D.get(4), bundlePoints3D.get(5)));
        triangles.add(generateTriangle(bundlePoints3D.get(0), bundlePoints3D.get(1), bundlePoints3D.get(4)));
        triangles.add(generateTriangle(bundlePoints3D.get(3), bundlePoints3D.get(4), bundlePoints3D.get(7)));
        triangles.add(generateTriangle(bundlePoints3D.get(0), bundlePoints3D.get(3), bundlePoints3D.get(4)));
        triangles.add(generateTriangle(bundlePoints3D.get(2), bundlePoints3D.get(6), bundlePoints3D.get(5)));
        triangles.add(generateTriangle(bundlePoints3D.get(1), bundlePoints3D.get(2), bundlePoints3D.get(5)));
        return triangles;
    }
}
