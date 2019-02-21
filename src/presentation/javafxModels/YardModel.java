package presentation.javafxModels;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class YardModel extends Pane implements IModel {
    private List<PackModel> packModels;
    private ChargerModel chargerModel;

    public YardModel() {
        super();
        packModels = new ArrayList<>();
    }

    public void draw() {

    }
}
