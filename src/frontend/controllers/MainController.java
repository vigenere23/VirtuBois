package frontend.controllers;

import helpers.JavafxHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController implements IController {

    private Stage stage;

    @FXML AnchorPane root;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void handleMenuFileExit(ActionEvent actionEvent) {
        // TODO ask for saving
        JavafxHelper.quitApplication();
    }

    public void handleMenuHelpAbout(ActionEvent actionEvent) {
        JavafxHelper.addView("About", "About", false);
    }
}