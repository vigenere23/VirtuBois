package glo2004.virtubois.helpers.file.saver;

import java.nio.file.Path;
import java.util.Optional;

public interface SavingPrompt {
    Optional<Path> promptSavingPath();
}
