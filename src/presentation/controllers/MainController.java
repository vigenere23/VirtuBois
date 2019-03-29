package presentation.controllers;

import domain.dtos.BundleDto;
import domain.entities.Yard;
import enums.EditorMode;
import helpers.FileHelper;
import helpers.JavafxHelper;
import helpers.Point2D;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import presentation.presenters.BundlePresenter;
import presentation.presenters.YardPresenter;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController extends BaseController {

    public ObjectProperty<EditorMode> editorMode;
    public ToggleGroup editorModeToggleGroup;

    DropShadow dropShadow;

    private YardPresenter yardPresenter;
    private Map<Rectangle, BundleDto> rectanglesId = new HashMap<>();
    private List<BundleDto> observableBundleList;
    private BundleDto selectedBundle;

    @FXML
    Pane root;
    @FXML
    Pane yardWrapper;

    @FXML
    public TextField inventorySearchBar;
    @FXML
    public TableView<BundleDto> inventoryTable;
    @FXML
    public TableColumn codeColumn;
    @FXML
    public TableColumn typeColumn;
    @FXML
    public TableColumn sizeColumn;

    @FXML
    public TextField bundleBarcodeValue;
    @FXML
    public TextField bundleLengthValue;
    @FXML
    public TextField bundleWidthValue;
    @FXML
    public TextField bundleHeightValue;
    @FXML
    public DatePicker bundleDateValue;
    @FXML
    public Spinner<Integer> bundleHourValue;
    @FXML
    public Spinner<Integer> bundleMinuteValue;
    @FXML
    public TextField bundleEssenceValue;
    @FXML
    public TextField bundlePlankSizeValue1;
    @FXML
    public TextField bundlePlankSizeValue2;
    @FXML
    public TextField bundleXPosValue;
    @FXML
    public TextField bundleYPosValue;
    @FXML
    public TextField bundleAngleValue;

    @FXML
    public ToggleButton pointerButton;
    @FXML
    public ToggleButton addBundleButton;
    @FXML
    public ToggleButton deleteButton;
    @FXML
    public ToggleButton snapGridButton;

    public boolean gridIsOn;

    @FXML
    public VBox elevationViewBox;

    @FXML
    public void initialize() {
        editorMode = new SimpleObjectProperty<>();

        initTableView();
        initInventorySearchBar();
        setEventHandlers();
        setupEditorModeToggleButtons();
        initYard();
        initBundleInfo();

        dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setColor(Color.GREY);
        gridIsOn = false;
    }

    //PRIVATE METHODS

    private void setEventHandlers() {
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                editorMode.setValue(EditorMode.POINTER);
            }
        });
    }

    private void setupEditorModeToggleButtons() {
        snapGridButton.setOnAction(event -> {
            gridIsOn = snapGridButton.isSelected();
            yardPresenter.draw();
        });
        pointerButton.setOnAction(event -> editorMode.setValue(EditorMode.POINTER));
        addBundleButton.setOnAction(event -> editorMode.setValue(EditorMode.ADDING_BUNDLE));
        deleteButton.setOnAction(event -> editorMode.setValue(EditorMode.DELETE));

        editorModeToggleGroup = new ToggleGroup();
        pointerButton.setToggleGroup(editorModeToggleGroup);
        addBundleButton.setToggleGroup(editorModeToggleGroup);
        deleteButton.setToggleGroup(editorModeToggleGroup);

        editorMode.addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(EditorMode.POINTER)) editorModeToggleGroup.selectToggle(pointerButton);
            else if (newValue.equals(EditorMode.ADDING_BUNDLE)) editorModeToggleGroup.selectToggle(addBundleButton);
            else if (newValue.equals(EditorMode.DELETE)) editorModeToggleGroup.selectToggle(deleteButton);
            else editorModeToggleGroup.selectToggle(null);
        });

        editorMode.setValue(EditorMode.POINTER);
    }

    private void initYard() {
        yardPresenter = new YardPresenter(this);
        yardWrapper.getChildren().setAll(yardPresenter);
        AnchorPane.setRightAnchor(yardPresenter, 0.0);
        AnchorPane.setLeftAnchor(yardPresenter, 0.0);
        AnchorPane.setBottomAnchor(yardPresenter, 0.0);
        AnchorPane.setTopAnchor(yardPresenter, 0.0);
    }

    private void initBundleInfo() {
        JavafxHelper.addStringToDoubleConverter(bundleLengthValue, null, 0.0, null);
        JavafxHelper.addStringToDoubleConverter(bundleWidthValue, null, 0.0, null);
        JavafxHelper.addStringToDoubleConverter(bundleHeightValue, null, 0.0, null);
        bundleHourValue.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        bundleHourValue.getValueFactory().setValue(0);
        bundleMinuteValue.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        bundleMinuteValue.getValueFactory().setValue(0);
        JavafxHelper.addStringToIntegerConverter(bundlePlankSizeValue1, 0, 1, null);
        JavafxHelper.addStringToIntegerConverter(bundlePlankSizeValue2, 0, 1, null);
        JavafxHelper.addStringToDoubleConverter(bundleXPosValue, 0.0, null, null);
        JavafxHelper.addStringToDoubleConverter(bundleYPosValue, 0.0, null, null);
        JavafxHelper.addStringToDoubleConverter(bundleAngleValue, 0.0, -360.0, 360.0);
        initTextFieldsHandlers();
    }

    private void initTextFieldsHandlers() {

        bundleBarcodeValue.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {
                    if (!bundleBarcodeValue.getText().isEmpty()) {
                        selectedBundle.barcode = bundleBarcodeValue.getText();
                        larmanController.modifyBundleProperties(selectedBundle);
                        yardPresenter.draw();
                    }
                    updateBundleInfo(selectedBundle);
                }
            }
        });

        bundleLengthValue.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {
                    if (!bundleLengthValue.getText().isEmpty() && !bundleLengthValue.getText().equals("-") && !bundleLengthValue.getText().equals(".") && !bundleLengthValue.getText().equals("-.")) {
                        selectedBundle.length = Double.parseDouble(bundleLengthValue.getText());
                        larmanController.modifyBundleProperties(selectedBundle);
                        yardPresenter.draw();
                    }
                    updateBundleInfo(selectedBundle);
                }

            }
        });

        bundleWidthValue.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {
                    if (!bundleWidthValue.getText().isEmpty() && !bundleWidthValue.getText().equals("-") && !bundleWidthValue.getText().equals(".") && !bundleWidthValue.getText().equals("-.")) {
                        selectedBundle.width = Double.parseDouble(bundleWidthValue.getText());
                        larmanController.modifyBundleProperties(selectedBundle);
                        yardPresenter.draw();
                    }
                    updateBundleInfo(selectedBundle);
                }
            }
        });

        bundleHeightValue.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {

                    if (!bundleHeightValue.getText().isEmpty() && !bundleHeightValue.getText().equals("-") && !bundleHeightValue.getText().equals(".") && !bundleHeightValue.getText().equals("-.")) {
                        selectedBundle.height = Double.parseDouble(bundleHeightValue.getText());
                        larmanController.modifyBundleProperties(selectedBundle);
                        yardPresenter.draw();
                    }
                    updateBundleInfo(selectedBundle);
                }
            }
        });

        bundleDateValue.setOnAction(event -> {
            if (selectedBundle != null) {
                if (bundleDateValue.getValue() != null) {
                    if (bundleDateValue.getValue() != selectedBundle.date) {
                        selectedBundle.date = bundleDateValue.getValue();
                        larmanController.modifyBundleProperties(selectedBundle);
                        yardPresenter.draw();
                        updateBundleInfo(selectedBundle);
                    }
                }
            }
        });

        bundleHourValue.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {
                    selectedBundle.time = LocalTime.of(bundleHourValue.getValue(), bundleMinuteValue.getValue());
                    larmanController.modifyBundleProperties(selectedBundle);
                    yardPresenter.draw();
                    updateBundleInfo(selectedBundle);
                }
            }
        });

        bundleMinuteValue.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {
                    selectedBundle.time = LocalTime.of(bundleHourValue.getValue(), bundleMinuteValue.getValue());
                    larmanController.modifyBundleProperties(selectedBundle);
                    yardPresenter.draw();
                    updateBundleInfo(selectedBundle);
                }
            }
        });

        bundleEssenceValue.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {
                    if (!bundleEssenceValue.getText().isEmpty()) {
                        selectedBundle.essence = bundleEssenceValue.getText();
                        larmanController.modifyBundleProperties(selectedBundle);
                        yardPresenter.draw();
                    }
                    updateBundleInfo(selectedBundle);
                }
            }
        });

        bundlePlankSizeValue1.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {
                    if (!bundlePlankSizeValue1.getText().isEmpty() && !bundlePlankSizeValue2.getText().isEmpty()) {
                        selectedBundle.plankSize = bundlePlankSizeValue1.getText() + "x" + bundlePlankSizeValue2.getText();
                        larmanController.modifyBundleProperties(selectedBundle);
                        yardPresenter.draw();
                    }
                    updateBundleInfo(selectedBundle);
                }
            }
        });

        bundlePlankSizeValue2.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {
                    if (!bundlePlankSizeValue1.getText().isEmpty() && !bundlePlankSizeValue2.getText().isEmpty()) {
                        selectedBundle.plankSize = bundlePlankSizeValue1.getText() + "x" + bundlePlankSizeValue2.getText();
                        larmanController.modifyBundleProperties(selectedBundle);
                        yardPresenter.draw();
                    }
                    updateBundleInfo(selectedBundle);
                }
            }
        });

        bundleXPosValue.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {
                    if (!bundleXPosValue.getText().isEmpty() && !bundleXPosValue.getText().equals("-") && !bundleXPosValue.getText().equals(".") && !bundleXPosValue.getText().equals("-.")) {
                        selectedBundle.position.setX(Double.parseDouble(bundleXPosValue.getText()));
                        larmanController.modifyBundlePosition(selectedBundle.id, selectedBundle.position);
                        yardPresenter.draw();
                    }
                    updateBundleInfo(selectedBundle);
                }
            }
        });

        bundleYPosValue.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {
                    if (!bundleYPosValue.getText().isEmpty() && !bundleYPosValue.getText().equals("-") && !bundleYPosValue.getText().equals(".") && !bundleYPosValue.getText().equals("-.")) {
                        selectedBundle.position.setY(Double.parseDouble(bundleYPosValue.getText()));
                        larmanController.modifyBundlePosition(selectedBundle.id, selectedBundle.position);
                        yardPresenter.draw();
                    }
                    updateBundleInfo(selectedBundle);
                }
            }
        });

        bundleAngleValue.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (selectedBundle != null) {
                    if (!bundleAngleValue.getText().isEmpty() && !bundleAngleValue.getText().equals("-") && !bundleAngleValue.getText().equals(".") && !bundleAngleValue.getText().equals("-.")) {
                        selectedBundle.angle = Double.parseDouble(bundleAngleValue.getText());
                        larmanController.modifyBundleProperties(selectedBundle);
                        yardPresenter.draw();
                    }
                    updateBundleInfo(selectedBundle);
                }
            }
        });


    }

    private void initInventorySearchBar() {
        inventorySearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<BundleDto> filteredData = new FilteredList<>(FXCollections.observableArrayList(observableBundleList));
            filteredData.setPredicate(bundleDto -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (bundleDto.getBarcode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (bundleDto.getEssence().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (bundleDto.getPlankSize().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;

            });
            SortedList<BundleDto> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(inventoryTable.comparatorProperty());
            inventoryTable.setItems(sortedData);

        });
    }

    private void initTableView() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<BundleDto, String>("barcode"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<BundleDto, String>("essence"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<BundleDto, String>("plankSize"));
        inventoryTable.setRowFactory(tv -> {
            TableRow<BundleDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    BundleDto bundle = row.getItem();
                    yardPresenter.setTopSelectedBundle(bundle);
                    inventoryTable.getSelectionModel().select(bundle);
                    updateBundleInfo(bundle);
                } else {
                    yardPresenter.setTopSelectedBundle(null);
                    clearAllBundleInfo();
                }
                clearElevationView();
            });
            return row;
        });
    }

    //PUBLIC METHODS

    public void clearAllBundleInfo() {
        this.selectedBundle = null;
        bundleBarcodeValue.clear();
        bundleLengthValue.clear();
        bundleWidthValue.clear();
        bundleHeightValue.clear();
        bundleDateValue.setValue(null);
        bundleHourValue.getValueFactory().setValue(0);
        bundleMinuteValue.getValueFactory().setValue(0);
        bundleEssenceValue.clear();
        bundlePlankSizeValue1.clear();
        bundlePlankSizeValue2.clear();
        bundleXPosValue.clear();
        bundleYPosValue.clear();
        bundleAngleValue.clear();

    }

    public void updateBundleInfo(BundleDto bundle) {
        this.selectedBundle = bundle;
        bundleBarcodeValue.setText(bundle.barcode);
        bundleLengthValue.setText(String.valueOf(bundle.length));
        bundleWidthValue.setText(String.valueOf(bundle.width));
        bundleHeightValue.setText(String.valueOf(bundle.height));
        bundleDateValue.setValue(bundle.date);
        bundleHourValue.getValueFactory().setValue(bundle.time.getHour());
        bundleMinuteValue.getValueFactory().setValue(bundle.time.getMinute());
        bundleEssenceValue.setText(bundle.essence);
        String[] plankSize = bundle.plankSize.split("x");
        bundlePlankSizeValue1.setText(plankSize[0]);
        bundlePlankSizeValue2.setText(plankSize[1]);
        bundleXPosValue.setText(String.valueOf(bundle.position.getX()));
        bundleYPosValue.setText(String.valueOf(bundle.position.getY()));
        bundleAngleValue.setText(String.valueOf(bundle.angle));
    }

    public void updateElevationView(List<BundleDto> bundles) {
        rectanglesId.clear();
        clearElevationView();
        for (BundleDto bundleDto : bundles) {
            BundlePresenter presenter = new BundlePresenter(bundleDto);
            Rectangle rectangle = presenter.get();
            rectangle.setWidth(200);
            rectangle.setHeight(50);
            rectangle.setRotate(0);
            if (yardPresenter.getTopSelectedBundle().equals(bundleDto)) {
                rectangle.setEffect(dropShadow);
            }
            elevationViewBox.getChildren().add(0, rectangle);
            rectanglesId.put(rectangle, bundleDto);
            rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                for (Map.Entry<Rectangle, BundleDto> entry : rectanglesId.entrySet()) {
                    entry.getKey().setEffect(null);
                }
                if (event.getButton() == MouseButton.PRIMARY) {
                    updateBundleInfo(rectanglesId.get(rectangle));
                    rectangle.setEffect(dropShadow);
                    yardPresenter.setTopSelectedBundle(rectanglesId.get(rectangle));
                }
            });
        }
    }

    public void clearElevationView() {
        elevationViewBox.getChildren().clear();
    }

    public void addTableViewBundles(List<BundleDto> bundles) {
        inventorySearchBar.clear();
        observableBundleList = bundles;
        ObservableList<BundleDto> data = FXCollections.observableArrayList(bundles);
        inventoryTable.setItems(data);
    }

    private void modifySelectedBundle() {
        selectedBundle.barcode = bundleBarcodeValue.getText();
        selectedBundle.length = Double.parseDouble(bundleLengthValue.getText());
        selectedBundle.width = Double.parseDouble(bundleWidthValue.getText());
        selectedBundle.height = Double.parseDouble(bundleHeightValue.getText());
        selectedBundle.date = bundleDateValue.getValue();
        selectedBundle.time = LocalTime.of(bundleHourValue.getValue(),bundleMinuteValue.getValue());
        selectedBundle.essence = bundleEssenceValue.getText();
        selectedBundle.plankSize = bundlePlankSizeValue1.getText() + "x" + bundlePlankSizeValue2.getText();
        selectedBundle.position = new Point2D(Double.parseDouble(bundleXPosValue.getText()),Double.parseDouble(bundleYPosValue.getText()));
        selectedBundle.angle = Double.parseDouble(bundleAngleValue.getText());
    }

    public void handleMenuFileNew(ActionEvent actionEvent) {
        FileHelper.newFile(stage, larmanController.getYard());
    }

    public void handleMenuFileOpen(ActionEvent actionEvent) {
        FileHelper.openFile(stage, larmanController.getYard());
    }

    public void handleMenuFileSave(ActionEvent actionEvent) {
        FileHelper.saveFile(stage, larmanController.getYard());
    }

    public void handleMenuSaveAs(ActionEvent actionEvent) {
        FileHelper.saveFileAs(stage, larmanController.getYard());
    }

    public void handleMenuHelpAbout(ActionEvent actionEvent) {
        JavafxHelper.popupView("About", "À propos", false, false);
    }
}