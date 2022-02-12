package glo2004.virtubois;

import javafx.application.Application;
import javafx.stage.Stage;
import glo2004.virtubois.helpers.JavafxHelper;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        JavafxHelper.loadView(stage, "Start", "Démarrer un projet", false);
    }

    public static void main(String[] args) {
        launch();
    }
}
