package glo2004.virtubois.helpers.file;

import glo2004.virtubois.context.SavingContext;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public abstract class DialogFilePrompt {

    protected final SavingContext savingContext;
    protected final Stage stage;
    protected final String dialogTitle;
    protected final String extension;
    protected final String fileDescriptor;
    protected final Path defaultPath;

    public DialogFilePrompt(SavingContext savingContext, Stage stage, String dialogTitle, String extension, String fileDescriptor, Path defaultPath) {
        this.savingContext = savingContext;
        this.stage = stage;
        this.dialogTitle = dialogTitle;
        this.extension = extension;
        this.fileDescriptor = fileDescriptor;
        this.defaultPath = defaultPath;
    }

    public abstract File getFile(FileChooser fileChooser);

    public Optional<Path> promptPath() {
        FileChooser fileChooser = initFileChooser();
        File file = getFile(fileChooser);

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
        Path initialPath = savingContext.getPath().orElse(defaultPath);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileDescriptor + " files (*" + extension + ")", "*" + extension));

        fileChooser.setTitle(dialogTitle);
        fileChooser.setInitialFileName(initialPath.getFileName().toString());
        fileChooser.setInitialDirectory(initialPath.getParent().toFile());

        return fileChooser;
    }
}
