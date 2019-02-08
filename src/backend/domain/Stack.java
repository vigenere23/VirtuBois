package backend.domain;

import javafx.geometry.Point3D;
import java.util.List;

public class Stack {

    private List<Point3D> vertices;
    private int index;

    public Stack() {}

    public Stack(List<Point3D> vertices, int index) {
        setVertices(vertices);
        setIndex(index);
    }

    public List<Point3D> getVertices() { return this.vertices; }

    public void setVertices(List<Point3D> vertices) { this.vertices = vertices; }

    public int getIndex() { return this.index; }

    public void setIndex(int index) { this.index = index; }
}
