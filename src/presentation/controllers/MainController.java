package presentation.controllers;

import domain.dtos.BundleDto;
import domain.entities.Bundle;
import enums.EditorMode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
import java.util.List;

public class MainController extends BaseController {

    public ObjectProperty<EditorMode> editorMode;
    public ToggleGroup editorModeToggleGroup;

    private Font windowFont;

    @FXML Pane root;
    @FXML Pane yardWrapper;

    @FXML public ListView listView;
    @FXML public TextFlow packCodeView;
    @FXML public TextFlow packLongView;
    @FXML public TextFlow packLargView;
    @FXML public TextFlow packHautView;
    @FXML public TextFlow packDateView;
    @FXML public TextFlow packHeureView;
    @FXML public TextFlow packTypeView;
    @FXML public TextFlow packPlankSize;

    @FXML public TextFlow bundleCode;
    @FXML public TextFlow bundleLength;
    @FXML public TextFlow bundleWidth;
    @FXML public TextFlow bundleHeight;
    @FXML public TextFlow bundleDate;
    @FXML public TextFlow bundleHour;
    @FXML public TextFlow bundleEssence;
    @FXML public TextFlow bundleSize;

    @FXML public ToggleButton pointerButton;
    @FXML public ToggleButton addBundleButton;
    @FXML public ToggleButton deleteButton;
    @FXML public ToggleButton editButton;

    @FXML public VBox elevViewBox;
    @FXML
    public void initialize()
    {
        editorMode = new SimpleObjectProperty<>();

        ObservableList<String> listItems = FXCollections.observableArrayList("Bundle 1", "Bundle 2", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle", "Bundle");
        listView.setItems(listItems);

        windowFont = new Font("System", 13);



        initBundleInfoView();
        setEventHandlers();
        setupEditorModeToggleButtons();
        initYard();
    }

    private void setEventHandlers() {
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                editorMode.setValue(EditorMode.POINTER);
            }
        });
    }

    private void setupEditorModeToggleButtons() {
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
        bundleCode.getChildren().clear();
        bundleLength.getChildren().clear();
        bundleWidth.getChildren().clear();
        bundleHeight.getChildren().clear();
        bundleDate.getChildren().clear();
        bundleHour.getChildren().clear();
        bundleEssence.getChildren().clear();
        bundleSize.getChildren().clear();
    }

    public void updateBundleInfo(BundleDto bundle) {
        clearAllBundleInfo();
        Text barcode = new Text(bundle.barcode);
        barcode.setFont(windowFont);
        barcode.setFill(Color.WHITESMOKE);
        bundleCode.getChildren().add(barcode);

        Text length = new Text(Double.toString(bundle.length));
        length.setFont(windowFont);
        length.setFill(Color.WHITESMOKE);
        bundleLength.getChildren().add(length);

        Text width = new Text(Double.toString(bundle.width));
        width.setFont(windowFont);
        width.setFill(Color.WHITESMOKE);
        bundleWidth.getChildren().add(width);

        Text height = new Text(Double.toString(bundle.height));
        height.setFont(windowFont);
        height.setFill(Color.WHITESMOKE);
        bundleHeight.getChildren().add(height);


        Text date = new Text(bundle.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        date.setFont(windowFont);
        date.setFill(Color.WHITESMOKE);
        bundleDate.getChildren().add(date);

        Text hour = new Text(bundle.time.format(DateTimeFormatter.ofPattern("HH:mm")));
        hour.setFont(windowFont);
        hour.setFill(Color.WHITESMOKE);
        bundleHour.getChildren().add(hour);


        Text essence = new Text(bundle.essence);
        essence.setFont(windowFont);
        essence.setFill(Color.WHITESMOKE);
        bundleEssence.getChildren().add(essence);


        Text plankSize = new Text(bundle.plankSize);
        plankSize.setFont(windowFont);
        plankSize.setFill(Color.WHITESMOKE);
        bundleSize.getChildren().add(plankSize);
    }

    private void initBundleInfoView() {
        Text packCode = new Text("Code barre : ");
        Text packLong = new Text("Longeur : ");
        Text packLarg = new Text("Largeur : ");
        Text packHaut = new Text("Hauteur : ");
        Text packDate = new Text("Date de production : ");
        Text packHeure = new Text("Heure de production : ");
        Text packType = new Text("Essence : ");
        Text packPlank = new Text("Dimensions des planches : ");

        packCode.setFont(windowFont);
        packCode.setFill(Color.WHITESMOKE);
        packLong.setFont(windowFont);
        packLong.setFill(Color.WHITESMOKE);
        packLarg.setFont(windowFont);
        packLarg.setFill(Color.WHITESMOKE);
        packHaut.setFont(windowFont);
        packHaut.setFill(Color.WHITESMOKE);
        packDate.setFont(windowFont);
        packDate.setFill(Color.WHITESMOKE);
        packHeure.setFont(windowFont);
        packHeure.setFill(Color.WHITESMOKE);
        packType.setFont(windowFont);
        packType.setFill(Color.WHITESMOKE);
        packPlank.setFont(windowFont);
        packPlank.setFill(Color.WHITESMOKE);

        packCodeView.getChildren().add(packCode);
        packLongView.getChildren().add(packLong);
        packLargView.getChildren().add(packLarg);
        packHautView.getChildren().add(packHaut);
        packDateView.getChildren().add(packDate);
        packHeureView.getChildren().add(packHeure);
        packTypeView.getChildren().add(packType);
        packPlankSize.getChildren().add(packPlank);

        bundleCode.setTextAlignment(TextAlignment.RIGHT);
        bundleLength.setTextAlignment(TextAlignment.RIGHT);
        bundleHeight.setTextAlignment(TextAlignment.RIGHT);
        bundleWidth.setTextAlignment(TextAlignment.RIGHT);
        bundleDate.setTextAlignment(TextAlignment.RIGHT);
        bundleHour.setTextAlignment(TextAlignment.RIGHT);
        bundleEssence.setTextAlignment(TextAlignment.RIGHT);
        bundleSize.setTextAlignment(TextAlignment.RIGHT);
    }

    public void updateElevationView(List<BundleDto> bundles) {

    }
}