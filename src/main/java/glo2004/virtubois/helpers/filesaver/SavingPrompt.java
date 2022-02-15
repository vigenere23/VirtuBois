package glo2004.virtubois.helpers.filesaver;

import java.nio.file.Path;
import java.util.Optional;

public interface SavingPrompt {
    Optional<Path> promptSavingPath();
}
