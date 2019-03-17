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

    public EditorController(){
    }

    @FXML
    public void initialize(){


        // Source url : https://stackoverflow.com/questions/45977390/how-to-force-a-double-input-in-a-textfield-in-javafx
        Pattern validEditingStateDouble = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

        UnaryOperator<TextFormatter.Change> filterDouble = c -> {
            String text = c.getControlNewText();
            if (validEditingStateDouble.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };

        StringConverter<Double> converterDouble = new StringConverter<Double>() {

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

        TextFormatter<Double> formatter = new TextFormatter<>(converterDouble,1.0, filterDouble);
        lengthTextField.setTextFormatter(formatter);
        heightTextField.setTextFormatter(formatter);
        widthTextField.setTextFormatter(formatter);
        angleTextField.setTextFormatter(formatter);


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
            bundleDto.plankSize = dimensionTextField.getText();
            bundleDto.barcode = barcodeTextField.getText();
            bundleDto.essence = essenceTextField.getText();
            bundleDto.length = parseDouble(lengthTextField.getText());
            bundleDto.angle = parseDouble(angleTextField.getText());
            bundleDto.height = parseDouble(heightTextField.getText());
            bundleDto.width = parseDouble(widthTextField.getText());
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
        barcodeTextField.setText(bundleDto.barcode);
        essenceTextField.setText(bundleDto.essence);
        dimensionTextField.setText(bundleDto.plankSize);
        lengthTextField.setText(Double.toString(bundleDto.length));
        widthTextField.setText(Double.toString(bundleDto.width));
        heightTextField.setText(Double.toString(bundleDto.height));
        angleTextField.setText(Double.toString(bundleDto.angle));
    }

    public BundleDto getBundleDto() {
        return bundleDto;
    }
}
