package glo2004.virtubois.presentation.display.fileprompt;

import java.nio.file.Path;
import java.util.Optional;

public interface FilePrompt {
    Optional<Path> promptPath();
}
