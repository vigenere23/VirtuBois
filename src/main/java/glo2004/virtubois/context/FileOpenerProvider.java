package glo2004.virtubois.context;

import glo2004.virtubois.helpers.file.opener.*;
import javafx.stage.Stage;

import java.nio.file.Path;

public class FileOpenerProvider {

    public static final String USER_DIR = System.getProperty("user.home");

    public FileOpener provideYardFileOpener(SavingContext yardSavingContext, Stage stage, boolean confirmationNeeded) {
        OpeningPrompt openingPrompt = new DialogOpeningPrompt(
            yardSavingContext,
            stage,
            "Ouvrir",
            ".ser",
            "SER",
            Path.of(USER_DIR, "Yard.ser") // TODO Try to remove Yard.ser
        );

        ConfirmationDialog confirmationDialog = new ConfirmationDialog(
            "Attention!",
            "Vous êtes sur le point de perdre vos changements non sauvegardés. Continuer?"
        );

        return new FileOpener(
            yardSavingContext,
            confirmationNeeded ? confirmationDialog : new EmptyPrecondition(),
            openingPrompt,
            new BaseFileReader()
        );
    }
}
