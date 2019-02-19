package domain.entities;

import java.util.ArrayList;
import java.util.List;

public class Yard {
    private List<Pack> packs;
    private Charger charger;

    public Yard() {
        setCharger(new Charger());
        setPacks(new ArrayList<>());
    }

    public Yard(List<Pack> packs, Charger charger) {
        setPacks(packs);
        setCharger(charger);
    }

    public List<Pack> getPacks() { return packs; }

    public void setPacks(List<Pack> packs) { this.packs = packs; }

    public Charger getCharger() { return charger; }

    public void setCharger(Charger charger) { this.charger = charger; }
}
