package domain.controllers;

import domain.dtos.BundleDto;
import domain.entities.Bundle;
import domain.entities.Yard;
import helpers.Converter;
import javafx.geometry.Point2D;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public class LarmanController {
    private static final LarmanController instance = new LarmanController();
    private Yard yard;

    private LarmanController() {
        yard = new Yard();
    }

    public static LarmanController getInstance() {
        return instance;
    }

    public void setYard(Yard yard) {
        instance.yard = yard;
    }

    /**** PUBLIC METHODS ****/
    public void createBundle(Point2D position) {
        yard.createBundle(position);
    }

    public BundleDto getLastBundle() {
        return yard.lastBundleCreated;
    }

    public List<BundleDto> getBundles() {
    return Converter.fromBundlesToBundleDtos(yard.getBundles());
    }

    public BundleDto getBundle(String id) {
        Bundle bundle = yard.getBundle(id);
        if (bundle == null) return null;
        return new BundleDto(bundle);
    }

    public List<BundleDto> getSelectedBundles(Point2D position) {
        List<Bundle> bundles = yard.getBundlesAtPosition(position);
        return Converter.fromBundlesToBundleDtos(bundles);
    }
    
    public BundleDto getTopBundle(Point2D position)
    {
        List<BundleDto> bundlesDto = getSelectedBundles(position);
        if (!bundlesDto.isEmpty()) {
            bundlesDto.sort(Comparator.comparing(BundleDto::getZ).reversed());
            return bundlesDto.get(0);
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

    public List<BundleDto> getCollidingBundleDtos(BundleDto bundleToCheckDto){
        Bundle bundleToCheck = yard.getBundle(bundleToCheckDto.id);
        List<BundleDto> bundlesDto;
        List<Bundle> bundles =  yard.getCollidingBundles(bundleToCheck);
        bundlesDto = Converter.fromBundlesToBundleDtos(bundles);
        return bundlesDto;
    }

    /**** PRIVATE METHODS ****/

}