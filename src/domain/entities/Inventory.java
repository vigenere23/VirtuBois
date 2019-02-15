package domain.entities;

import javafx.geometry.Point2D;

import java.util.List;

public class Inventory {
    private static List<Pack> packs;

    public static void addPack(Pack pack) {
        if (!packs.contains(pack)) {
            packs.add(pack);
        }
    }

    public static void removePack(Pack pack) {
        packs.remove(pack);
    }

    public Pack getFirstPackAtPosition(Point2D position) {
        // TODO use GeomHelper
        return null;
    }

    public List<Pack> getAllPacksAtPosition(Point2D position) {
        // TODO use GeomHelper
        return null;
    }
}
