package glo2004.virtubois.helpers.file.opener;

import java.nio.file.Path;
import java.util.Optional;

public interface OpeningPrompt {
    Optional<Path> promptOpeningPath();
}
