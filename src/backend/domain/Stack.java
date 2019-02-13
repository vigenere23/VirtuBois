package backend.domain;

import javafx.geometry.Point3D;
import java.util.List;

public class Stack {

    private List<Point3D> vertices;
    private List<Pack> packs;
    private int index;

    public Stack() {}

    public Stack(List<Point3D> vertices, int index, List<Pack> packs) {
        setPacks(packs);
        setVertices(vertices);
        setIndex(index);
    }

    public List<Point3D> getVertices() { return vertices; }

    public void setVertices(List<Point3D> vertices) { this.vertices = vertices; }

    public int getIndex() { return index; }

    public void setIndex(int index) { this.index = index; }

    public List<Pack> getPacks() { return packs; }

    public void setPacks(List<Pack> packs) { this.packs = packs; }

    public void removePack(Pack pack) {
        this.packs.remove(pack);
    }

    public void addPack(Pack pack) {
        this.packs.add(pack);
    }
}
