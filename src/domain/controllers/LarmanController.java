package domain.controllers;

import domain.dtos.BundleDto;
import domain.dtos.LiftDto;
import domain.entities.Bundle;
import domain.entities.Lift;
import domain.entities.Yard;
import helpers.Converter;
import helpers.Point2D;

import java.util.ArrayList;
import java.util.Collections;
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
        return new BundleDto(yard.createBundle(position));
    }

    private List<Bundle> sortBundlesZ(List<Bundle> bundles) {
        bundles.sort(Comparator.comparing(Bundle::getZ));
        return bundles;
    }

    public List<BundleDto> sortBundlesY(List<BundleDto> bundles) {
        bundles.sort(Comparator.comparing(BundleDto::getY));
        Collections.reverse(bundles);
        return bundles;
    }

    public List<BundleDto> sortBundlesX(List<BundleDto> bundles) {
        bundles.sort(Comparator.comparing(BundleDto::getX));
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

    public List<BundleDto> sortBundlesDtoZ(List<BundleDto> bundleDtos) {
        bundleDtos.sort(Comparator.comparing(BundleDto::getZ));
        return bundleDtos;
    }

    public LiftDto getLift() {
        Lift lift = getYard().getLift();
        return new LiftDto(lift);
    }

    public void setLiftMovement(LiftDto liftDto) {
        getYard().setLiftMovement(liftDto);

    }

    public BundleDto getBundle(String id) {
        Bundle bundle = yard.getBundle(id);
        if (bundle == null) return null;
        return new BundleDto(bundle);
    }

    public List<BundleDto> getSelectedBundles(Point2D position) {
        List<Bundle> bundles = yard.getBundlesAtPosition(position);
        return Converter.fromBundlesToBundleDtos(
                sortBundlesZ(bundles)
        );
    }

    public BundleDto getTopBundle(Point2D position) {
        List<BundleDto> bundlesDto = getSelectedBundles(position);
        if (!bundlesDto.isEmpty()) {
            return bundlesDto.get(bundlesDto.size() - 1);
        }
        return null;
    }

    public void modifyBundleProperties(BundleDto bundleDto) {
        double zChange = bundleDto.height - getBundle(bundleDto.id).height;
        yard.modifyBundleProperties(bundleDto);
        List<BundleDto> bundlesInStack = new ArrayList<>();
        getAllCollidingBundles(bundlesInStack, bundleDto);
        for (BundleDto bundle : bundlesInStack) {
            if (bundleDto.z < bundle.z && bundleDto.id != bundle.id) {
                bundle.z = bundle.z + zChange;
                yard.modifyBundleProperties(bundle);
            }
        }
    }

    public void modifyBundlePosition(String id, Point2D position) {
        yard.modifyBundlePosition(id, position);
        //TODO modify z if bundle doesn't collide anymore
    }

    public void deleteBundle(String id) {
        yard.deleteBundle(id);
    }

    public List<BundleDto> getCollidingBundles(BundleDto bundleDtoToCheck) {
        Bundle bundleToCheck = yard.getBundle(bundleDtoToCheck.id);
        if (bundleToCheck != null) {
            return Converter.fromBundlesToBundleDtos(
                    yard.getCollidingBundles(bundleToCheck)
            );
        }
        return null;
    }

    public List<BundleDto> getAllCollidingBundles(List<BundleDto> bundles, BundleDto bundleToCheck) {
        bundles.add(bundleToCheck);
        for (BundleDto bundle : getCollidingBundles(bundleToCheck)) {
            int count = 0;
            for (BundleDto bundleInBundles : bundles) {
                if (bundleInBundles.id != bundle.id) {
                    count++;
                }
            }
            if (count == bundles.size()) {
                bundles = getAllCollidingBundles(bundles, bundle);
            }
        }
        return bundles;
    }

    public List<BundleDto> getAllCollidingBundlesZMin(List<BundleDto> bundles, BundleDto bundleToCheck, float zMin) {
        List<BundleDto> bundlesZMin = new ArrayList<>();
        for (BundleDto bundle : getAllCollidingBundles(bundles, bundleToCheck)) {
            if (bundle.z >= zMin) {
                bundlesZMin.add(bundle);
            }
        }
        return bundlesZMin;
    }

    public void modifyBundlesPositionUsingLift(List<BundleDto> bundles, Point2D position) {
        for (BundleDto bundle : bundles) {
            yard.modifyBundlePosition(bundle.id, position);
        }
    }
}