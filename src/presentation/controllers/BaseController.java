package presentation.controllers;

import helpers.JavafxHelper;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class BaseController implements IController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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
}
