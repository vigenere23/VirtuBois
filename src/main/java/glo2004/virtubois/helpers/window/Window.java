package glo2004.virtubois.helpers.window;

public interface Window {
    void loadView(ViewName viewName);

    void popupView(ViewName viewName);

    void showDialog(DialogType dialogType);
}
