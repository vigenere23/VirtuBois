package glo2004.virtubois.domain.shared;

import java.nio.file.Path;

public interface FileReader {
    <T> T read(Path path);
}
