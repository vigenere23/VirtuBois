package glo2004.virtubois.context;

import java.nio.file.Path;
import java.util.Optional;

public class SavingContext {

    private Path path;

    public void setPath(Path lastSavingPath) {
        this.path = lastSavingPath;
    }

    public Optional<Path> getPath() {
        return Optional.ofNullable(path);
    }

    public Optional<String> getFileName() {
        if (path == null) {
            return Optional.empty();
        }

        return Optional.of(path.getFileName().toString());
    }
}
