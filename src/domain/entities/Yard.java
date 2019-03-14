package domain.entities;

import helpers.CenteredRectangle;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Yard {
    private Map<String, Bundle> bundles;
    private Lift lift;

    public Yard() {
        setBundles(new HashMap<>());
    }

    public Yard(Map<String, Bundle> bundles, Lift lift) {
        setBundles(bundles);
        setLift(lift);
    }

    public List<Bundle> getBundles() { return new ArrayList<>(bundles.values()); }

    public void setBundles(Map<String, Bundle> bundles) { this.bundles = bundles; }

    public Lift getLift() { return lift; }

    public void setLift(Lift lift) { this.lift = lift; }

    public void createBundle(Point2D position) {
        Bundle bundle = new Bundle(position);
        bundles.put(bundle.getId(), bundle);
    }

    public Bundle getBundle(long id) {
        return bundles.get(id);
    }

    public List<Bundle> getBundlesAtPosition(Point2D position) {
        List<Bundle> selectedBundles = new ArrayList<>();
        for (Bundle bundle : new ArrayList<>(bundles.values())) {
            CenteredRectangle rec = new CenteredRectangle(
                    bundle.position,
                    bundle.width,
                    bundle.length,
                    bundle.angle
            );
            rec.getPoints();
        }

        return selectedBundles;
    }
}
