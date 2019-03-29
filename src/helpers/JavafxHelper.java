package helpers;

import domain.dtos.BundleDto;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import presentation.controllers.BundleEditorController;
import presentation.controllers.IController;
import presentation.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class JavafxHelper {

    public static void loadView(Stage stage, String viewName, String title, boolean maximised) {
        setupScene(stage, viewName);
        setupStage(stage, title, maximised, false);
    }

    private static IController setupScene(Stage stage, String viewName) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/" + viewName + ".fxml"));
        Parent page;

        try {
            page = loader.load();
        }
        catch (Exception e) {
            System.out.println("Could not load view");
            e.printStackTrace();
            return null;
        }

        IController controller = loader.getController();
        if (controller != null) {
            controller.setStage(stage);
        }

        Scene scene = new Scene(page);
        stage.setScene(scene);

        return controller;
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

    public static void popupView(String viewName, String title, boolean maximised, boolean waitForClosing) {
        Stage stage = new Stage();
        setupScene(stage, viewName);
        setupStage(stage, title, maximised, waitForClosing);
    }

    public static void quitApplication() {
        Platform.exit();
    }

    public static void popupBundleEditorView(BundleDto bundleToEditDto) {
        Stage stage = new Stage();
        IController controller = setupScene(stage, "BundleEditor");
        if (controller instanceof BundleEditorController) {
            ((BundleEditorController) controller).setBundleDto(bundleToEditDto);
        }
        setupStage(stage, "Éditer un paquet", false, true);
    }

    public static void addStringToDoubleConverter(TextField textField, Double defaultValue, Double min, Double max) {
        ChangeListener<String> stringToDoubleConverter = (observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if(!newValue.equals("-") && !newValue.equals("-.") && !newValue.equals("."))
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
