package frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class YardEditorController implements IController {

    private Stage stage;

    @FXML AnchorPane root;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
