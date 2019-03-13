package domain.controllers;

import domain.dtos.BundleDto;
import domain.entities.Bundle;
import domain.entities.Yard;
import javafx.geometry.Point2D;

import java.util.ArrayList;
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

    public List<BundleDto> getBundles() {
    return convertBundlesToDtos(yard.getBundles());
    }

    public BundleDto getBundle(long id) {
        Bundle bundle = yard.getBundle(id);
        if (bundle == null) return null;
        return new BundleDto(bundle);
    }

    public List<BundleDto> getSelectedBundles(Point2D position) {
        List<Bundle> bundles = yard.getBundlesAtPosition(position);
        return convertBundlesToDtos(bundles);
    }

    /**** PRIVATE METHODS ****/
    private List<BundleDto> convertBundlesToDtos(List<Bundle> bundles) {
        List<BundleDto> dtos = new ArrayList<>();
        for (Bundle bundle : yard.getBundles()) {
            dtos.add(new BundleDto(bundle));
        }
        return dtos;
    }
}