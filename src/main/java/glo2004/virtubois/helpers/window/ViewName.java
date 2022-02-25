package glo2004.virtubois.helpers.window;

public enum ViewName {
    START("Start"),
    MAIN("Main"),
    ABOUT("About"),
    GRID("Grid");

    private final String value;

    ViewName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
