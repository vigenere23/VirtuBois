package presentation.controllers;

import domain.controllers.LarmanController;
import domain.entities.Yard;
import helpers.FileHelper;
import helpers.JavafxHelper;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public abstract class BaseController implements IController {

    protected Stage stage;
    public LarmanController larmanController;

    public BaseController() {
        larmanController = LarmanController.getInstance();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void newFile(ActionEvent actionEvent) {
        larmanController.setYard(new Yard());
        FileHelper.newFile(stage);
    }

    public void newFileMenu(ActionEvent actionEvent) {
        if (larmanController.getYard().getBundles().size() != 0) {
            save(actionEvent);
        }
        newFile(actionEvent);
    }

    public void openFile(ActionEvent actionEvent) {
        FileHelper.openFile(stage);
    }

    public void saveAs(ActionEvent actionEvent) {
        FileHelper.saveFileAs(stage, larmanController.getYard());
    }

    public void save(ActionEvent actionEvent) {
        FileHelper.saveFile(stage, larmanController.getYard());
    }

    public void quit(ActionEvent actionEvent) {
        JavafxHelper.quitApplication();
    }

    public void handleMenuHelpAbout(ActionEvent actionEvent) {
        JavafxHelper.popupView("About", "À propos", false, false);
    }

}
