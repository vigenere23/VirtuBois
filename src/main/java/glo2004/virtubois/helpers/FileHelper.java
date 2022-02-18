package glo2004.virtubois.helpers;

import glo2004.virtubois.domain.controllers.LarmanController;
import glo2004.virtubois.helpers.file.opener.ConfirmationDialog;
import glo2004.virtubois.helpers.file.opener.PreconditionException;
import glo2004.virtubois.helpers.view.ViewName;
import javafx.stage.Stage;

public class FileHelper {

    public static void newFile(Stage stage, boolean warnUnsavedChanges) {
        if (warnUnsavedChanges) {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                "Attention!",
                "Vous êtes sur le point de perdre vos changements non sauvegardés. Continuer?"
            );

            try {
                confirmationDialog.execute();
            } catch (PreconditionException e) {
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
