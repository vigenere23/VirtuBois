package presentation.controllers;

import domain.dtos.BundleDto;
import enums.EditorMode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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

    @FXML public ListView listView;

    @FXML public TextFlow bundleBarcodeLabel;
    @FXML public TextFlow bundleLengthLabel;
    @FXML public TextFlow bundleWidthLabel;
    @FXML public TextFlow bundleHeightLabel;
    @FXML public TextFlow bundleDateLabel;
    @FXML public TextFlow bundleTimeLabel;
    @FXML public TextFlow bundleEssenceLabel;
    @FXML public TextFlow bundlePlankSizeLabel;

    @FXML public TextFlow bundleBarcodeValue;
    @FXML public TextFlow bundleLengthValue;
    @FXML public TextFlow bundleWidthValue;
    @FXML public TextFlow bundleHeightValue;
    @FXML public TextFlow bundleDateValue;
    @FXML public TextFlow bundleTimeValue;
    @FXML public TextFlow bundleEssenceValue;
    @FXML public TextFlow bundlePlankSizeValue;

    @FXML public ToggleButton pointerButton;
    @FXML public ToggleButton addBundleButton;
    @FXML public ToggleButton deleteButton;
    @FXML public ToggleButton editButton;
    @FXML public ToggleButton snapGridButton;

    public boolean gridIsOn;

    public Group group;

    @FXML public VBox elevationViewBox;
    @FXML
    public void initialize()
    {
        editorMode = new SimpleObjectProperty<>();

        windowFont = new Font("System", 13);
        
        initBundleInfoView();
        setEventHandlers();
        setupEditorModeToggleButtons();
        initYard();

        dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setColor(Color.GREY);
        gridIsOn = false;
    }

    public ListView getListView() { return listView; }

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
        editButton.setOnAction(event -> editorMode.setValue(EditorMode.EDIT));

        editorModeToggleGroup = new ToggleGroup();
        pointerButton.setToggleGroup(editorModeToggleGroup);
        addBundleButton.setToggleGroup(editorModeToggleGroup);
        deleteButton.setToggleGroup(editorModeToggleGroup);
        editButton.setToggleGroup(editorModeToggleGroup);

        editorMode.addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(EditorMode.POINTER)) editorModeToggleGroup.selectToggle(pointerButton);
            else if (newValue.equals(EditorMode.ADDING_BUNDLE)) editorModeToggleGroup.selectToggle(addBundleButton);
            else if (newValue.equals(EditorMode.DELETE)) editorModeToggleGroup.selectToggle(deleteButton);
            else if (newValue.equals(EditorMode.EDIT)) editorModeToggleGroup.selectToggle(editButton);
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
        bundleBarcodeValue.getChildren().clear();
        bundleLengthValue.getChildren().clear();
        bundleWidthValue.getChildren().clear();
        bundleHeightValue.getChildren().clear();
        bundleDateValue.getChildren().clear();
        bundleTimeValue.getChildren().clear();
        bundleEssenceValue.getChildren().clear();
        bundlePlankSizeValue.getChildren().clear();
    }

    public void updateBundleInfo(BundleDto bundle) {
        setText(bundleBarcodeValue, bundle.barcode, false);
        setText(bundleLengthValue, String.valueOf(bundle.length), false);
        setText(bundleWidthValue, String.valueOf(bundle.width), false);
        setText(bundleHeightValue, String.valueOf(bundle.height), false);
        setText(bundleDateValue, bundle.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), false);
        setText(bundleTimeValue, bundle.time.format(DateTimeFormatter.ofPattern("HH:mm")), false);
        setText(bundleEssenceValue, bundle.essence, false);
        setText(bundlePlankSizeValue, bundle.plankSize, false);
    }

    private void setText(TextFlow textFlow, String textToSet, boolean alignRight) {
        Text text = new Text(textToSet);
        text.setFont(windowFont);
        text.setFill(Color.WHITESMOKE);
        if (alignRight) text.setTextAlignment(TextAlignment.RIGHT);
        textFlow.getChildren().setAll(text);
    }

    private void initBundleInfoView() {
        setText(bundleBarcodeLabel, "Code barre : ", true);
        setText(bundleLengthLabel, "Longeur : ", true);
        setText(bundleWidthLabel, "Largeur : ", true);
        setText(bundleHeightLabel, "Hauteur : ", true);
        setText(bundleDateLabel, "Date de production : ", true);
        setText(bundleTimeLabel, "Heure de production : ", true);
        setText(bundleEssenceLabel, "Essence : ", true);
        setText(bundlePlankSizeLabel, "Dimensions des planches : ", true);
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

    private YardPresenter getYard() {
        return (YardPresenter) yardWrapper.getChildren().get(0);
    }
}