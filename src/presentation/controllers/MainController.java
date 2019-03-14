package presentation.controllers;

import domain.dtos.BundleDto;
import enums.EditorMode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import presentation.presenters.YardPresenter;

import java.time.format.DateTimeFormatter;

public class MainController extends BaseController {

    public ObjectProperty<EditorMode> editorMode;
    public ToggleGroup editorModeToggleGroup;

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
    @FXML public TextFlow bundleHeigth;
    @FXML public TextFlow bundleDate;
    @FXML public TextFlow bundleHour;
    @FXML public TextFlow bundleEssence;
    @FXML public TextFlow bundleSize;

    @FXML public ToggleButton pointerButton;
    @FXML public ToggleButton addBundleButton;
    @FXML public ToggleButton deleteButton;

    @FXML
    public void initialize()
    {
        editorMode = new SimpleObjectProperty<>();

        ObservableList<String> listItems = FXCollections.observableArrayList("Bundle 1","Bundle 2", "Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle","Bundle");
        listView.setItems(listItems);

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

    public void updateBundleInfo(BundleDto bundle){
        bundleCode.getChildren().clear();
        bundleLength.getChildren().clear();
        bundleWidth.getChildren().clear();
        bundleHeigth.getChildren().clear();
        bundleDate.getChildren().clear();
        bundleHour.getChildren().clear();
        bundleEssence.getChildren().clear();
        bundleSize.getChildren().clear();

        Text barcode =  new Text(bundle.barcode);
        bundleCode.getChildren().add(barcode);
        Text length =  new Text(Double.toString(bundle.length));
        bundleLength.getChildren().add(length);
        Text width =  new Text(Double.toString(bundle.width));
        bundleWidth.getChildren().add(width);
        Text heigth =  new Text(Double.toString(bundle.height));
        bundleHeigth.getChildren().add(heigth);
        Text date =  new Text(bundle.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        bundleDate.getChildren().add(date);
        Text hour =  new Text(bundle.time.format(DateTimeFormatter.ofPattern("HH:mm")));
        bundleHour.getChildren().add(hour);
        Text essence =  new Text(bundle.essence);
        bundleEssence.getChildren().add(essence);
        Text plankSize =  new Text(bundle.plankSize);
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

        packCode.setFont(new Font("System", 12));
        packCode.setFill(Color.WHITESMOKE);
        packLong.setFont(new Font("System", 12));
        packLong.setFill(Color.WHITESMOKE);
        packLarg.setFont(new Font("System", 12));
        packLarg.setFill(Color.WHITESMOKE);
        packHaut.setFont(new Font("System", 12));
        packHaut.setFill(Color.WHITESMOKE);
        packDate.setFont(new Font("System", 12));
        packDate.setFill(Color.WHITESMOKE);
        packHeure.setFont(new Font("System", 12));
        packHeure.setFill(Color.WHITESMOKE);
        packType.setFont(new Font("System", 12));
        packType.setFill(Color.WHITESMOKE);
        packPlank.setFont(new Font("System", 12));
        packPlank.setFill(Color.WHITESMOKE);

        packCodeView.getChildren().add(packCode);
        packLongView.getChildren().add(packLong);
        packLargView.getChildren().add(packLarg);
        packHautView.getChildren().add(packHaut);
        packDateView.getChildren().add(packDate);
        packHeureView.getChildren().add(packHeure);
        packTypeView.getChildren().add(packType);
        packPlankSize.getChildren().add(packPlank);
    }
}
