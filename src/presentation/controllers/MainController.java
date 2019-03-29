package presentation.controllers;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import enums.EditorMode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import presentation.presenters.BundlePresenter;
import presentation.presenters.YardPresenter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class MainController extends BaseController {

    public ObjectProperty<EditorMode> editorMode;
    public ToggleGroup editorModeToggleGroup;

    DropShadow dropShadow;

    private Font windowFont;
    private YardPresenter yardPresenter;
    private Map<Rectangle, BundleDto> rectanglesId = new HashMap<>();
    private List<BundleDto> observableBundleList;

    @FXML Pane root;
    @FXML Pane yardWrapper;

    //@FXML public ListView listView;
    @FXML public TextField inventorySearchBar;
    @FXML public TableView<BundleDto> inventoryTable;
    @FXML public TableColumn codeColumn;
    @FXML public TableColumn typeColumn;
    @FXML public TableColumn sizeColumn;

    @FXML public TextField bundleCodeValue;
    @FXML public TextField bundleLengthValue;
    @FXML public TextField bundleWidthValue;
    @FXML public TextField bundleHeightValue;
    @FXML public DatePicker bundleDateValue;
    @FXML public TextField bundleEssenceValue;

    @FXML public ToggleButton pointerButton;
    @FXML public ToggleButton addBundleButton;
    @FXML public ToggleButton deleteButton;
    @FXML public ToggleButton snapGridButton;

    @FXML public DatePicker datePicker;
    @FXML public Spinner<Integer> hourSpinner;
    @FXML public Spinner<Integer> minuteSpinner;

    public boolean gridIsOn;

    @FXML public VBox elevationViewBox;
    @FXML
    public void initialize()
    {
        editorMode = new SimpleObjectProperty<>();

        windowFont = new Font("System", 13);

        initTableView();
        initInventorySearchBar();
        setEventHandlers();
        setupEditorModeToggleButtons();
        initYard();

        dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setColor(Color.GREY);
        gridIsOn = false;
    }


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

    public void clearAllBundleInfo() {
        bundleCodeValue.clear();
        bundleLengthValue.clear();
        bundleWidthValue.clear();
        bundleHeightValue.clear();
        bundleEssenceValue.clear();

    }

    public void updateBundleInfo(BundleDto bundle) {
        setTextField(bundleCodeValue, bundle.barcode);
        bundleCodeValue.setOnAction(event -> {
            bundle.barcode = bundleCodeValue.getText();
            LarmanController.getInstance().getYard().modifyBundleProperties(bundle);
        });
        setTextField(bundleWidthValue, String.valueOf(bundle.width));
        bundleWidthValue.setOnAction(event -> {
            bundle.width = Double.parseDouble(bundleWidthValue.getText());
            LarmanController.getInstance().getYard().modifyBundleProperties(bundle);
        });
        setTextField(bundleLengthValue, String.valueOf(bundle.length));
        bundleLengthValue.setOnAction(event -> {
            bundle.length = Double.parseDouble(bundleLengthValue.getText());
            LarmanController.getInstance().getYard().modifyBundleProperties(bundle);
        });
        setTextField(bundleHeightValue, String.valueOf(bundle.height));
        bundleHeightValue.setOnAction(event -> {
            bundle.height = Double.parseDouble(bundleHeightValue.getText());
            LarmanController.getInstance().getYard().modifyBundleProperties(bundle);
        });
        setTextField(bundleEssenceValue, bundle.essence);
        bundleEssenceValue.setOnAction(event -> {
            bundle.essence = bundleEssenceValue.getText();
            LarmanController.getInstance().getYard().modifyBundleProperties(bundle);
        });
        bundleDateValue.setValue(bundle.date);
        bundleDateValue.setOnAction((event -> {
            bundle.date = bundleDateValue.getValue();
            LarmanController.getInstance().getYard().modifyBundleProperties(bundle);
        }));

        //setTextField(bundleDateValue, bundle.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        //setTextField(bundleTimeValue, bundle.time.format(DateTimeFormatter.ofPattern("HH:mm")));

        //setTextField(bundlePlankSizeValue, bundle.plankSize);
        //bundlePlankSizeValue.setOnAction(event -> {
          //  bundle.plankSize = bundleBarcodeValue.getText();
            //LarmanController.getInstance().getYard().modifyBundleProperties(bundle);
        //});
    }

    public void setTextField(TextField textField, String textToSet) {
        textField.setText(String.valueOf(textToSet));
    }

    private void setText(TextFlow textFlow, String textToSet) {
        Text text = new Text(textToSet);
        text.setFont(windowFont);
        text.setFill(Color.WHITESMOKE);
        text.setTextAlignment(TextAlignment.RIGHT);
        textFlow.getChildren().setAll(text);
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
            if(yardPresenter.getTopSelectedBundle().equals(bundleDto)){rectangle.setEffect(dropShadow);}
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

    private void initInventorySearchBar(){
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

    public void initTableView() {
        codeColumn.setCellValueFactory( new PropertyValueFactory<BundleDto, String>("barcode"));
        typeColumn.setCellValueFactory( new PropertyValueFactory<BundleDto, String>("essence"));
        sizeColumn.setCellValueFactory( new PropertyValueFactory<BundleDto, String>("plankSize"));
        inventoryTable.setRowFactory( tv ->{
            TableRow<BundleDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(!row.isEmpty()){
                    BundleDto bundle = row.getItem();
                    yardPresenter.setTopSelectedBundle(bundle);
                    inventoryTable.getSelectionModel().select(bundle);
                    updateBundleInfo(bundle);
                }
                else{
                    yardPresenter.setTopSelectedBundle(null);
                    clearAllBundleInfo();
                }
                clearElevationView();
            });
            return row;
        });
    }

    public void addTableViewBundles(List<BundleDto> bundles) {
        inventorySearchBar.clear();
        observableBundleList = bundles;
        ObservableList<BundleDto> data = FXCollections.observableArrayList(bundles);
        inventoryTable.setItems(data);
    }
}