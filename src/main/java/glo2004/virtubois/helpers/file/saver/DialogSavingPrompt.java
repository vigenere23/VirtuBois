package glo2004.virtubois.helpers.file.saver;

import glo2004.virtubois.context.SavingContext;
import glo2004.virtubois.helpers.file.DialogFilePrompt;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public class DialogSavingPrompt extends DialogFilePrompt implements SavingPrompt {

    public DialogSavingPrompt(SavingContext savingContext, Stage stage, String dialogTitle, String extension, String fileDescriptor, Path defaultPath) {
        super(savingContext, stage, dialogTitle, extension, fileDescriptor, defaultPath);
    }

    @Override
    public Optional<Path> promptSavingPath() {
        return super.promptPath();
    }

    @Override
    public File getFile(FileChooser fileChooser) {
        return fileChooser.showSaveDialog(stage);
    }
}
