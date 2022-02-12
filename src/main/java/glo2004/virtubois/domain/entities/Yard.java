package glo2004.virtubois.domain.entities;

import glo2004.virtubois.domain.dtos.BundleDto;
import glo2004.virtubois.domain.dtos.LiftDto;
import glo2004.virtubois.enums.Comparison;
import glo2004.virtubois.helpers.*;

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

    public List<Bundle> sortBundlesZ(List<Bundle> bundles) {
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
                zMin = bundle.getTopZ();
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
        UndoRedo.addCurrentYard();
        Bundle bundle = new Bundle(position);
        bundles.put(bundle.getId(), bundle);
        putBundleToTop(bundle);
        CenteredRectangle rectangleNew = new CenteredRectangle(bundle);
        for(Bundle bundlesInLift : lift.getBundlesOnLift()) {
            CenteredRectangle rectangleInLift = new CenteredRectangle(bundlesInLift);
            if (GeomHelper.rectangleCollidesRectangle(rectangleNew,rectangleInLift)) {
                UndoRedo.undoAction();
                return null;
            }
        }
        if (liftCollidesAnyBundle()) {
            UndoRedo.undoAction();
            return null;
        }
        else{
            return bundle;
        }
    }

    private void putBundleToTop(Bundle bundle) {
        putBundleToTop(bundle, getCollidingBundles(bundle, null));
    }

    private void putBundleToTop(Bundle bundle, List<Bundle> bundlesToPutUnder) {
        if (!bundlesToPutUnder.isEmpty()) {
            Bundle higherBundle = Collections.max(bundlesToPutUnder, Comparator.comparing(Bundle::getTopZ));
            bundle.setZ(higherBundle.getTopZ());
            if (liftCollidesAnyBundle()) {
                deleteBundle(bundle.getId());
                UndoRedo.undoUndo();
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
        boolean isOnLift = false;
        List<Bundle> bundleOnLift = lift.getBundlesOnLift();
        if (!bundleOnLift.isEmpty()) {
            for (Bundle bundleLift : bundleOnLift) {
                if (bundleLift.getId().equals(bundleDto.id)) {
                    isOnLift = true;
                    break;
                }
            }
        }
        if (bundle != null && !modifiedBundleCollidesLift && !isOnLift) {
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
            if (liftCollidesAnyBundle()) {
                UndoRedo.undoAction();
            }
        } else {
            UndoRedo.undoUndo();
        }
    }

    public void modifyLiftProperties(LiftDto liftDto) {
        double scaleBackup = lift.getScale();
        if (liftDto != null && !liftDtoCollidesAnyBundle(liftDto) && liftDto.length > liftDto.armsLength) {
            lift.setArmsHeight(MathHelper.round(liftDto.armsHeight, 2));
            lift.setPosition(new Point2D(liftDto.position.getX(), liftDto.position.getY()));
            lift.setAngle(MathHelper.round(liftDto.angle, 2));
            lift.setScale(MathHelper.round(liftDto.scale, 2));
        }
        if (liftDtoCollidesAnyBundle(new LiftDto(lift))) {
            lift.setScale(scaleBackup);
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
        boolean isOnLift = false;
        List<Bundle> bundleOnLift = lift.getBundlesOnLift();
        if (!bundleOnLift.isEmpty()) {
            for (Bundle bundleLift : bundleOnLift) {
                if (bundleLift.getId().equals(id)) {
                    isOnLift = true;
                    break;
                }
            }
        }
        if (bundle != null && !isOnLift) {
            Point2D oldPosition = bundle.getPosition();
            bundle.setPosition(position);
            putBundleToTop(bundle);
            if (liftCollidesAnyBundle()) {
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
        if (!lift.getBundlesOnLift().isEmpty()) {
            movingBundles(lift.getBundlesOnLift(), true);
        }
        lift.moveForward();
        if (liftCollidesAnyBundle()) {
            lift.moveBackward();
            if (!lift.getBundlesOnLift().isEmpty()) {
                movingBundles(lift.getBundlesOnLift(), false);
            }
        }
    }

    public void moveLiftBackward() {
        if (!lift.getBundlesOnLift().isEmpty()) {
            movingBundles(lift.getBundlesOnLift(), false);
        }
        lift.moveBackward();
        if (liftCollidesAnyBundle()) {
            lift.moveForward();
            if (!lift.getBundlesOnLift().isEmpty()) {
                movingBundles(lift.getBundlesOnLift(), true);
            }
        }
    }

    public void moveLiftToBundle() {
        if (lift.getBundlesOnLift().isEmpty()) {
            Point2D point1 = new Point2D(lift.position.getX(), lift.position.getY());
            Point2D point2 = new Point2D(lift.position.getX() + 100000 * Math.cos(Math.toRadians(lift.angle)), lift.position.getY() + 100000 * Math.sin(Math.toRadians(lift.angle)));
            Point2D point3 = new Point2D(lift.position.getX() + (lift.getArmsLength() / 2), lift.position.getY() + (lift.getArmsLength() / 2));
            Point2D point4 = new Point2D(lift.position.getX() + (100000 + lift.getArmsLength() / 2) * Math.cos(Math.toRadians(lift.angle)), lift.position.getY() + (lift.getArmsLength() / 2 + 100000) * Math.sin(Math.toRadians(lift.angle)));
            Point2D point5 = new Point2D(lift.position.getX() - (lift.getArmsLength() / 2) * Math.cos(Math.toRadians(lift.angle)), lift.position.getY() - (lift.getArmsLength() / 2) * Math.sin(Math.toRadians(lift.angle)));
            Point2D point6 = new Point2D(lift.position.getX() - (lift.getArmsLength() / 2 + 100000) * Math.cos(Math.toRadians(lift.angle)), lift.position.getY() - (lift.getArmsLength() / 2 + 100000) * Math.sin(Math.toRadians(lift.angle)));
            List<Bundle> listBundles = getBundles();
            for (Bundle bundles : listBundles) {
                double z = bundles.getZ();
                CenteredRectangle rectangle = new CenteredRectangle(bundles.position.getX(), bundles.position.getY(), bundles.width, bundles.length, bundles.angle);
                if (GeomHelper.lineIntersectsRectangle(point1, point2, rectangle) && lift.height > z) {
                    while (!liftCollidesAnyBundle()) {
                        lift.moveForward();
                    }
                    lift.moveBackward();
                }
                if (GeomHelper.lineIntersectsRectangle(point3, point4, rectangle) && lift.height > z) {
                    while (!liftCollidesAnyBundle()) {
                        lift.moveForward();
                    }
                    lift.moveBackward();
                }
                if (GeomHelper.lineIntersectsRectangle(point5, point6, rectangle) && lift.height > z) {
                    while (!liftCollidesAnyBundle()) {
                        lift.moveForward();
                    }
                }
            }
        }
    }

    public void turnLiftRight() {
        if (!lift.getBundlesOnLift().isEmpty()) {
            turnBundlesRight(lift.getBundlesOnLift());
        }
        lift.turnRight();
        if (liftCollidesAnyBundle()) {
            lift.turnLeft();
            if (!lift.getBundlesOnLift().isEmpty()) {
                turnBundlesLeft(lift.getBundlesOnLift());
            }
        }
    }

    public void turnLiftLeft() {
        if (!lift.getBundlesOnLift().isEmpty()) {
            turnBundlesLeft(lift.getBundlesOnLift());
        }
        lift.turnLeft();
        if (liftCollidesAnyBundle()) {
            lift.turnRight();
            if (!lift.getBundlesOnLift().isEmpty()) {
                turnBundlesRight(lift.getBundlesOnLift());
            }
        }
    }

    public void riseArms() {
        lift.riseArms();
    }

    public void lowerArms() {
        lift.lowerArms();
    }

    public List<Bundle> bundlesToMove() {
        List<Bundle> bundleTolift = new ArrayList<>();
        CenteredRectangle rectArms = new CenteredRectangle(lift.getArmsPosition().getX(), lift.getArmsPosition().getY(), lift.getArmsWidth(), lift.getArmsLength(), lift.angle);
        List<Bundle> bundlesSorted = sortBundlesZ(getBundles());
        Bundle bundleUnderAll = null;
        for (Bundle bundle : bundlesSorted) {
            CenteredRectangle rectangle1 = new CenteredRectangle(bundle);
            if (GeomHelper.rectangleCollidesRectangle(rectArms, rectangle1)) {
                if (bundle.getZ() >= lift.getArmsHeight()) {
                    bundleUnderAll = bundle;
                    break;
                }
            }
        }
        if (bundleUnderAll == null) {
            return bundleTolift;
        }
        Deque<Bundle> bundlesToCheck = new ArrayDeque<>();
        bundlesToCheck.addLast(bundleUnderAll);
        while (!bundlesToCheck.isEmpty()) {
            Bundle check = bundlesToCheck.peekFirst();
            Set<Bundle> exceptions = new HashSet<>();
            exceptions.add(check);
            List<Bundle> allColliding = getCollidingBundles(check, null);
            for (Bundle bundle : allColliding) {
                if (bundle.getZ() <= check.getZ()) {
                    exceptions.add(bundle);
                }
            }
            List<Bundle> goodBundles = getCollidingBundles(check, exceptions);
            for (Bundle bundle : goodBundles) {
                if (!bundlesToCheck.contains(bundle)) {
                    bundlesToCheck.add(bundle);

                }
            }
            if (!bundleTolift.contains(bundlesToCheck.getFirst())) {
                bundleTolift.add(bundlesToCheck.getFirst());
            }
            bundlesToCheck.pop();
        }
        return bundleTolift;
    }

    private void movingBundles(List<Bundle> bundlesToMove, boolean movingForward) {
        if (!bundlesToMove.isEmpty()) {
            for (Bundle bundle : bundlesToMove) {
                Point2D increment = new Point2D(ConfigHelper.liftPositionIncrement);
                Point2D rotatedIncrement = GeomHelper.getRotatedVector(increment, lift.angle);
                if (movingForward) bundle.setPosition(bundle.position.add(rotatedIncrement));
                else bundle.setPosition(bundle.position.substract(rotatedIncrement));
            }
        }
    }

    private void turnBundlesLeft(List<Bundle> bundlesToMove) {
        if (!bundlesToMove.isEmpty()) {
            for (Bundle bundle : bundlesToMove) {
                double alpha = Math.toRadians(+5.0);
                bundle.setPosition(changeBundleOnLift(bundle, alpha));
                bundle.setAngleOnLift(bundle.getAngle() + 5.0);
            }
        }
    }

    private void turnBundlesRight(List<Bundle> bundlesToMove) {
        if (!bundlesToMove.isEmpty()) {
            for (Bundle bundle : bundlesToMove) {
                double alpha = Math.toRadians(-5.0);
                bundle.setPosition(changeBundleOnLift(bundle, alpha));
                bundle.setAngleOnLift(bundle.getAngle() - 5.0);
            }
        }
    }

    private Point2D changeBundleOnLift(Bundle bundle, double alpha) {
        double x1 = lift.getPosition().getX();
        double y1 = lift.getPosition().getY();
        double x2 = bundle.getPosition().getX();
        double y2 = bundle.getPosition().getY();
        double xPos = (x2 - x1) * Math.cos(alpha) - (y2 - y1) * Math.sin(alpha) + x1;
        double yPos = (x2 - x1) * Math.sin(alpha) + (y2 - y1) * Math.cos(alpha) + y1;
        return (new Point2D(xPos, yPos));
    }

    public void setLiftBundles() {
        List<Bundle> bundlesToMove = bundlesToMove();
        if (!bundlesToMove.isEmpty()) {
            UndoRedo.addCurrentYard();
            lift.setBundlesOnLift(bundlesToMove);
        }
    }

    public void clearLiftBundles() {
        if (!lift.getBundlesOnLift().isEmpty()) {
            UndoRedo.addCurrentYard();
            List<Bundle> sortedBundles = sortBundlesZ(lift.getBundlesOnLift());
            for (Bundle bundle : sortedBundles) {
                List<Bundle> exception = new ArrayList<>();
                for (Bundle otherBundle : lift.getBundlesOnLift()) {
                    if (otherBundle.getZ() > bundle.getZ()) {
                        exception.add(otherBundle);
                    }
                }
                adjustBundleHeight(bundle, exception);
            }
        }
        lift.clearBundles();
    }
}

