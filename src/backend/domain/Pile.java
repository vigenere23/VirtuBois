package backend.domain;

import javafx.geometry.Point3D;

public class Pile {

    private Point3D _sommet;
    private int _index;

    public Pile()
    {

    }

    public Pile(Point3D sommet, int index)
    {
        _sommet = sommet;
        _index = index;
    }

    public Point3D getSommet()
    {
        return _sommet;
    }

    public void setSommet(Point3D sommet)
    {
        _sommet = sommet;
    }

    public int getIndex()
    {
        return _index;
    }

    public void setIndex(int index)
    {
        _index = index;
    }
}
