package glo2004.virtubois.presentation.display.fileprompt;

import glo2004.virtubois.enums.DialogAction;
import glo2004.virtubois.presentation.display.ConfirmationDialog;

public class ConfirmationFilePromptPrecondition implements FilePromptPrecondition {

    private final ConfirmationDialog confirmationDialog;

    public ConfirmationFilePromptPrecondition(ConfirmationDialog confirmationDialog) {
        this.confirmationDialog = confirmationDialog;
    }

    @Override
    public void execute() throws PreconditionException {
        DialogAction action = confirmationDialog.prompt();

        if (!action.equals(DialogAction.YES)) {
            throw new PreconditionException("User did not accept.");
        }
    }
}
