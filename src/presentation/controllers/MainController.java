package presentation.controllers;

import domain.controllers.Controller;
import javafx.stage.Stage;

public class MainController extends BaseController implements IController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
