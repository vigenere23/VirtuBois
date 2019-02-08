package helpers;

import frontend.Main;
import frontend.controllers.IController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavafxHelper {
  public static void loadView(Stage stage, String viewName, String title) throws Exception {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("./views/" + viewName + ".fxml"));
    Parent page = loader.load();
    Scene scene = new Scene(page);

    IController controller = loader.getController();
    controller.setStage(stage);

    stage.setTitle(title);
    stage.setScene(scene);
    stage.centerOnScreen();
    stage.show();
  }
}
