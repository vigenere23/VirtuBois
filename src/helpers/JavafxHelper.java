package helpers;

import frontend.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class JavafxHelper {
  public static void loadView(Stage stage, String viewName, String title) throws Exception {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("./views/" + viewName + ".fxml"));
    Pane page = loader.load();
    Scene scene = new Scene(page);

    stage.setTitle(title);
    stage.setScene(scene);
    stage.centerOnScreen();
    stage.show();
  }
}
