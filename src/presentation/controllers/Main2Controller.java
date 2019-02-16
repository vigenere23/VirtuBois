package presentation.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class Main2Controller extends BaseController implements IController {

    private Stage stage;

    @FXML
    public ListView listView;
    @FXML
    public TextFlow packProp;
    @FXML
    public TextFlow packPropValues;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize()
    {
        ObservableList<String> listItems = FXCollections.observableArrayList("Allo","Bonjour");
        listView.setItems(listItems);

        Text packInfo = new Text("Code barre : \nLongeur : \nLargeur : \nHauteur : \nDate de production : \nHeure de production : \nType : ");
        packInfo.setFont(new Font(15));
        packInfo.setFill(Color.WHITESMOKE);
        packProp.setLineSpacing(40.0);
        packProp.getChildren().add(packInfo);


    }
}
