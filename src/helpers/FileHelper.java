package helpers;

import domain.controllers.LarmanController;
import domain.entities.Yard;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class FileHelper {

    public static File lastPath = null;

    public static void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SER", "*.ser"));
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(stage);
        lastPath = file;
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            Yard yardInit = (Yard) objectInputStream.readObject();
            LarmanController.getInstance().setYard(yardInit);
            JavafxHelper.loadView(stage, "Main", file.getName(), true);

        } catch (IOException | ClassNotFoundException ex){
            System.out.println(ex);
        }
    }

    public static void saveFile(Stage stage, Yard yard) {
        if (lastPath == null) {
            saveFileAs(stage, yard);
        }
        else {
            FileOutputStream fileOutputStream;
            ObjectOutputStream objectOutputStream;
            try {
                fileOutputStream = new FileOutputStream(lastPath);
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SER", "*.ser"));
        fileChooser.setTitle("Save As");
        lastPath = fileChooser.showSaveDialog(stage);
        saveFile(stage, yard);
    }

}
