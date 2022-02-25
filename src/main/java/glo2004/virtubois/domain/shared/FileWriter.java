package glo2004.virtubois.domain.shared;

import java.nio.file.Path;

public interface FileWriter {
    void write(Path path, String content);

    void write(Path path, Object content);
}
