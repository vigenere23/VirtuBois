package presentation.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class Main2Controller extends BaseController implements IController {

    private Stage stage;

    @FXML
    public ListView listView;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize()
    {
        ObservableList<String> listItems = FXCollections.observableArrayList("Allo","Bonjour");
        listView.setItems(listItems);
    }
}
