package backend.domain;

import java.util.List;

public class Stack {

    private List<Pack> packs;
    private int index;

    public Stack() {}

    public Stack(int index, List<Pack> packs) {
        setPacks(packs);
        setIndex(index);
    }

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
