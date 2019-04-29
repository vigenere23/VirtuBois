package domain.entities;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import domain.dtos.LiftDto;
import enums.Comparison;
import helpers.*;

import java.awt.geom.Line2D;
import java.io.Serializable;
import java.math.MathContext;
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
        UndoRedo.addCurrentYard();
        bundles.put(bundle.getId(), bundle);
        putBundleToTop(bundle);
        if (!liftCollidesAnyBundle()) {
            return bundle;
        }
        else if (liftCollidesAnyBundle()){
            UndoRedo.undoAction();
        } else {
            return null;
        }
        return null;
    }

    private void putBundleToTop(Bundle bundle) {
        putBundleToTop(bundle, getCollidingBundles(bundle, null));
    }

    private void putBundleToTop(Bundle bundle, List<Bundle> bundlesToPutUnder) {
        if (!bundlesToPutUnder.isEmpty()) {
            Bundle higherBundle = Collections.max(bundlesToPutUnder, Comparator.comparing(Bundle::getTopZ));
            bundle.setZ(higherBundle.getTopZ());
            if(liftCollidesAnyBundle()){
                UndoRedo.undoAction();
            }
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
        boolean modifiedBundleCollidesLift = GeomHelper.rectangleCollidesRectangle(
                new CenteredRectangle(bundleDto),
                new CenteredRectangle(lift)
        );
        if (bundle != null && !modifiedBundleCollidesLift) {
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
            if(liftCollidesAnyBundle()){
                UndoRedo.undoAction();
            }
        }
        else {
            UndoRedo.undoUndo();
        }
    }

    public void modifyLiftProperties(LiftDto liftDto) {
        if (liftDto != null && !liftDtoCollidesAnyBundle(liftDto) && liftDto.length > liftDto.armsLength) {
            lift.setArmsHeight(MathHelper.round(liftDto.armsHeight,2));
            lift.setPosition(new Point2D(liftDto.position.getX(), liftDto.position.getY()));
            lift.setAngle(MathHelper.round(liftDto.angle,2));
            lift.setHeight(MathHelper.round(liftDto.height, 2));
            lift.setLength(MathHelper.round(liftDto.length, 2));
            lift.setWidth(MathHelper.round(liftDto.width,2));
            lift.setArmsLength(MathHelper.round(liftDto.armsLength,2));
            lift.setArmsWidth(MathHelper.round(liftDto.armsWidth,2));
            lift.setScale(MathHelper.round(liftDto.scale,2));
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
        if (bundle != null) {
            Point2D oldPosition = bundle.getPosition();
            bundle.setPosition(position);
            putBundleToTop(bundle);
            if(liftCollidesAnyBundle()){
                bundle.setPosition(oldPosition);
            }
        }
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

    private boolean liftDtoCollidesAnyBundle(LiftDto liftDto) {
        return liftDtoCollidesAnyBundle(liftDto, getBundles());
    }

    private boolean liftDtoCollidesAnyBundle(LiftDto liftDto, List<Bundle> bundles) {
        for (Bundle bundle : bundles) {
            if (liftDtoCollidesBundle(liftDto, bundle) && liftDto.height > bundle.getZ()) {
                return true;
            }
        }
        return false;
    }

    private boolean liftCollidesBundle(Bundle bundle) {
        return liftDtoCollidesBundle(new LiftDto(lift), bundle);
    }

    private boolean liftDtoCollidesBundle(LiftDto liftDto, Bundle bundle) {
        return GeomHelper.rectangleCollidesRectangle(
                new CenteredRectangle(bundle),
                new CenteredRectangle(liftDto)
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
        Point2D point1 = new Point2D(lift.position.getX(), lift.position.getY());
        Point2D point2 = new Point2D(lift.position.getX() + 100000 * Math.cos(Math.toRadians(lift.angle)), lift.position.getY() + 100000 * Math.sin(Math.toRadians(lift.angle)));
        Point2D point3 = new Point2D(lift.position.getX() + (lift.getArmsLength()/2), lift.position.getY() + (lift.getArmsLength()/2));
        Point2D point4 = new Point2D(lift.position.getX() + (100000 + lift.getArmsLength()/2) * Math.cos(Math.toRadians(lift.angle)) , lift.position.getY() + (lift.getArmsLength()/2 + 100000) * Math.sin(Math.toRadians(lift.angle)));
        Point2D point5 = new Point2D(lift.position.getX() - (lift.getArmsLength()/2) * Math.cos(Math.toRadians(lift.angle)) , lift.position.getY() - (lift.getArmsLength()/2) * Math.sin(Math.toRadians(lift.angle)));
        Point2D point6 = new Point2D(lift.position.getX() - (lift.getArmsLength()/2 + 100000) * Math.cos(Math.toRadians(lift.angle)) , lift.position.getY() - (lift.getArmsLength()/2 + 100000) * Math.sin(Math.toRadians(lift.angle)));
        System.out.println(point3.getX());
        List<Bundle> listBundles = getBundles();
        for (Bundle bundles : listBundles) {
            double z = bundles.getZ();
            CenteredRectangle rectangle = new CenteredRectangle(bundles.position.getX(), bundles.position.getY(), bundles.width, bundles.length, bundles.angle);
            if (GeomHelper.lineIntersectsRectangle(point1, point2, rectangle) && lift.height > z) {
                while(!liftCollidesAnyBundle()){
                    lift.moveForward();
                }
            }
            if (GeomHelper.lineIntersectsRectangle(point3, point4, rectangle) && lift.height > z) {
                while(!liftCollidesAnyBundle()){
                    lift.moveForward();
                }
            }
            if (GeomHelper.lineIntersectsRectangle(point5, point6, rectangle) && lift.height > z) {
                while(!liftCollidesAnyBundle()){
                    lift.moveForward();
                }
            }
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

    public void moveBundles(){

    }
    public void setLiftScale(double scale){
        lift.setScale(scale);
    }
}
