import Coordonnees;

public class Pile {

    private Coordonnees sommet;
    private int index;

    public Pile()
    {

    }

    public Pile(Coordonnees p_sommet, int p_index)
    {
        sommet = p_sommet;
        index = p_index;
    }

    public Coordonnees getSommet()
    {
        return sommet;
    }

    public void setSommet(Coordonnees sommet)
    {
        this.sommet = sommet;
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
