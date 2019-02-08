package helpers;

import frontend.Main;
import frontend.controllers.IController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavafxHelper {
  public static void loadView(Stage stage, String viewName, String title, boolean maximised) {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("./views/" + viewName + ".fxml"));
    Parent page;

    try {
      page = loader.load();
    }
    catch (Exception e) {
      System.out.println("Could not load view");
      e.printStackTrace();
      return;
    }

    Scene scene = new Scene(page);

    IController controller = loader.getController();
    controller.setStage(stage);

    stage.setTitle("Virtubois - " + title);
    stage.setScene(scene);
    stage.centerOnScreen();
    stage.setMaximized(maximised);
    stage.show();
  }

  public static void quitApplication() {
    Platform.exit();
  }
}
