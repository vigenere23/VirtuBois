package presentation.controllers;

import domain.controllers.LarmanController;
import helpers.FileHelper;
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
        JavafxHelper.loadView(stage, "Main", "Nouvelle Cour", true);
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

}
