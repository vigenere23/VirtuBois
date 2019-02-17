package domain.entities;

import java.util.ArrayList;
import java.util.List;

public class Yard {
    private List<Stack> stacks;
    private List<Pack> packs;
    private Charger charger;

    public Yard() {
        setCharger(new Charger());
        setStacks(new ArrayList<>());
        setPacks(new ArrayList<>());
    }

    public Yard(List<Pack> packs, List<Stack> stacks, Charger charger) {
        setPacks(packs);
        setStacks(stacks);
        setCharger(charger);
    }

    public List<Pack> getPacks() { return packs; }

    public void setPacks(List<Pack> packs) { this.packs = packs; }

    public List<Stack> getStacks() { return stacks; }

    public void setStacks(List<Stack> stacks) { this.stacks = stacks; }

    public Charger getCharger() { return charger; }

    public void setCharger(Charger charger) { this.charger = charger; }
}
