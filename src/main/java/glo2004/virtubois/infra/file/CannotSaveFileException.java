package glo2004.virtubois.infra.file;

public class CannotSaveFileException extends RuntimeException {
    public CannotSaveFileException(String message) {
        super(message);
    }
}
