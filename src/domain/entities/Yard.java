package domain.entities;

import java.util.ArrayList;
import java.util.List;

public class Yard {
    private List<Pack> packs;
    private Lift lift;

    public Yard() {
        setPacks(new ArrayList<>());
    }

    public Yard(List<Pack> packs, Lift lift) {
        setPacks(packs);
        setLift(lift);
    }

    public List<Pack> getPacks() { return packs; }

    public void setPacks(List<Pack> packs) { this.packs = packs; }

    public Lift getLift() { return lift; }

    public void setLift(Lift lift) { this.lift = lift; }
}
