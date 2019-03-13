package domain.entities;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Yard {
    private Map<Long, Bundle> bundles;
    private Lift lift;

    public Yard() {
        setBundles(new HashMap<>());
    }

    public Yard(Map<Long, Bundle> bundles, Lift lift) {
        setBundles(bundles);
        setLift(lift);
    }

    public List<Bundle> getBundles() { return new ArrayList<>(bundles.values()); }

    public void setBundles(Map<Long, Bundle> bundles) { this.bundles = bundles; }

    public Lift getLift() { return lift; }

    public void setLift(Lift lift) { this.lift = lift; }

    public void createBundle(Point2D position) {
        Bundle bundle = new Bundle(position);
        bundles.put(bundle.getId(), bundle);
    }

    public Bundle getBundle(long id) {
        return bundles.get(id);
    }
}
