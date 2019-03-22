package domain.controllers;

import domain.dtos.BundleDto;
import domain.entities.Bundle;
import domain.entities.Yard;
import helpers.Converter;
import javafx.geometry.Point2D;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class LarmanController implements Serializable {
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

    public Yard getYard() { return yard; }

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
        int index = getSelectedBundles(position).size()-1;
        if (index >= 0) {
            return getSelectedBundles(position).get(index);
        }
        return null;
    }

    public void modifyBundleProperties(String id, String barcode, double height, double width, double length, LocalTime time,
                             LocalDate date, String essence, String planksize, double angle)
    {
        yard.modifyBundleProperties(id, barcode, height, width, length, time, date, essence, planksize, angle);
    }

    public void modifyBundlePosition(String id, Point2D position)
    {
        yard.modifyBundlePosition(id, position);
    }

    public void deleteBundle(String id) {
        yard.deleteBundle(id);
    }

    /**** PRIVATE METHODS ****/

}