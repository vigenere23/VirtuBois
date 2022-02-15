package glo2004.virtubois.helpers.filesaver;

import glo2004.virtubois.context.SavingContext;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public class DialogSavingPrompt implements SavingPrompt {

    private final SavingContext savingContext;
    private final Stage stage;
    private final String dialogTitle;
    private final String extension;
    private final String fileDescriptor;
    private final Path defaultPath;

    public DialogSavingPrompt(SavingContext savingContext, Stage stage, String dialogTitle, String extension, String fileDescriptor, Path defaultPath) {
        this.savingContext = savingContext;
        this.stage = stage;
        this.dialogTitle = dialogTitle;
        this.extension = extension;
        this.fileDescriptor = fileDescriptor;
        this.defaultPath = defaultPath;
    }

    @Override
    public Optional<Path> promptSavingPath() {
        FileChooser fileChooser = initFileChooser();
        File file = fileChooser.showSaveDialog(stage);

        if (file == null) {
            return Optional.empty();
        }

        file = ensureExtension(file);

        return Optional.of(file.toPath());
    }

    private File ensureExtension(File file) {
        if (!getExtension(file).equals(extension)) {
            file = new File(file.getPath() + extension);
        }
        return file;
    }

    private String getExtension(File file) {
        String extension = "";

        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            extension = file.getName().substring(i);
        }
        return extension;
    }

    private FileChooser initFileChooser() {
        Path initialPath = savingContext.getLastSavingPath().orElse(defaultPath);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileDescriptor + " files (*" + extension + ")", "*" + extension));

        fileChooser.setTitle(dialogTitle);
        fileChooser.setInitialFileName(initialPath.getFileName().toString());
        fileChooser.setInitialDirectory(initialPath.getParent().toFile());

        return fileChooser;
    }
}
