package presentation.controllers;

import helpers.ConfigHelper;
import javafx.fxml.FXML;

import javafx.event.*;
import javafx.scene.control.*;

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

    public void handleGridModificationCancel(ActionEvent actionEvent) { stage.close(); }
}
