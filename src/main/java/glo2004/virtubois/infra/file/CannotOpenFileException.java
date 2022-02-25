package glo2004.virtubois.infra.file;

public class CannotOpenFileException extends RuntimeException {
    public CannotOpenFileException(String message) {
        super(message);
    }
}
