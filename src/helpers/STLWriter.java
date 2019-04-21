package helpers;

import domain.dtos.BundleDto;
import javafx.geometry.Point3D;
import presentation.presenters.BundlePresenter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class STLWriter{
    private List<BundlePresenter> bundles = new ArrayList<>();
    private String filename;

    public STLWriter(List<BundleDto> bundlesDto, String filename) {
        this.filename = filename;
        createBundlePresenter(bundlesDto);
        writeInSTLFile();
    }

    private void createBundlePresenter(List<BundleDto> bundlesDto) {
        for (BundleDto bundle : bundlesDto) {
            bundles.add(new BundlePresenter(bundle));
        }
    }

    private List<Point3D> generateBundlePoints3D(BundlePresenter bundle) {
        List<Point2D> bundlePoints2D = bundle.getPoints();
        List<Point3D> bundlePoints3D = new ArrayList<>();
        bundlePoints3D.add(new Point3D(bundlePoints2D.get(0).getX(), bundlePoints2D.get(0).getY(), bundle.dto.z));
        bundlePoints3D.add(new Point3D(bundlePoints2D.get(1).getX(), bundlePoints2D.get(1).getY(), bundle.dto.z));
        bundlePoints3D.add(new Point3D(bundlePoints2D.get(2).getX(), bundlePoints2D.get(2).getY(), bundle.dto.z));
        bundlePoints3D.add(new Point3D(bundlePoints2D.get(3).getX(), bundlePoints2D.get(3).getY(), bundle.dto.z));
        bundlePoints3D.add(new Point3D(bundlePoints2D.get(0).getX(), bundlePoints2D.get(0).getY(), bundle.dto.z + bundle.dto.height));
        bundlePoints3D.add(new Point3D(bundlePoints2D.get(1).getX(), bundlePoints2D.get(1).getY(), bundle.dto.z + bundle.dto.height));
        bundlePoints3D.add(new Point3D(bundlePoints2D.get(2).getX(), bundlePoints2D.get(2).getY(), bundle.dto.z + bundle.dto.height));
        bundlePoints3D.add(new Point3D(bundlePoints2D.get(3).getX(), bundlePoints2D.get(3).getY(), bundle.dto.z + bundle.dto.height));
        return bundlePoints3D;
    }

    private List<Point3D> generateTriangle(Point3D p1, Point3D p2, Point3D p3) {
        List<Point3D> triangle = new ArrayList<>();
        triangle.add(p1);
        triangle.add(p2);
        triangle.add(p3);
        return triangle;
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

    private void writeInSTLFile() {
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
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename), Charset.forName("UTF-8"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(sb.toString());
        } catch (IOException e) {

        }
    }
}
