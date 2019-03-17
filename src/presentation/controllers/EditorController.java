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
    @FXML public TextField widthTextField;
    @FXML public TextField heightTextField;
    @FXML public TextField angleTextField;
    @FXML public DatePicker datePicker;
    @FXML public Spinner<Integer> hourSpinner;
    @FXML public Spinner<Integer> minuteSpinner;
    @FXML public Button modifyButton;
    @FXML public Button cancelledButton;

    private BundleDto bundleDto;
    private String length;

    public EditorController(){
    }

    @FXML
    public void initialize(){

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
        bundleDto.plankSize = dimensionTextField.getText();
        Stage stage = (Stage) modifyButton.getScene().getWindow();
        stage.close();
        //} else{
        //    System.out.print(message);
        //}
    }

    private String validateInput(){
        String message = "";
        if (barcodeTextField.getText().isEmpty()){
            message = "Le code barre ne doit pas être vide";
        }
        if (essenceTextField.getText().isEmpty()){
            message = "L'essence ne doit pas être vide";
        }
        if (dimensionTextField.getText().isEmpty()){
            message = "L'essence ne doit pas être vide";
        }
        try {
            double length = parseDouble(lengthTextField.getText());
            double angle = parseDouble(angleTextField.getText());
            double height = parseDouble(heightTextField.getText());
            double width = parseDouble(widthTextField.getText());

        } catch (NumberFormatException e){
            message = "La longueur, l'angle, la hauteur et la largeur doivent être des nombres réels.";
        }
        return message;
    }

    public void setBundleDto(BundleDto dto){
        this.bundleDto = dto;
    }

    public BundleDto getBundleDto() {
        return bundleDto;
    }
}
