package presentation.controllers;

import helpers.JavafxHelper;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class BaseController implements IController {

    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void newFile(ActionEvent actionEvent) {
        String filename = "NouvelleCour";
        JavafxHelper.loadView(this.stage, "Main", filename, true);
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
        JavafxHelper.addView("About", "About", false);
    }

}
