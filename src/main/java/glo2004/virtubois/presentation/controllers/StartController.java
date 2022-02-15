package glo2004.virtubois.presentation.controllers;

import glo2004.virtubois.context.FileOpenerProvider;
import glo2004.virtubois.context.SavingContext;
import glo2004.virtubois.context.YardSavingContext;
import glo2004.virtubois.domain.controllers.LarmanController;
import glo2004.virtubois.domain.entities.Yard;
import glo2004.virtubois.helpers.FileHelper;
import glo2004.virtubois.helpers.JavafxHelper;
import glo2004.virtubois.helpers.file.opener.FileOpener;
import javafx.event.ActionEvent;

import java.util.Optional;

public class StartController extends BaseController {

    private final SavingContext savingContext = YardSavingContext.getInstance();
    private final FileOpenerProvider fileOpenerProvider = new FileOpenerProvider();
    private final FileOpener yardFileOpener;

    public StartController() {
        SavingContext savingContext = YardSavingContext.getInstance();
        FileOpenerProvider fileOpenerProvider = new FileOpenerProvider();

        yardFileOpener = fileOpenerProvider.provideYardFileOpener(savingContext, stage, false);
    }

    public void newFile(ActionEvent actionEvent) {
        FileHelper.newFile(stage, false);
    }

    public void openFile(ActionEvent actionEvent) {
        // FileHelper.openFile(stage);
        Optional<Yard> openedYard = yardFileOpener.open();

        if (openedYard.isEmpty()) {
            return;
        }

        LarmanController.getInstance().setYard(openedYard.get());
        JavafxHelper.loadView(stage, "Main", savingContext.getFileName().orElse("New Yard"), false);
    }
}
