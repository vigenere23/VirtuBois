package glo2004.virtubois.presentation.controllers;

import glo2004.virtubois.domain.controllers.LarmanController;
import glo2004.virtubois.helpers.JavafxHelper;
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

    public void quit(ActionEvent actionEvent) {
        JavafxHelper.quitApplication();
    }

}
