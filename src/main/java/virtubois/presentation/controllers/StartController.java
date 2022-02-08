package virtubois.presentation.controllers;

import virtubois.helpers.FileHelper;
import javafx.event.ActionEvent;

public class StartController extends BaseController {

    public void newFile(ActionEvent actionEvent) {
        FileHelper.newFile(stage);
    }

    public void openFile(ActionEvent actionEvent) {
        FileHelper.openFile(stage);
    }

}
