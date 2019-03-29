package domain.controllers;

import domain.dtos.BundleDto;
import domain.entities.Bundle;
import domain.entities.Yard;
import helpers.Converter;
import helpers.Point2D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LarmanController implements Serializable {
    private static final LarmanController instance = new LarmanController();
    private List<BundleDto> allCollidingBundles = new ArrayList<>();
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

    public Yard getYard() { return yard; }

    /**** PUBLIC METHODS ****/
    public BundleDto createBundle(Point2D position) {
        return new BundleDto(yard.createBundle(position));
    }

    public BundleDto getLastBundle() {
        return new BundleDto(yard.lastBundleCreated);
    }

    private List<Bundle> sortBundlesZ(List<Bundle> bundles) {
        bundles.sort(Comparator.comparing(Bundle::getZ));
        return bundles;
    }

    private List<BundleDto> sortBundlesY(List<BundleDto> bundles) {
        bundles.sort(Comparator.comparing(BundleDto::getY));
        return bundles;
    }

    private List<BundleDto> sortBundlesX(List<BundleDto> bundles) {
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
    
    public BundleDto getTopBundle(Point2D position)
    {
        List<BundleDto> bundlesDto = getSelectedBundles(position);
        if (!bundlesDto.isEmpty()) {
            return bundlesDto.get(bundlesDto.size() - 1);
        }
        return null;
    }

    public void modifyBundleProperties(BundleDto bundleDto)
    {
        yard.modifyBundleProperties(bundleDto);
    }

    public void modifyBundlePosition(String id, Point2D position)
    {
        yard.modifyBundlePosition(id, position);
    }

    public void deleteBundle(String id) {
        yard.deleteBundle(id);
    }

    public List<BundleDto> getCollidingBundles(BundleDto bundleDtoToCheck){
        Bundle bundleToCheck = yard.getBundle(bundleDtoToCheck.id);
        if (bundleToCheck != null) {
            return Converter.fromBundlesToBundleDtos(
                    yard.getCollidingBundles(bundleToCheck)
            );
        }
        return null;
    }

    public List<BundleDto> getAllCollidingBundles(List<BundleDto> bundles, BundleDto bundleToCheck){
        bundles.add(bundleToCheck);
        for (BundleDto bundle : getCollidingBundles(bundleToCheck)) {
            int count = 0;
            for (BundleDto bundleInBundles : bundles) {
                if (bundleInBundles.id != bundle.id) {
                    count++;
                }
            }
            if (count == bundles.size()){
                bundles = getAllCollidingBundles(bundles, bundle);
            }
        }
        return bundles;
    }

}