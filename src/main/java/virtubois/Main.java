package virtubois;

import javafx.application.Application;
import javafx.stage.Stage;
import virtubois.helpers.JavafxHelper;

public class Main extends Application {

  @Override
  public void start(Stage stage) {
    JavafxHelper.loadView(stage, "Start", "Démarrer un projet", false);
  }

  public static void main(String ...args) {
    launch(args);
  }
}
