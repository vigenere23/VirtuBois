package glo2004.virtubois.helpers.window;

import javafx.stage.Stage;

public class JavaFxWindow implements Window {

    private final Stage stage;

    public JavaFxWindow(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void loadView(ViewName viewName) {

    }

    @Override
    public void popupView(ViewName viewName) {

    }

    @Override
    public void showDialog(DialogType dialogType) {

    }
}
