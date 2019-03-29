package helpers;

import domain.controllers.LarmanController;
import domain.entities.Yard;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class FileHelper {

    private static File lastDirectory = new File(System.getProperty("user.home"));
    private static String lastFilename = "Yard.ser";
    private static File lastFile = null;

    public static void newFile(Stage stage) {
        JavafxHelper.loadView(stage, "Main", "Nouvelle Cour", true);
        lastFile = null;
    }

    public static void openFile(Stage stage) {
        FileChooser fileChooser = initFileChooser("Ouvrir");
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
                updateLastFile(file);
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex);
            }
        }
    }

    public static void saveFile(Stage stage, Yard yard) {
        if (lastFile == null) {
            saveFileAs(stage, yard);
        }
        else {
            FileOutputStream fileOutputStream;
            ObjectOutputStream objectOutputStream;
            try {
                fileOutputStream = new FileOutputStream(lastFile);
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(yard);
                objectOutputStream.flush();
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveFileAs(Stage stage, Yard yard) {
        FileChooser fileChooser = initFileChooser("Enregistrer sous");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            updateLastFile(file);
            saveFile(stage, yard);
        }
    }

    private static void updateLastFile(File file) {
        lastDirectory = file.getParentFile();
        lastFilename = file.getName();
        lastFile = file;
    }

    private static FileChooser initFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SER", "*.ser"));
        fileChooser.setTitle(title);
        if (lastFile != null) {
            fileChooser.setInitialDirectory(lastDirectory);
            fileChooser.setInitialFileName(lastFilename);
        }
        return fileChooser;
    }

}
