package glo2004.virtubois.helpers;

import glo2004.virtubois.domain.controllers.LarmanController;
import glo2004.virtubois.enums.DialogAction;
import glo2004.virtubois.helpers.window.ViewName;
import glo2004.virtubois.presentation.display.ConfirmationDialog;
import javafx.stage.Stage;

public class FileHelper {

    public static void newFile(Stage stage, boolean warnUnsavedChanges) {
        if (warnUnsavedChanges) {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                "Attention!",
                "Vous êtes sur le point de perdre vos changements non sauvegardés. Continuer?"
            );

            DialogAction action = confirmationDialog.prompt();

            if (!action.equals(DialogAction.YES)) {
                return;
            }
        }

        LarmanController.getInstance().clearYard();
        newFile(stage);
    }

    private static void newFile(Stage stage) {
        JavafxHelper.loadView(stage, ViewName.MAIN, "Nouvelle Cour", false);
        UndoRedo.getUndo().clear();
        UndoRedo.getRedo().clear();
    }
}
