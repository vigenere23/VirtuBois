package glo2004.virtubois.context;

import glo2004.virtubois.helpers.filesaver.BaseFileWriter;
import glo2004.virtubois.helpers.filesaver.DialogSavingPrompt;
import glo2004.virtubois.helpers.filesaver.FileSaver;
import glo2004.virtubois.helpers.filesaver.FileWriter;
import javafx.stage.Stage;

import java.nio.file.Path;


public class FileSaverProvider {

    public static final String USER_DIR = System.getProperty("user.home");

    public final FileSaver saveYardFileSaver;
    public final FileSaver export3DFileSaver;

    public FileSaverProvider(Stage stage, SavingContext yardSavingContext) {
        FileWriter fileWriter = new BaseFileWriter();

        saveYardFileSaver = new FileSaver(
            yardSavingContext,
            new DialogSavingPrompt(
                yardSavingContext,
                stage,
                "Sauvegarder...",
                ".ser",
                "SER",
                Path.of(USER_DIR, "Yard.ser")
            ),
            fileWriter
        );

        SavingContext export3DSavingContext = new SavingContext();

        export3DFileSaver = new FileSaver(
            export3DSavingContext,
            new DialogSavingPrompt(
                export3DSavingContext,
                stage,
                "Exporter en 3D...",
                ".stl",
                "STL",
                Path.of(USER_DIR, "Yard.stl")
            ),
            fileWriter
        );
    }
}
