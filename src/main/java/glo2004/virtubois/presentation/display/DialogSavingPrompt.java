package glo2004.virtubois.presentation.display;

import glo2004.virtubois.context.SavingContext;
import glo2004.virtubois.presentation.display.fileprompt.FilePrompt;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class DialogSavingPrompt extends DialogFilePrompt implements FilePrompt {

    public DialogSavingPrompt(SavingContext savingContext, Stage stage, String dialogTitle, String extension, String fileDescriptor, Path defaultPath) {
        super(savingContext, stage, dialogTitle, extension, fileDescriptor, defaultPath);
    }

    @Override
    public File getFile(FileChooser fileChooser) {
        return fileChooser.showSaveDialog(stage);
    }
}
