package presentation.controllers;

import domain.controllers.LarmanController;
import helpers.JavafxHelper;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

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
        JavafxHelper.loadView(this.stage, "Main", "Nouvelle Cour", true);
    }

    public void openFile(ActionEvent actionEvent) {
    }

    public void saveAs(ActionEvent actionEvent){

    }

    public void save(ActionEvent actionEvent){

    }

    public void quit(ActionEvent actionEvent) {
        JavafxHelper.quitApplication();
    }

    public void handleMenuHelpAbout(ActionEvent actionEvent) {
        JavafxHelper.popupView("About", "À propos", false, false);
    }

}
