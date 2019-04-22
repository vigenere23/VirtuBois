package domain.entities;

import domain.dtos.BundleDto;
import helpers.*;

import java.io.Serializable;
import java.util.*;

public class Yard implements Serializable {
    private static final long serialVersionUID = 15641321L;
    private Map<String, Bundle> bundles;
    public Bundle lastBundleCreated;
    private Lift lift;

    public Yard() {
        setBundles(new HashMap<>());
        setLift(new Point2D(0,0));
    }

    public Yard(Map<String, Bundle> bundles) {
        setBundles(bundles);
        setLift(lift.position);
    }

    public Yard(Yard yard) {
        this.bundles = new HashMap<>(yard.getBundlesMap());
        this.lift = yard.getLift();
    }

    private List<Bundle> sortBundlesZ(List<Bundle> bundles) {
        bundles.sort(Comparator.comparing(Bundle::getZ));
        return bundles;
    }

    private List<Bundle> getBundlesWithMinZ(List<Bundle> bundles, double zMin) {
        List<Bundle> bundlesZMin = new ArrayList<>();
        for (Bundle bundle : bundles) {
            if (bundle.z >= zMin) {
                bundlesZMin.add(bundle);
            }
        }
        return bundlesZMin;
    }

    public List<Bundle> getBundles() {
        return new ArrayList<>(this.bundles.values());
    }

    public Map<String,Bundle> getBundlesMap(){
        return bundles;
    }

    public void setBundles(Map<String, Bundle> bundles) { this.bundles = bundles; }

    public Lift getLift() { return lift; }

    public void setLift(Point2D position) {
        this.lift = new Lift(position);
    }

    public Bundle createBundle(Point2D position) {
        Bundle bundle = new Bundle(position);
        putBundleToTop(bundle);
        bundles.put(bundle.getId(), bundle);
        lastBundleCreated = bundle;
        return bundle;
    }

    private void putBundleToTop(Bundle bundle) {
        List<Bundle> collidingBundles = getCollidingBundles(bundle, null);
        if (!collidingBundles.isEmpty()) {
            Bundle higherBundle = Collections.max(collidingBundles, Comparator.comparing(b -> b.getZ() + b.getHeight()));
            bundle.setZ(higherBundle.getZ() + higherBundle.getHeight());
        } else {
            bundle.setZ(0);
        }
    }

    public Bundle getBundle(String id) {
        return bundles.get(id);
    }

    public List<Bundle> getBundlesAtPosition(Point2D position) {
        List<Bundle> selectedBundles = new ArrayList<>();
        for (Bundle bundle : new ArrayList<>(bundles.values())) {
            CenteredRectangle rectangle = Converter.fromBundleToCenteredRectangle(bundle);
            if (GeomHelper.pointIsInsideRectangle(position, rectangle)) {
                selectedBundles.add(bundle);
            }
        }
        return selectedBundles;
    }

    public Bundle getTopBundle(Point2D position) {
        List<Bundle> bundles = sortBundlesZ(getBundlesAtPosition(position));
        if (!bundles.isEmpty()) {
            return bundles.get(bundles.size() - 1);
        }
        return null;
    }

    public void deleteBundle(String id) {
        bundles.remove(id);
    }

    public void modifyBundleProperties(BundleDto bundleDto)
    {
        Bundle bundle = getBundle(bundleDto.id);
        if (bundle != null) {
            Set<Bundle> allTimeCollidingBundles = new LinkedHashSet<>(getAllCollidingBundles(bundle));
            bundle.setBarcode(bundleDto.barcode);
            bundle.setHeight(bundleDto.height);
            bundle.setWidth(bundleDto.width);
            bundle.setLength(bundleDto.length);
            bundle.setTime(bundleDto.time);
            bundle.setDate(bundleDto.date);
            bundle.setEssence(bundleDto.essence);
            bundle.setPlanckSize(bundleDto.plankSize);
            bundle.setAngle(bundleDto.angle);
            bundle.setZ(bundleDto.z);
            allTimeCollidingBundles.addAll(getAllCollidingBundles(bundle));
            adjustBundlesHeightAfterChange(bundle, new ArrayList<>(allTimeCollidingBundles));
            UndoRedo.add(this);
        }
    }

    private void adjustBundlesHeightAfterChange(Bundle source, List<Bundle> allTimeCollidingBundles) {
        System.out.println("\n----------------\n");
        List<Bundle> bundlesHigher = sortBundlesZ(getBundlesWithMinZ(allTimeCollidingBundles, source.getZ() + source.getHeight()));
        adjustBundleHeight(source);
        if (!bundlesHigher.isEmpty()) {
            for (Bundle bundle : bundlesHigher) {
                adjustBundleHeight(bundle);
            }
        }
    }

    private void adjustBundleHeight(Bundle bundle) {
        System.out.println("Adjusting bundle " + bundle.getBarcode());
        List<Bundle> collidingBundles = sortBundlesZ(getCollidingBundles(bundle, null));
        double newZ = 0;
        for (Bundle lowerBundle : collidingBundles) {
            System.out.println("Colliding with " + lowerBundle.getBarcode());
            if (lowerBundle.getZ() >= bundle.getZ() + bundle.getHeight()) break;
            newZ = lowerBundle.getZ() + lowerBundle.getHeight();
            System.out.println("newZ adjusted to " + newZ);
        }
        System.out.println("Setting z to " + newZ);
        bundle.setZ(newZ);
    }

    public void modifyBundlePosition(String id, Point2D position)
    {
        Bundle bundle = getBundle(id);
        bundle.setPosition(position);
        putBundleToTop(bundle);
    }

    public List<Bundle> getCollidingBundles(Bundle bundleToCheck, Set<Bundle> exceptionList) {
        List<Bundle> collidingBundles = new ArrayList<>();
        for (Bundle bundle : getBundles()) {
            if (bundle != bundleToCheck) {
                if (exceptionList != null && exceptionList.contains(bundle)) {
                    continue;
                }
                boolean bundleCollides = GeomHelper.rectangleCollidesRectangle(
                        Converter.fromBundleToCenteredRectangle(bundle),
                        Converter.fromBundleToCenteredRectangle(bundleToCheck)
                );
                if (bundleCollides) {
                    collidingBundles.add(bundle);
                }
            }
        }
        return collidingBundles;
    }

    private List<Bundle> getAllCollidingBundles(Bundle originBundle) {
        Set<Bundle> allCollidingBundles = new HashSet<>();
        Set<Bundle> bundlesToCheck = new HashSet<>();

        if (originBundle != null) {
            bundlesToCheck.add(originBundle);

            while (!bundlesToCheck.isEmpty()) {
                Bundle bundle = bundlesToCheck.iterator().next();
                bundlesToCheck.remove(bundle);
                allCollidingBundles.add(bundle);
                Set<Bundle> exceptedBundles = new HashSet<>(allCollidingBundles);
                exceptedBundles.addAll(bundlesToCheck);
                for (Bundle collidingBundle : getCollidingBundles(bundle, exceptedBundles)) {
                    if (!allCollidingBundles.contains(collidingBundle)) {
                        bundlesToCheck.add(collidingBundle);
                    }
                }
            }
        }

        return new ArrayList<>(allCollidingBundles);
    }

    public List<Bundle> getAllCollidingBundles(BundleDto bundleToCheck) {
        return getAllCollidingBundles(getBundle(bundleToCheck.id));
    }

    public void moveLiftForward() {
        // TODO Calculate for front collision HERE (ONLY the lift, NOT the arms)
        lift.moveForward();
    }

    public void moveLiftBackward() {
        // TODO Calculate for back collision HERE (ONLY the lift, NOT the arms)
        lift.moveBackward();
    }

    public void turnLiftRight() {
        // TODO Calculate for rotational collision HERE (ONLY the lift, NOT the arms)
        lift.turnRight();
    }

    public void turnLiftLeft() {
        // TODO Calculate for rotational collision HERE (ONLY the lift, NOT the arms)
        lift.turnLeft();
    }
}
