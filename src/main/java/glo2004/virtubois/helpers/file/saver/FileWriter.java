package glo2004.virtubois.helpers.file.saver;

import java.nio.file.Path;

public interface FileWriter {
    void write(Path path, String content);

    void write(Path path, Object content);
}