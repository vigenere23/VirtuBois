package glo2004.virtubois.helpers.file.opener;

import glo2004.virtubois.context.SavingContext;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public class FileOpener {

    private final SavingContext savingContext;
    private final FileOpeningPrecondition fileOpeningPrecondition;
    private final OpeningPrompt openingPrompt;
    private final FileReader fileReader;

    public FileOpener(SavingContext savingContext, FileOpeningPrecondition fileOpeningPrecondition, OpeningPrompt openingPrompt, FileReader fileReader) {
        this.savingContext = savingContext;
        this.fileOpeningPrecondition = fileOpeningPrecondition;
        this.openingPrompt = openingPrompt;
        this.fileReader = fileReader;
    }

    public <T> Optional<T> open() {
        try {
            this.fileOpeningPrecondition.execute();
        } catch (Exception e) {
            return Optional.empty();
        }

        Optional<Path> openingPath = openingPrompt.promptOpeningPath();

        if (openingPath.isEmpty()) {
            return Optional.empty();
        }

        File openingFile = openingPath.get().toFile();
        T object = fileReader.read(openingFile);
        savingContext.setPath(openingPath.get());

        return Optional.of(object);
    }
}
