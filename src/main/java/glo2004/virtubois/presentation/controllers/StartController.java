package glo2004.virtubois.presentation.controllers;

import glo2004.virtubois.context.FileOpeningPromptProvider;
import glo2004.virtubois.context.SavingContext;
import glo2004.virtubois.context.YardSavingContext;
import glo2004.virtubois.domain.controllers.LarmanController;
import glo2004.virtubois.helpers.FileHelper;
import glo2004.virtubois.helpers.JavafxHelper;
import glo2004.virtubois.helpers.window.ViewName;
import glo2004.virtubois.presentation.display.fileprompt.FilePrompt;
import javafx.event.ActionEvent;

import java.nio.file.Path;
import java.util.Optional;

public class StartController extends BaseController {

    private final SavingContext savingContext = YardSavingContext.getInstance();
    private final FilePrompt yardOpeningFilePrompt;

    public StartController() {
        FileOpeningPromptProvider fileOpeningPromptProvider = new FileOpeningPromptProvider();
        yardOpeningFilePrompt = fileOpeningPromptProvider.provideOpenYardFilePrompt(savingContext, stage, false);
    }

    public void newFile(ActionEvent actionEvent) {
        FileHelper.newFile(stage, false);
    }

    public void openFile(ActionEvent actionEvent) {
        Optional<Path> path = yardOpeningFilePrompt.promptPath();

        if (path.isEmpty()) {
            return;
        }

        LarmanController.getInstance().openYard(path.get());
        JavafxHelper.loadView(stage, ViewName.MAIN, savingContext.getFileName().orElse("New Yard"), false);
    }
}
