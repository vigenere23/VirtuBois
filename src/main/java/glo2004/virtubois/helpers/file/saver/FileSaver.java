package glo2004.virtubois.helpers.file.saver;

import glo2004.virtubois.context.SavingContext;

import java.nio.file.Path;
import java.util.Optional;

public class FileSaver {

    private final SavingContext savingContext;
    private final SavingPrompt savingPrompt;
    private final FileWriter fileWriter;

    public FileSaver(SavingContext savingContext, SavingPrompt savingPrompt, FileWriter fileWriter) {
        this.savingContext = savingContext;
        this.savingPrompt = savingPrompt;
        this.fileWriter = fileWriter;
    }

    public void save(String content, boolean prompt) {
        Optional<Path> savingPath = prompt || savingContext.getPath().isEmpty()
            ? savingPrompt.promptSavingPath()
            : savingContext.getPath();

        if (savingPath.isEmpty()) {
            return;
        }

        savingContext.setPath(savingPath.get());
        fileWriter.write(savingPath.get(), content);
    }

    public void save(Object content, boolean prompt) {
        Optional<Path> savingPath = prompt || savingContext.getPath().isEmpty()
            ? savingPrompt.promptSavingPath()
            : savingContext.getPath();

        if (savingPath.isEmpty()) {
            return;
        }

        savingContext.setPath(savingPath.get());
        fileWriter.write(savingPath.get(), content);
    }
}
