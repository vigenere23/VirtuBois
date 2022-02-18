package glo2004.virtubois.helpers;

import glo2004.virtubois.Main;
import glo2004.virtubois.helpers.view.ViewName;
import glo2004.virtubois.presentation.controllers.IController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class JavafxHelper {

    public static void loadView(Stage stage, ViewName viewName, String title, boolean maximised) {
        setupScene(stage, viewName);
        setupStage(stage, title, maximised, false);
    }

    private static void setupScene(Stage stage, ViewName viewName) {
        URL viewPath = Main.class.getResource("/glo2004/virtubois/presentation/views/" + viewName.getValue() + ".fxml");
        FXMLLoader loader = new FXMLLoader(viewPath);

        try {
            Parent page = loader.load();
            Scene scene = new Scene(page);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Could not load scene %s", viewName));
        }

        IController controller = loader.getController();

        if (controller != null) {
            controller.setStage(stage);
        }
    }

    private static void setupStage(Stage stage, String title, boolean maximised, boolean waitForClosing) {
        stage.setTitle("Virtubois - " + title);
        stage.centerOnScreen();
        stage.setMaximized(maximised);

        if (waitForClosing) {
            stage.showAndWait();
        } else {
            stage.show();
        }
    }

    public static void popupView(ViewName viewName, String title, boolean maximised, boolean waitForClosing) {
        Stage stage = new Stage();
        setupScene(stage, viewName);
        setupStage(stage, title, maximised, waitForClosing);
    }

    public static void quitApplication() {
        Platform.exit();
    }

    public static void popupGrid() {
        Stage stage = new Stage();
        setupScene(stage, ViewName.GRID);
        setupStage(stage, "Éditer la grille", false, true);
    }

    public static void addStringToDoubleConverter(TextField textField, Double defaultValue, Double min, Double max) {
        ChangeListener<String> stringToDoubleConverter = (observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!newValue.equals("-") && !newValue.equals("-.") && !newValue.equals("."))
                    try {
                        double value = Double.parseDouble(newValue);
                        if (min != null && value < min || max != null && value > max)
                            textField.setText(oldValue);
                    } catch (Exception e) {
                        textField.setText(oldValue);
                    }
            }
        };
        addChangeAndFocusListeners(textField, stringToDoubleConverter, defaultValue);
    }

    public static void addStringToIntegerConverter(TextField textField, Integer defaultValue, Integer min, Integer max) {
        ChangeListener<String> stringToIntegerConverter = (observable, oldValue, newValue) -> {

            if (!newValue.isEmpty()) {
                try {
                    int value = Integer.parseInt(newValue);
                    if (min != null && value < min || max != null && value > max)
                        textField.setText(oldValue);
                } catch (Exception e) {
                    textField.setText(oldValue);
                }
            }
        };
        addChangeAndFocusListeners(textField, stringToIntegerConverter, defaultValue);
    }

    private static void addChangeAndFocusListeners(TextField textField, ChangeListener<String> stringConverter, Number defaultValue) {
        textField.textProperty().addListener(stringConverter);
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.textProperty().getValue().isEmpty() && !newValue) {
                textField.setText(String.valueOf(defaultValue));
            }
        });
    }
}
