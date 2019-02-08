package frontend;

import helpers.JavafxHelper;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    JavafxHelper.loadView(stage, "Start", "VirtuboisEditor");
  }

  public static void main(String[] args) {
    launch(args);
  }
}