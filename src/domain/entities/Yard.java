package domain.entities;

import domain.dtos.BundleDto;
import enums.Comparison;
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

    private List<Bundle> sortBundlesTopZ(List<Bundle> bundles) {
        bundles.sort(Comparator.comparing(Bundle::getTopZ));
        return bundles;
    }

    private List<Bundle> getBundlesWithMinZ(List<Bundle> bundles, double zMin) {
        return getBundlesWithMinZ(bundles, zMin, true);
    }

    private List<Bundle> getBundlesWithMinZ(List<Bundle> bundles, double zMin, boolean includeEquals) {
        List<Bundle> bundlesMinZ = new ArrayList<>();
        for (Bundle bundle : bundles) {
            boolean shouldAdd = includeEquals
                    ? MathHelper.compareDoubles(bundle.getZ(), zMin) != Comparison.SMALLER
                    : MathHelper.compareDoubles(bundle.getZ(), zMin) == Comparison.GREATER;
            if (shouldAdd) {
                bundlesMinZ.add(bundle);
            }
        }
        return bundlesMinZ;
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
        putBundleToTop(bundle, getCollidingBundles(bundle, null));
    }

    private void putBundleToTop(Bundle bundle, List<Bundle> bundlesToPutUnder) {
        if (!bundlesToPutUnder.isEmpty()) {
            Bundle higherBundle = Collections.max(bundlesToPutUnder, Comparator.comparing(Bundle::getTopZ));
            bundle.setZ(higherBundle.getTopZ());
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

    public void modifyBundleProperties(BundleDto bundleDto) {
        Bundle bundle = getBundle(bundleDto.id);
        if (bundle != null) {
            UndoRedo.add(this);
            Set<Bundle> allTimeCollidingBundles = new LinkedHashSet<>(getAllCollidingBundles(bundle, true));
            bundle.setBarcode(bundleDto.barcode);
            bundle.setHeight(MathHelper.round(bundleDto.height, 2));
            bundle.setWidth(MathHelper.round(bundleDto.width, 2));
            bundle.setLength(MathHelper.round(bundleDto.length, 2));
            bundle.setTime(bundleDto.time);
            bundle.setDate(bundleDto.date);
            bundle.setEssence(bundleDto.essence);
            bundle.setPlanckSize(bundleDto.plankSize);
            bundle.setAngle(MathHelper.round(bundleDto.angle, 2));
            bundle.setZ(MathHelper.round(bundleDto.z, 2));
            allTimeCollidingBundles.addAll(getAllCollidingBundles(bundle, true));
            adjustBundlesHeightAfterChange(bundle, new ArrayList<>(allTimeCollidingBundles));
        }
    }

    private void adjustBundlesHeightAfterChange(Bundle source, List<Bundle> allTimeCollidingBundles) {
        List<Bundle> bundlesToAdjust = sortBundlesZ(getBundlesWithMinZ(allTimeCollidingBundles, source.getZ()));
        adjustBundleHeight(source, bundlesToAdjust);
        Iterator<Bundle> bundlesToAdjustIterator = bundlesToAdjust.iterator();
        while (bundlesToAdjustIterator.hasNext()) {
            Bundle bundleToAdjust = bundlesToAdjustIterator.next();
            bundlesToAdjustIterator.remove();
            adjustBundleHeight(bundleToAdjust, bundlesToAdjust);
        }
    }

    private void adjustBundleHeight(Bundle bundle, List<Bundle> exceptionList) {
        List<Bundle> bundlesToPutUnder = getCollidingBundles(bundle, new HashSet<>(exceptionList));
        putBundleToTop(bundle, bundlesToPutUnder);
    }

    public void modifyBundlePosition(String id, Point2D position) {
        Bundle bundle = getBundle(id);
        bundle.setPosition(new Point2D(
                MathHelper.round(position.getX(), 2),
                MathHelper.round(position.getY(), 2)
        ));
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

    private List<Bundle> getAllCollidingBundles(Bundle originBundle, boolean removeOriginBundle) {
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
            if (removeOriginBundle) {
                allCollidingBundles.removeIf(b -> b.equals(originBundle));
            }
        }
        return new ArrayList<>(allCollidingBundles);
    }

    public List<Bundle> getAllCollidingBundles(BundleDto bundleToCheck) {
        return getAllCollidingBundles(getBundle(bundleToCheck.id), false);
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
