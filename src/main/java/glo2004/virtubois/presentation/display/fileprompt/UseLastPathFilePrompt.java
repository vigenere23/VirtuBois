package glo2004.virtubois.presentation.display.fileprompt;

import glo2004.virtubois.context.SavingContext;

import java.nio.file.Path;
import java.util.Optional;

public class UseLastPathFilePrompt implements FilePrompt {

    private final SavingContext savingContext;
    private final FilePrompt filePrompt;

    public UseLastPathFilePrompt(SavingContext savingContext, FilePrompt filePrompt) {
        this.savingContext = savingContext;
        this.filePrompt = filePrompt;
    }

    @Override
    public Optional<Path> promptPath() {
        if (savingContext.getPath().isPresent()) {
            return savingContext.getPath();
        }

        return filePrompt.promptPath();
    }
}
