package domain.controllers;

import domain.dtos.BundleDto;
import domain.dtos.LiftDto;
import domain.entities.Bundle;
import domain.entities.Yard;
import helpers.Converter;
import helpers.Point2D;
import helpers.UndoRedo;

import java.util.Comparator;
import java.util.List;

public class LarmanController {
    private static final LarmanController instance = new LarmanController();
    private Yard yard;

    private LarmanController() {
        clearYard();
    }

    public static LarmanController getInstance() {
        return instance;
    }

    public void clearYard() {
        yard = new Yard();
    }

    public void setYard(Yard newYard) {
        yard = newYard;
    }

    public Yard getYard() {
        return yard;
    }

    public BundleDto createBundle(Point2D position) {
        Bundle bundle = yard.createBundle(position);
        if (bundle != null) {
            return new BundleDto(bundle);
        } else {
            return null;
        }
    }

    private List<Bundle> sortBundlesZ(List<Bundle> bundles) {
        bundles.sort(Comparator.comparing(Bundle::getZ));
        return bundles;
    }

    public List<BundleDto> getBundles() {
        return Converter.fromBundlesToBundleDtos(yard.getBundles());
    }

    public List<BundleDto> getBundlesSortedZ() {
        return Converter.fromBundlesToBundleDtos(
            sortBundlesZ(yard.getBundles())
        );
    }

    public LiftDto getLift() {
        return new LiftDto(yard.getLift());
    }

    public List<BundleDto> getSelectedBundles(Point2D position) {
        List<Bundle> bundles = yard.getBundlesAtPosition(position);
        return Converter.fromBundlesToBundleDtos(
            sortBundlesZ(bundles)
        );
    }

    public BundleDto getTopBundle(Point2D position) {
        return new BundleDto(yard.getTopBundle(position));
    }

    public void modifyBundleProperties(BundleDto bundleDto) {
        UndoRedo.addCurrentYard();
        yard.modifyBundleProperties(bundleDto);
    }

    public void modifyLiftProperties(LiftDto liftDto){
        yard.modifyLiftProperties(liftDto);
    }

    public void modifyBundlePosition(String id, Point2D position) {
        yard.modifyBundlePosition(id, position);
    }

    public void deleteBundle(String id) {
        UndoRedo.addCurrentYard();
        yard.deleteBundle(id);
    }

    public List<BundleDto> getCollidingBundles(BundleDto bundleDtoToCheck) {
        Bundle bundleToCheck = yard.getBundle(bundleDtoToCheck.id);
        if (bundleToCheck != null) {
            return Converter.fromBundlesToBundleDtos(
                yard.getCollidingBundles(bundleToCheck, null)
            );
        }
        return null;
    }

    public List<BundleDto> getAllCollidingBundles(BundleDto bundleToCheck) {
        List<Bundle> collidingBundles = yard.getAllCollidingBundles(bundleToCheck);
        return Converter.fromBundlesToBundleDtos(collidingBundles);
    }

    public void riseArms() {
        yard.riseArms();
    }

    public void lowerArms() {
        yard.lowerArms();
    }

    public void moveLiftToBundle() {yard.moveLiftToBundle();}

    public void moveLiftForward() {
        yard.moveLiftForward();
    }

    public void moveLiftBackward() {
        yard.moveLiftBackward();
    }

    public void turnLiftRight() {
        yard.turnLiftRight();
    }

    public void turnLiftLeft() {
        yard.turnLiftLeft();
    }
}