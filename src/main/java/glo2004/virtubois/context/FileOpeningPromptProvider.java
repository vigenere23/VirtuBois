package glo2004.virtubois.context;

import glo2004.virtubois.presentation.display.ConfirmationDialog;
import glo2004.virtubois.presentation.display.DialogOpeningPrompt;
import glo2004.virtubois.presentation.display.fileprompt.ConfirmationFilePromptPrecondition;
import glo2004.virtubois.presentation.display.fileprompt.FilePrompt;
import glo2004.virtubois.presentation.display.fileprompt.FilePromptPrecondition;
import glo2004.virtubois.presentation.display.fileprompt.PreconditionFilePrompt;
import javafx.stage.Stage;

import java.nio.file.Path;

public class FileOpeningPromptProvider {

    public static final String USER_DIR = System.getProperty("user.home");

    public FilePrompt provideOpenYardFilePrompt(SavingContext yardSavingContext, Stage stage, boolean confirmationNeeded) {
        FilePrompt openingPrompt = new DialogOpeningPrompt(
            yardSavingContext,
            stage,
            "Ouvrir",
            ".ser",
            "SER",
            Path.of(USER_DIR, "Yard.ser") // TODO Try to remove Yard.ser
        );

        if (confirmationNeeded) {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                "Attention!",
                "Vous êtes sur le point de perdre vos changements non sauvegardés. Continuer?"
            );
            FilePromptPrecondition precondition = new ConfirmationFilePromptPrecondition(confirmationDialog);

            return new PreconditionFilePrompt(precondition, openingPrompt);
        } else {
            return openingPrompt;
        }
    }
}
