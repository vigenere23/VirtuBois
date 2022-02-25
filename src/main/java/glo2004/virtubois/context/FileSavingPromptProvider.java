package glo2004.virtubois.context;

import glo2004.virtubois.presentation.display.DialogSavingPrompt;
import glo2004.virtubois.presentation.display.fileprompt.FilePrompt;
import glo2004.virtubois.presentation.display.fileprompt.UseLastPathFilePrompt;
import javafx.stage.Stage;

import java.nio.file.Path;


public class FileSavingPromptProvider {

    public static final String USER_DIR = System.getProperty("user.home");

    public FilePrompt provideSaveYardFilePrompt(SavingContext yardSavingContext, Stage stage) {
        DialogSavingPrompt dialogPrompt = new DialogSavingPrompt(
            yardSavingContext,
            stage,
            "Enregistrer...",
            ".ser",
            "SER",
            Path.of(USER_DIR, "Yard.ser")
        );

        return new UseLastPathFilePrompt(yardSavingContext, dialogPrompt);
    }

    public FilePrompt provideSaveYardAsFilePrompt(SavingContext yardSavingContext, Stage stage) {
        return new DialogSavingPrompt(
            yardSavingContext,
            stage,
            "Enregistrer sous...",
            ".ser",
            "SER",
            Path.of(USER_DIR, "Yard.ser")
        );
    }

    public FilePrompt provideExport3DFilePrompt(SavingContext export3DSavingContext, Stage stage) {
        DialogSavingPrompt dialogPrompt = new DialogSavingPrompt(
            export3DSavingContext,
            stage,
            "Exporter en 3D...",
            ".stl",
            "STL",
            Path.of(USER_DIR, "Yard.stl")
        );

        return new UseLastPathFilePrompt(export3DSavingContext, dialogPrompt);
    }
}
