package domain.entities;

import java.util.ArrayList;
import java.util.List;

public class Yard {
    private List<Bundle> bundles;
    private Lift lift;

    public Yard() {
        setBundles(new ArrayList<>());
    }

    public Yard(List<Bundle> bundles, Lift lift) {
        setBundles(bundles);
        setLift(lift);
    }

    public List<Bundle> getBundles() { return bundles; }

    public void setBundles(List<Bundle> bundles) { this.bundles = bundles; }

    public Lift getLift() { return lift; }

    public void setLift(Lift lift) { this.lift = lift; }
}
