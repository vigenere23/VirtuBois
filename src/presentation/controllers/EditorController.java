package presentation.controllers;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import domain.entities.Bundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import presentation.presenters.YardPresenter;

import javax.swing.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static javax.xml.bind.DatatypeConverter.parseDouble;

public class EditorController extends BaseController {
    @FXML public TextField barcodeTextField;
    @FXML public TextField essenceTextField;
    @FXML public TextField dimensionTextField;
    @FXML public TextField lengthTextField;
    @FXML public Spinner<Double> angleSpinner;
    @FXML public Spinner<Double> widthSpinner;
    @FXML public Spinner<Double> heightSpinner;
    @FXML public DatePicker datePicker;
    @FXML public Spinner<Integer> hourSpinner;
    @FXML public Spinner<Integer> minuteSpinner;
    @FXML public Button modifyButton;
    @FXML public Button cancelledButton;

    private BundleDto bundleDto;
    private String length;

    public EditorController(){

    }

    public EditorController(BundleDto dto){

    }

    @FXML
    public void initialize(){


        SpinnerValueFactory.DoubleSpinnerValueFactory widthSpinnerValue = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 0.0);
        widthSpinner.setValueFactory(widthSpinnerValue);

        SpinnerValueFactory.DoubleSpinnerValueFactory heightSpinnerValue = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 0.0);
        heightSpinner.setValueFactory(heightSpinnerValue);

        SpinnerValueFactory.DoubleSpinnerValueFactory angleSpinnerValue = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 360.0, 0.0);
        angleSpinner.setValueFactory(angleSpinnerValue);

        SpinnerValueFactory.IntegerSpinnerValueFactory hourSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, 0);
        hourSpinner.setValueFactory(hourSpinnerValue);

        SpinnerValueFactory.IntegerSpinnerValueFactory minuteSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0);
        minuteSpinner.setValueFactory(minuteSpinnerValue);
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelledButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleModifyButtonAction(ActionEvent event) {
        //String message = validateInput();
        //if (message == ""){
            length = lengthTextField.getText();
            Stage stage = (Stage) modifyButton.getScene().getWindow();
            stage.close();
        //} else{
        //    System.out.print(message);
        //}
    }

    private String validateInput(){
        String message = "";
        try {

            double lenght = parseDouble(lengthTextField.getText());
            double angle = angleSpinner.getValue();
            double heigth = heightSpinner.getValue();
            double width = widthSpinner.getValue();

        } catch (NumberFormatException e){
            message = "La longueur, l'angle, la hauteur et la largeur doivent être des nombres réels.";
        }
        return message;
    }

    public void setBundleDto(BundleDto dto){

    }

    public String getBundleDto() {
        return length;
    }
}


