package domain.entities;

import domain.dtos.BundleDto;
import domain.dtos.LiftDto;
import enums.Comparison;
import helpers.*;

import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.*;

public class Yard implements Serializable {
    private static final long serialVersionUID = 15641321L;
    private Map<String, Bundle> bundles;
    private Lift lift;

    public Yard() {
        this.bundles = new HashMap<>();
        this.lift = new Lift(new Point2D(0, 0));
    }

    private List<Bundle> sortBundlesZ(List<Bundle> bundles) {
        bundles.sort(Comparator.comparing(Bundle::getZ));
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

    public Lift getLift() {
        return lift;
    }

    public Bundle createBundle(Point2D position) {
        Bundle bundle = new Bundle(position);
        if (!liftCollidesBundle(bundle)) {
            UndoRedo.addCurrentYard();
            bundles.put(bundle.getId(), bundle);
            putBundleToTop(bundle);
            return bundle;
        } else {
            return null;
        }
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
            if (GeomHelper.pointIsInsideRectangle(position, new CenteredRectangle(bundle))) {
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

    public void modifyLiftProperties(LiftDto liftDto) {
        if (lift != null) {
            lift.setArmsHeight(liftDto.armsHeight);
            lift.setPosition(new Point2D(liftDto.position.getX(), liftDto.position.getY()));
            lift.setAngle(liftDto.angle);
            lift.repositionArms();
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
                    new CenteredRectangle(bundle),
                    new CenteredRectangle(bundleToCheck)
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

    private boolean liftCollidesAnyBundle() {
        return liftCollidesAnyBundle(getBundles());
    }

    private boolean liftCollidesAnyBundle(List<Bundle> bundles) {
        for (Bundle bundle : bundles) {
            if (liftCollidesBundle(bundle) && lift.getHeight() > bundle.getZ()) {
                return true;
            }
        }
        return false;
    }

    private boolean liftCollidesBundle(Bundle bundle) {
        return GeomHelper.rectangleCollidesRectangle(
                new CenteredRectangle(bundle),
                new CenteredRectangle(lift)
        );
    }

    public void moveLiftForward() {
        lift.moveForward();
        if (liftCollidesAnyBundle()) {
            lift.moveBackward();
        }
    }

    public void moveLiftBackward() {
        lift.moveBackward();
        if (liftCollidesAnyBundle()) {
            lift.moveForward();
        }
    }

    public void moveLiftToBundle() {
        Line2D line = new Line2D.Double(lift.position.getX()* (Math.cos(lift.angle - 90)), lift.position.getY() * lift.angle, Math.cos(lift.angle) + 10000, Math.sin(lift.angle) + 10000);{
        }
    }
    public void turnLiftRight() {
        lift.turnRight();
        if (liftCollidesAnyBundle()) {
            lift.turnLeft();
        }
    }

    public void turnLiftLeft() {
        lift.turnLeft();
        if (liftCollidesAnyBundle()) {
            lift.turnRight();
        }
    }

    public void riseArms() {
        lift.riseArms();
    }

    public void lowerArms(){
        if (lift.getArmsHeight() > 0) {
            lift.lowerArms();
        }
        if (lift.getArmsHeight() < 0) {
            lift.setArmsHeight(0);
        }
    }
}
