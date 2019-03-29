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
        yard = new Yard();
    }

    public static LarmanController getInstance() {
        return instance;
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

    private List<Bundle> sortBundles(List<Bundle> bundles) {
        bundles.sort(Comparator.comparing(Bundle::getZ));
        return bundles;
    }

    public List<BundleDto> getBundles() {
        return Converter.fromBundlesToBundleDtos(yard.getBundles());
    }

    public List<BundleDto> getBundlesSorted() {
        return Converter.fromBundlesToBundleDtos(
                sortBundles(yard.getBundles())
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
                sortBundles(bundles)
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

    public List<BundleDto> getAllCollidingBundles(BundleDto bundleDtoToCheck){
        if (allCollidingBundles.contains(bundleDtoToCheck)) {
            return allCollidingBundles;
        }
        allCollidingBundles.add(bundleDtoToCheck);
        for (BundleDto bundle : getCollidingBundles(bundleDtoToCheck)) {
            getAllCollidingBundles(bundle);
        }
        return allCollidingBundles;
    }

}