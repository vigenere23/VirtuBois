package virtubois.presentation.controllers;

import virtubois.domain.controllers.LarmanController;
import virtubois.helpers.JavafxHelper;
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
