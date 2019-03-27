package presentation.controllers;

import domain.dtos.BundleDto;
import enums.EditorMode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class MainController extends BaseController {

    public ObjectProperty<EditorMode> editorMode;
    public ToggleGroup editorModeToggleGroup;

    DropShadow dropShadow;

    private Font windowFont;
    private Map<Rectangle, BundleDto> rectanglesId = new HashMap<>();

    @FXML Pane root;
    @FXML Pane yardWrapper;

    //@FXML public ListView listView;
    @FXML public TableView inventoryTable;
    @FXML public TableColumn codeColumn;
    @FXML public TableColumn typeColumn;
    @FXML public TableColumn sizeColumn;

    @FXML public TextFlow bundleBarcodeLabel;
    @FXML public TextFlow bundleLengthLabel;
    @FXML public TextFlow bundleWidthLabel;
    @FXML public TextFlow bundleHeightLabel;
    @FXML public TextFlow bundleDateLabel;
    @FXML public TextFlow bundleTimeLabel;
    @FXML public TextFlow bundleEssenceLabel;
    @FXML public TextFlow bundlePlankSizeLabel;

    @FXML public TextField bundleBarcodeValue;
    @FXML public TextField bundleLengthValue;
    @FXML public TextField bundleWidthValue;
    @FXML public TextField bundleHeightValue;
    @FXML public TextField bundleDateValue;
    @FXML public TextField bundleTimeValue;
    @FXML public TextField bundleEssenceValue;
    @FXML public TextField bundlePlankSizeValue;

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
        
        initBundleInfoView();
        initTableView();
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
            getYard().draw();
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
        YardPresenter yardPresenter = new YardPresenter(this);
        yardWrapper.getChildren().setAll(yardPresenter);
        AnchorPane.setRightAnchor(yardPresenter, 0.0);
        AnchorPane.setLeftAnchor(yardPresenter, 0.0);
        AnchorPane.setBottomAnchor(yardPresenter, 0.0);
        AnchorPane.setTopAnchor(yardPresenter, 0.0);
    }

    public void clearAllBundleInfo() {
        bundleBarcodeValue.clear();
        bundleLengthValue.clear();
        bundleWidthValue.clear();
        bundleHeightValue.clear();
        bundleDateValue.clear();
        bundleTimeValue.clear();
        bundleEssenceValue.clear();
        bundlePlankSizeValue.clear();
    }

    public void updateBundleInfo(BundleDto bundle) {
        setTextField(bundleBarcodeValue, bundle.barcode);
        setTextField(bundleWidthValue, String.valueOf(bundle.width));
        setTextField(bundleLengthValue, String.valueOf(bundle.length));
        setTextField(bundleHeightValue, String.valueOf(bundle.height));
        setTextField(bundleDateValue, bundle.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        setTextField(bundleTimeValue, bundle.time.format(DateTimeFormatter.ofPattern("HH:mm")));
        setTextField(bundleEssenceValue, bundle.essence);
        setTextField(bundlePlankSizeValue, bundle.plankSize);
        getYard().draw();
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

    private void initBundleInfoView() {
        setText(bundleBarcodeLabel, "Code barre : ");
        setText(bundleLengthLabel, "Longeur : ");
        setText(bundleWidthLabel, "Largeur : ");
        setText(bundleHeightLabel, "Hauteur : ");
        setText(bundleDateLabel, "Date de production : ");
        setText(bundleTimeLabel, "Heure de production : ");
        setText(bundleEssenceLabel, "Essence : ");
        setText(bundlePlankSizeLabel, "Dimensions des planches : ");
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
            if(getYard().getTopSelectedBundle().equals(bundleDto)){rectangle.setEffect(dropShadow);}
            elevationViewBox.getChildren().add(0, rectangle);
            rectanglesId.put(rectangle, bundleDto);
            rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                for (Map.Entry<Rectangle, BundleDto> entry : rectanglesId.entrySet()) {
                    entry.getKey().setEffect(null);
                }
                if (event.getButton() == MouseButton.PRIMARY) {
                    updateBundleInfo(rectanglesId.get(rectangle));
                    rectangle.setEffect(dropShadow);
                    getYard().setTopSelectedBundle(rectanglesId.get(rectangle));
                }
            });
        }
    }

    public void clearElevationView() {
        elevationViewBox.getChildren().clear();
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
                    getYard().setTopSelectedBundle(bundle);
                    inventoryTable.getSelectionModel().select(bundle);
                    updateBundleInfo(bundle);
                    clearElevationView();
                }
                else{
                    getYard().setTopSelectedBundle(null);
                    clearAllBundleInfo();
                }
            });
            return row;
        });
    }

    public void addTableViewBundles(List<BundleDto> bundles) {
        inventoryTable.getItems().clear();
        ObservableList<BundleDto> data = FXCollections.observableArrayList(bundles);
        inventoryTable.setItems(data);
    }

    private YardPresenter getYard() {
        return (YardPresenter) yardWrapper.getChildren().get(0);
    }
}