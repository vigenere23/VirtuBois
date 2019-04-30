package presentation;

import helpers.JavafxHelper;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage stage) {
    JavafxHelper.loadView(stage, "Start", "Démarrer un projet", false);
  }

  public static void main(String ...args) {
    launch(args);
  }
}
