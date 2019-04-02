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
    List<BundlePresenter> bundles = new ArrayList<>();
    String filename;

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

    private void writeInSTLFile(){
        StringBuilder sb = new StringBuilder();
        sb.append("solid stl\n");
        for (BundlePresenter bundle : bundles) {
            List<Point2D> points = bundle.getPoints();
            List<List<Point3D>> triangles = new ArrayList<>(new ArrayList<>());
            Point3D point1 = new Point3D(points.get(0).getX(), points.get(0).getY(), bundle.dto.z);
            Point3D point2 = new Point3D(points.get(1).getX(), points.get(1).getY(), bundle.dto.z);
            Point3D point3 = new Point3D(points.get(2).getX(), points.get(2).getY(), bundle.dto.z);
            Point3D point4 = new Point3D(points.get(3).getX(), points.get(3).getY(), bundle.dto.z);
            Point3D point5 = new Point3D(points.get(0).getX(), points.get(0).getY(), bundle.dto.z + bundle.dto.height);
            Point3D point6 = new Point3D(points.get(1).getX(), points.get(1).getY(), bundle.dto.z + bundle.dto.height);
            Point3D point7 = new Point3D(points.get(2).getX(), points.get(2).getY(), bundle.dto.z + bundle.dto.height);
            Point3D point8 = new Point3D(points.get(3).getX(), points.get(3).getY(), bundle.dto.z + bundle.dto.height);

            ArrayList<Point3D> triangle1 = new ArrayList<>();
            triangle1.add(point1);
            triangle1.add(point3);
            triangle1.add(point4);
            triangles.add(triangle1);

            ArrayList<Point3D> triangle2 = new ArrayList<>();
            triangle2.add(point1);
            triangle2.add(point2);
            triangle2.add(point3);
            triangles.add(triangle2);

            ArrayList<Point3D> triangle3 = new ArrayList<>();
            triangle3.add(point5);
            triangle3.add(point8);
            triangle3.add(point7);
            triangles.add(triangle3);

            ArrayList<Point3D> triangle4 = new ArrayList<>();
            triangle4.add(point5);
            triangle4.add(point6);
            triangle4.add(point7);
            triangles.add(triangle4);

            ArrayList<Point3D> triangle5 = new ArrayList<>();
            triangle5.add(point3);
            triangle5.add(point4);
            triangle5.add(point8);
            triangles.add(triangle5);

            ArrayList<Point3D> triangle6 = new ArrayList<>();
            triangle6.add(point3);
            triangle6.add(point7);
            triangle6.add(point8);
            triangles.add(triangle6);

            ArrayList<Point3D> triangle7 = new ArrayList<>();
            triangle7.add(point2);
            triangle7.add(point5);
            triangle7.add(point6);
            triangles.add(triangle7);

            ArrayList<Point3D> triangle8 = new ArrayList<>();
            triangle8.add(point1);
            triangle8.add(point2);
            triangle8.add(point5);
            triangles.add(triangle8);

            ArrayList<Point3D> triangle9 = new ArrayList<>();
            triangle9.add(point4);
            triangle9.add(point5);
            triangle9.add(point8);
            triangles.add(triangle9);

            ArrayList<Point3D> triangle10 = new ArrayList<>();
            triangle10.add(point1);
            triangle10.add(point4);
            triangle10.add(point5);
            triangles.add(triangle10);

            ArrayList<Point3D> triangle11 = new ArrayList<>();
            triangle11.add(point3);
            triangle11.add(point7);
            triangle11.add(point6);
            triangles.add(triangle11);

            ArrayList<Point3D> triangle12 = new ArrayList<>();
            triangle12.add(point2);
            triangle12.add(point3);
            triangle12.add(point6);
            triangles.add(triangle12);

            for (List<Point3D> triangle : triangles) {
                Point3D normal =triangle.get(0).subtract(triangle.get(2)).crossProduct(triangle.get(1).subtract(triangle.get(2))).normalize();
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
