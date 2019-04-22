package helpers;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import domain.entities.Yard;
import enums.DialogAction;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FileHelper {

    private static final File DEFAULT_DIRECTORY = new File(System.getProperty("user.home"));

    private static final String YARD_FILE_DESCRIPTOR = "SER";
    private static final String YARD_EXTENSION = ".ser";
    private static final String YARD_DEFAULT_FILENAME = "Yard.ser";
    private static File lastFile = null;
    private static boolean savedOnce = false;

    private static final String STL_FILE_DESCRIPTOR = "STL";
    private static final String STL_EXTENSION = ".stl";
    private static final String STL_DEFAULT_FILENAME = "Yard.stl";
    private static File lastSTLFile = null;

    public static void newFile(Stage stage, Yard yard) {
        if (yard != null && yard.getBundles().size() != 0) {
            DialogAction result = FileHelper.popupConfirmationDialog(
                    "Enregistrer",
                    "Voulez-vous enregistrer avant de continuer?"
            );

            if (result == DialogAction.YES)
                saveFile(stage, yard);
            if (result == DialogAction.YES || result == DialogAction.NO) {
                LarmanController.getInstance().clearYard();
                newFile(stage);
            }
        }
        else {
            newFile(stage);
        }
    }

    public static void newFile(Stage stage) {
        savedOnce = false;
        JavafxHelper.loadView(stage, "Main", "Nouvelle Cour", true);
    }

    public static void openFile(Stage stage, Yard yard) {
        if (yard != null && yard.getBundles().size() != 0) {
            DialogAction result = FileHelper.popupConfirmationDialog(
                    "Enregistrer",
                    "Voulez-vous enregistrer avant de continuer?"
            );

            if (result == DialogAction.YES)
                saveFile(stage, yard);
            if (result == DialogAction.YES || result == DialogAction.NO) {
                openFile(stage);
            }
        }
        else {
            openFile(stage);
        }
    }

    public static void openFile(Stage stage) {
        FileChooser fileChooser = initFileChooser("Ouvrir", YARD_EXTENSION, YARD_FILE_DESCRIPTOR, YARD_DEFAULT_FILENAME, lastFile);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            FileInputStream fileInputStream;
            ObjectInputStream objectInputStream;
            try {
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);
                Yard yardInit = (Yard) objectInputStream.readObject();
                LarmanController.getInstance().setYard(yardInit);
                JavafxHelper.loadView(stage, "Main", file.getName(), true);
                lastFile = file;
                savedOnce = true;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveFile(Stage stage, Yard yard) {
        if (!savedOnce) {
            saveFileAs(stage, yard);
        }
        else {
            writeFile(yard, lastFile);
        }
    }

    private static void writeFile(Object object, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeTextFile(String text, File file) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                    Paths.get(file.getPath()),
                    Charset.forName("UTF-8"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
        )) {
            writer.write(text);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFileAs(Stage stage, Yard yard) {
        FileChooser fileChooser = initFileChooser("Enregistrer sous", YARD_EXTENSION, YARD_FILE_DESCRIPTOR, YARD_DEFAULT_FILENAME, lastFile);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            lastFile = ensureExtension(file, YARD_EXTENSION);
            savedOnce = true;
            saveFile(stage, yard);
        }
    }

    public static void saveSTLFile(Stage stage, List<BundleDto> bundleDtos) {
        String stl = STLCreator.generateSTL(bundleDtos);
        FileChooser fileChooser = initFileChooser("Exporter en 3D...", STL_EXTENSION, STL_FILE_DESCRIPTOR, STL_DEFAULT_FILENAME, lastSTLFile);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            lastSTLFile = ensureExtension(file, STL_EXTENSION);
            writeTextFile(stl, lastSTLFile);
        }
    }

    private static FileChooser initFileChooser(String title, String extension, String fileDescriptor, String defaultFilename, File lastFile) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileDescriptor + " files (*" + extension + ")", "*" + extension));
        fileChooser.setTitle(title);
        if (lastFile != null) {
            fileChooser.setInitialDirectory(lastFile.getParentFile());
            fileChooser.setInitialFileName(lastFile.getName());
        }
        else {
            fileChooser.setInitialDirectory(DEFAULT_DIRECTORY);
            fileChooser.setInitialFileName(defaultFilename);
        }
        return fileChooser;
    }

    private static File ensureExtension(File file, String extension) {
        if (!getExtension(file).equals(extension)) {
            file = new File(file.getPath() + extension);
        }
        return file;
    }

    private static String getExtension(File file) {
        String extension = "";

        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            extension = file.getName().substring(i);
        }
        return extension;
    }

    private static DialogAction popupConfirmationDialog(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);

        ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(cancelButton, noButton, yesButton);

        return parseDialogResults(alert.showAndWait());
    }

    private static DialogAction parseDialogResults(Optional<ButtonType> result) {
        if (result.isPresent()) {
            switch (result.get().getButtonData()) {
                case YES: return DialogAction.YES;
                case NO: return DialogAction.NO;
                default: return DialogAction.CANCEL;
            }
        }
        return DialogAction.CANCEL;
    }

}
