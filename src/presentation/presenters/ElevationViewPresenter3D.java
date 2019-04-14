package presentation.presenters;

import javafx.scene.SubScene;
import javafx.scene.layout.StackPane;

public class ElevationViewPresenter3D implements IPresenter {

    private SubScene scene;

    public ElevationViewPresenter3D(StackPane parent, SubScene scene){
        this.scene = scene;
        this.scene.heightProperty().bind(parent.heightProperty());
        this.scene.widthProperty().bind(parent.widthProperty());
    }

    @Override
    public void draw() {

    }
}
