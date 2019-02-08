package frontend.controllers;

import helpers.JavafxHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartController implements IController {

    private Stage stage;

    @FXML AnchorPane root;

    public void setStage(Stage stage) {
        this.stage = stage;
        System.out.println(stage);
    }

    public void handleButton(ActionEvent actionEvent) {
        try {
            JavafxHelper.loadView(this.stage, "Main", "VirtuBois - Editor");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
