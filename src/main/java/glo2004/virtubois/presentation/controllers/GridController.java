package glo2004.virtubois.presentation.controllers;

import glo2004.virtubois.helpers.ConfigHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class GridController extends BaseController {
    @FXML
    public TextField gridTextField;
    @FXML
    public Button applyGridButton;
    @FXML
    public Button cancelGridButton;

    public void handleModificationGridApply(ActionEvent actionEvent) {
        ConfigHelper.gridSquareSize = Integer.parseInt(gridTextField.getText());
        stage.close();
    }

    public void handleGridModificationCancel(ActionEvent actionEvent) {
        stage.close();
    }
}
