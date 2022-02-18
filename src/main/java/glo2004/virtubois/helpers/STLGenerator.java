package glo2004.virtubois.helpers;

import glo2004.virtubois.domain.dtos.BundleDto;
import glo2004.virtubois.helpers.export.Triangle;
import glo2004.virtubois.helpers.export.TriangleGenerator;
import glo2004.virtubois.presentation.presenters.BundlePresenter;
import javafx.geometry.Point3D;

import java.util.List;

public class STLGenerator {

    private final TriangleGenerator triangleGenerator;

    public STLGenerator(TriangleGenerator triangleGenerator) {
        this.triangleGenerator = triangleGenerator;
    }

    public String generateSTL(List<BundleDto> bundleDtos) {
        List<BundlePresenter> bundles = Converter.fromBundleDtosToBundlePresenters(bundleDtos);
        StringBuilder sb = new StringBuilder();
        sb.append("solid stl\n");
        for (BundlePresenter bundle : bundles) {
            List<Point3D> bundlePoints3D = bundle.get3DPoints();
            List<Triangle> triangles = triangleGenerator.triangulate(bundlePoints3D);

            for (Triangle triangle : triangles) {
                Point3D normal = triangle.first.subtract(triangle.third).crossProduct(triangle.second.subtract(triangle.third)).normalize();
                sb.append("  facet normal ").append(normal.getX()).append(" ").append(normal.getY()).append(" ").append(normal.getZ()).append("\n");
                sb.append("    outer loop\n");
                sb.append("      vertex ").append(triangle.first.getX()).append(" ").append(triangle.first.getY()).append(" ").append(triangle.first.getZ()).append("\n");
                sb.append("      vertex ").append(triangle.second.getX()).append(" ").append(triangle.second.getY()).append(" ").append(triangle.second.getZ()).append("\n");
                sb.append("      vertex ").append(triangle.third.getX()).append(" ").append(triangle.third.getY()).append(" ").append(triangle.third.getZ()).append("\n");
                sb.append("    endloop\n");
                sb.append("  endfacet\n");
            }
        }
        sb.append("endsolid stl");
        return sb.toString();
    }
}
