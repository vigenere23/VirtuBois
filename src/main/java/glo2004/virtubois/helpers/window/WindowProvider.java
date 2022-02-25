package glo2004.virtubois.helpers.window;

public class WindowProvider {

    private static final WindowProvider instance = new WindowProvider();

    private Window window;

    private WindowProvider() {
    }

    public static WindowProvider getInstance() {
        return instance;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public Window getWindow() {
        return window;
    }
}
