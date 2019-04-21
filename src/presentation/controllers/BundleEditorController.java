package presentation.controllers;

import domain.dtos.BundleDto;
import helpers.JavafxHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalTime;

public class BundleEditorController extends BaseController {
    @FXML
    public TextField barcodeTextField;
    @FXML
    public TextField essenceTextField;
    @FXML
    public TextField plankSizeTextField1;
    @FXML
    public TextField plankSizeTextField2;
    @FXML
    public TextField lengthTextField;
    @FXML
    public TextField widthTextField;
    @FXML
    public TextField heightTextField;
    @FXML
    public TextField angleTextField;
    @FXML
    public DatePicker datePicker;
    @FXML
    public Spinner<Integer> hourSpinner;
    @FXML
    public Spinner<Integer> minuteSpinner;
    @FXML
    public Button applyModificationButton;
    @FXML
    public Button cancelModificationButton;

    private BundleDto bundleDto;

    @FXML
    public void initialize() {
        JavafxHelper.addStringToIntegerConverter(plankSizeTextField1, 1, 1, null);
        JavafxHelper.addStringToIntegerConverter(plankSizeTextField2, 1, 1, null);
        JavafxHelper.addStringToDoubleConverter(lengthTextField, 1.0, 0.0, null);
        JavafxHelper.addStringToDoubleConverter(widthTextField, 1.0, 0.0, null);
        JavafxHelper.addStringToDoubleConverter(heightTextField, 1.0, 0.0, null);
        JavafxHelper.addStringToDoubleConverter(angleTextField, 0.0, 0.0, 359.99);
        SpinnerValueFactory.IntegerSpinnerValueFactory hourSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        hourSpinner.setValueFactory(hourSpinnerValue);
        SpinnerValueFactory.IntegerSpinnerValueFactory minuteSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minuteSpinner.setValueFactory(minuteSpinnerValue);
    }

    public void setBundleDto(BundleDto dto) {
        this.bundleDto = dto;
        setInputValues();
    }

    private void setInputValues() {
        barcodeTextField.setText(bundleDto.barcode);
        essenceTextField.setText(bundleDto.essence);
        plankSizeTextField1.setText(bundleDto.plankSize.substring(0, 1));
        plankSizeTextField2.setText(bundleDto.plankSize.substring(2));
        lengthTextField.setText(String.valueOf(bundleDto.length));
        widthTextField.setText(String.valueOf(bundleDto.width));
        heightTextField.setText(String.valueOf(bundleDto.height));
        angleTextField.setText(String.valueOf(bundleDto.angle));
        hourSpinner.getValueFactory().setValue(bundleDto.time.getHour());
        minuteSpinner.getValueFactory().setValue(bundleDto.time.getMinute());
        datePicker.setValue(bundleDto.date);
    }

    @FXML
    public void handleCancelModificationButton(ActionEvent event) {
        stage.close();
    }

    @FXML
    public void handleApplyModificationButton(ActionEvent event) {
        setDtoValues();
        stage.close();
    }

    private void setDtoValues() {
        bundleDto.plankSize = plankSizeTextField1.getText() + "x" + plankSizeTextField2.getText();
        bundleDto.barcode = barcodeTextField.getText();
        bundleDto.essence = essenceTextField.getText();
        bundleDto.length = Double.parseDouble(lengthTextField.getText());
        bundleDto.angle = Double.parseDouble(angleTextField.getText());
        bundleDto.height = Double.parseDouble(heightTextField.getText());
        bundleDto.width = Double.parseDouble(widthTextField.getText());
        bundleDto.time = LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue());
        bundleDto.date = datePicker.getValue();
    }
}
