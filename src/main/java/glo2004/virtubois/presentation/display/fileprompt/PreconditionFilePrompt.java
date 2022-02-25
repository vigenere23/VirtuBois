package glo2004.virtubois.presentation.display.fileprompt;

import java.nio.file.Path;
import java.util.Optional;

public class PreconditionFilePrompt implements FilePrompt {

    private final FilePromptPrecondition precondition;
    private final FilePrompt filePrompt;

    public PreconditionFilePrompt(FilePromptPrecondition precondition, FilePrompt filePrompt) {
        this.precondition = precondition;
        this.filePrompt = filePrompt;
    }

    @Override
    public Optional<Path> promptPath() {
        try {
            precondition.execute();
        } catch (PreconditionException e) {
            return Optional.empty();
        }

        return filePrompt.promptPath();
    }
}
