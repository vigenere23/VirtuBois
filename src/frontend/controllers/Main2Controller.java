package frontend.controllers;

import helpers.JavafxHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class Main2Controller implements IController {

    private Stage stage;

    @FXML
    AnchorPane root;

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