package presentation.controllers;

import domain.controllers.LarmanController;
import domain.entities.Bundle;
import domain.entities.Yard;
import helpers.JavafxHelper;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Map;

public abstract class BaseController implements IController {

    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void newFile(ActionEvent actionEvent) {
        String filename = "NouvelleCour";
        JavafxHelper.loadView(this.stage, "Main", filename, true);
    }

    public void openFile(ActionEvent actionEvent) {
//        FileInputStream fileInputStream;
//        ObjectInputStream objectInputStream;
//        try {
//            fileInputStream = new FileInputStream("C:\\Users\\Yoan Chamberland\\h19-glo-equipe2\\src\\Save\\Cours1.ser");
//            objectInputStream = new ObjectInputStream(fileInputStream);
//            Yard yard = (Yard) objectInputStream.readObject();
//            JavafxHelper.loadView(new Stage(), "Main", "Cours1", true);
//            LarmanController.getInstance().setYard(yard);
//        } catch (IOException | ClassNotFoundException ex){
//            System.out.println(ex);
//        }
    }

    public void saveAs(ActionEvent actionEvent){
//        Yard yard;
//        yard = LarmanController.getInstance().getYard();
//        FileOutputStream fileOutputStream;
//        ObjectOutputStream objectOutputStream;
//        try{
//            fileOutputStream = new FileOutputStream("C:\\Users\\Yoan Chamberland\\h19-glo-equipe2\\src\\Save\\Cours1.ser");
//            objectOutputStream = new ObjectOutputStream(fileOutputStream);
//            objectOutputStream.writeObject(yard);
//            objectOutputStream.close();
//        } catch (IOException ex){
//            System.out.println(ex);
//        }
    }

    public void save(ActionEvent actionEvent){

    }

    public void quit(ActionEvent actionEvent) {
        JavafxHelper.quitApplication();
    }

    public void handleMenuHelpAbout(ActionEvent actionEvent) {
        JavafxHelper.addView("About", "About", false);
    }

}
