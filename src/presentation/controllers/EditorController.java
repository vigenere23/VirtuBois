package presentation.controllers;

import domain.dtos.BundleDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.util.StringConverter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;

import static javax.xml.bind.DatatypeConverter.parseDouble;

public class EditorController extends BaseController {
    @FXML public TextField barcodeTextField;
    @FXML public TextField essenceTextField;
    @FXML public TextField plankSizeTextField1;
    @FXML public TextField plankSizeTextField2;
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

    public EditorController(){
    }

    @FXML
    public void initialize(){


        // Source url : https://stackoverflow.com/questions/45977390/how-to-force-a-double-input-in-a-textfield-in-javafx
        Pattern validEditingState = Pattern.compile("(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };

        StringConverter<Double> converter = new StringConverter<Double>() {

            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 1.0 ;
                } else {
                    return Double.valueOf(s);
                }
            }


            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };
        StringConverter<Double> converterAngle = new StringConverter<Double>() {

            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0 ;
                } else {
                    return Double.valueOf(s);
                }
            }


            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };

        lengthTextField.setTextFormatter(new TextFormatter<>(converter, 1.0, filter));
        heightTextField.setTextFormatter(new TextFormatter<>(converter, 1.0, filter));
        widthTextField.setTextFormatter(new TextFormatter<>(converter, 1.0, filter));
        angleTextField.setTextFormatter(new TextFormatter<>(converterAngle, 0.0, filter));


        SpinnerValueFactory.IntegerSpinnerValueFactory hourSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        hourSpinner.setValueFactory(hourSpinnerValue);

        SpinnerValueFactory.IntegerSpinnerValueFactory minuteSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minuteSpinner.setValueFactory(minuteSpinnerValue);
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelledButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleModifyButtonAction(ActionEvent event) {
        String message = validateInput();
        if (message == ""){
            bundleDto.plankSize = plankSizeTextField1.getText()+"x"+plankSizeTextField2.getText();
            bundleDto.barcode = barcodeTextField.getText();
            bundleDto.essence = essenceTextField.getText();
            bundleDto.length = parseDouble(lengthTextField.getText());
            bundleDto.angle = parseDouble(angleTextField.getText());
            bundleDto.height = parseDouble(heightTextField.getText());
            bundleDto.width = parseDouble(widthTextField.getText());
            bundleDto.time = LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue());
            bundleDto.date = datePicker.getValue();
            Stage stage = (Stage) modifyButton.getScene().getWindow();
            stage.close();
        } else{
            System.out.print(message);
        }
    }

    private String validateInput(){
        String message = "";
        if (barcodeTextField.getText().isEmpty()){
            message = "Le code barre ne doit pas être vide";
        }
        if (essenceTextField.getText().isEmpty()){
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
        barcodeTextField.setText(bundleDto.barcode);
        essenceTextField.setText(bundleDto.essence);
        plankSizeTextField1.setText(bundleDto.plankSize.substring(0,1));
        plankSizeTextField2.setText(bundleDto.plankSize.substring(2));
        lengthTextField.setText(Double.toString(bundleDto.length));
        widthTextField.setText(Double.toString(bundleDto.width));
        heightTextField.setText(Double.toString(bundleDto.height));
        angleTextField.setText(Double.toString(bundleDto.angle));
        SpinnerValueFactory.IntegerSpinnerValueFactory hourSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, bundleDto.time.getHour());
        hourSpinner.setValueFactory(hourSpinnerValue);
        SpinnerValueFactory.IntegerSpinnerValueFactory minuteSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, bundleDto.time.getMinute());
        minuteSpinner.setValueFactory(minuteSpinnerValue);
        datePicker.setValue(bundleDto.date);
    }
}
