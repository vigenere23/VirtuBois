import Coordonnates;

public class Stack {

    private Coordonnates vertices;
    private int index;

    public Pile()
    {

    }

    public Pile(Coordonnates p_vertices, int p_index)
    {
        vertices = p_vertices;
        index = p_index;
    }

    public Coordonnates getVertices()
    {
        return vertices;
    }

    public void setVertices(Coordonnates vertices)
    {
        this.vertices = vertices;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }
}
