package domain.controllers;

import domain.dtos.BundleDto;
import domain.entities.Bundle;
import domain.entities.Yard;
import helpers.Converter;
import javafx.geometry.Point2D;

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

    /**** PRIVATE METHODS ****/

}