package glo2004.virtubois.context;

import glo2004.virtubois.helpers.file.saver.BaseFileWriter;
import glo2004.virtubois.helpers.file.saver.DialogSavingPrompt;
import glo2004.virtubois.helpers.file.saver.FileSaver;
import javafx.stage.Stage;

import java.nio.file.Path;


public class FileSaverProvider {

    public static final String USER_DIR = System.getProperty("user.home");

    public FileSaver provideYardFileSaver(SavingContext yardSavingContext, Stage stage) {
        return new FileSaver(
            yardSavingContext,
            new DialogSavingPrompt(
                yardSavingContext,
                stage,
                "Sauvegarder...",
                ".ser",
                "SER",
                Path.of(USER_DIR, "Yard.ser")
            ),
            new BaseFileWriter()
        );
    }

    public FileSaver provideExport3DFileSaver(Stage stage) {
        SavingContext export3DSavingContext = new SavingContext();

        return new FileSaver(
            export3DSavingContext,
            new DialogSavingPrompt(
                export3DSavingContext,
                stage,
                "Exporter en 3D...",
                ".stl",
                "STL",
                Path.of(USER_DIR, "Yard.stl")
            ),
            new BaseFileWriter()
        );
    }
}
