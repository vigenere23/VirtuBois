package frontend.controllers;

import helpers.JavafxHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartController implements IController {

    private Stage stage;

    @FXML AnchorPane root;

    public void setStage(Stage stage) { this.stage = stage; }

    public void newFile(ActionEvent actionEvent) {
        String filename = "NouvelleCour";
        // TODO demander nom fichier / cour
        JavafxHelper.loadView(this.stage, "Main2", filename, true);
    }

    public void openFile(ActionEvent actionEvent) {
    }

    public void quit(ActionEvent actionEvent) {
        JavafxHelper.quitApplication();
    }
}
