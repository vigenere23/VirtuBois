package glo2004.virtubois.context;

import java.nio.file.Path;
import java.util.Optional;

public class SavingContext {

    private Path lastSavingPath;

    public void setLastSavingPath(Path lastSavingPath) {
        this.lastSavingPath = lastSavingPath;
    }

    public Optional<Path> getLastSavingPath() {
        return Optional.ofNullable(lastSavingPath);
    }
}
