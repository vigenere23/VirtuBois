package glo2004.virtubois.helpers.file.opener;

import glo2004.virtubois.enums.DialogAction;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ConfirmationDialog implements FileOpeningPrecondition {

    private final String title;
    private final String message;

    public ConfirmationDialog(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public void execute() {
        Alert dialog = initDialog();
        Optional<ButtonType> result = dialog.showAndWait();
        DialogAction parsedResult = parseResult(result);

        if (!parsedResult.equals(DialogAction.YES)) {
            throw new PreconditionException("User did not accept.");
        }
    }

    private Alert initDialog() {
        Alert dialog = new Alert(Alert.AlertType.WARNING);
        dialog.setTitle(title);
        dialog.setHeaderText(message);

        ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.NO);
        // ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getButtonTypes().setAll(noButton, yesButton);

        return dialog;
    }

    private DialogAction parseResult(Optional<ButtonType> result) {
        if (result.isEmpty()) {
            return DialogAction.CANCEL;
        }

        switch (result.get().getButtonData()) {
            case YES:
                return DialogAction.YES;
            case NO:
                return DialogAction.NO;
            default:
                return DialogAction.CANCEL;
        }
    }
}
