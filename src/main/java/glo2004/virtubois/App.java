package glo2004.virtubois;

import glo2004.virtubois.helpers.JavafxHelper;
import glo2004.virtubois.helpers.view.ViewName;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        JavafxHelper.loadView(stage, ViewName.START, "Démarrer un projet", false);
    }

    public static void main(String[] args) {
        launch();
    }
}
