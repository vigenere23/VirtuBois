package presentation.javafxModels;

import java.util.ArrayList;
import java.util.List;

public class YardModel implements IModel {
    private List<PackModel> packModels;
    private ChargerModel chargerModel;

    public YardModel() {
        packModels = new ArrayList<>();
    }

    public void draw() {

    }
}
