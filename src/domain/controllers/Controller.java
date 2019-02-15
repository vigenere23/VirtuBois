package domain.controllers;

import helpers.JavafxHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller {

    /**** MEMBER VARIABLES ****/

    private Stage stage;

    /**** FXML VARIABLES ****/

    @FXML AnchorPane root;

    /**** PUBLIC METHODS ****/

    // GETTERS / SETTERS
    public void setStage(Stage stage) { this.stage = stage; }

    // MENU / FILES
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

    public void handleMenuHelpAbout(ActionEvent actionEvent) {
        JavafxHelper.addView("About", "About", false);
    }

    /**** PRIVATE METHODS ****/

}