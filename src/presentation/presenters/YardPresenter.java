package presentation.presenters;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import enums.EditorMode;
import helpers.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import presentation.controllers.MainController;

import java.util.*;

public class YardPresenter extends Pane implements IPresenter {
    private List<BundlePresenter> bundles;
    private String selectedBundleId = null;
    private LiftPresenter lift;
    private double zoom;
    private Point2D lastClickedPoint;
    private Point2D dragVector;
    private Point2D translateVector;

    private Point2D mousePositionInRealCoords;
    private Label mousePositionLabel;
    private Line xAxis;
    private Line yAxis;
    private Line xAxisGrid;
    private Line yAxisGrid;

    private MainController mainController;
    private LarmanController larmanController;

    public YardPresenter(MainController mainController) {
        super();
        setFocusTraversable(true);

        this.mainController = mainController;

        larmanController = LarmanController.getInstance();
        bundles = new ArrayList<>();
        zoom = ConfigHelper.defaultZoom;
        dragVector = new Point2D(0, 0);
        translateVector = new Point2D(0.0, 0.0);
        mousePositionLabel = new Label("x:0  y:0");
        mousePositionLabel.setAlignment(Pos.BOTTOM_RIGHT);// TODO Not working...
        xAxis = new Line();
        yAxis = new Line();

        initEventListeners();
        draw();
    }

    private void initEventListeners() {
        widthProperty().addListener(observable -> draw());
        heightProperty().addListener(observable -> draw());

        setOnMousePressed(event -> {
            //Point2D clippedPoint = snapPointToGrid(new Point2D(event.getX(), event.getY()));
            requestFocus();
            mainController.clearAllBundleInfo();
            if (event.getButton() == MouseButton.SECONDARY || event.getButton() == MouseButton.MIDDLE) {
                lastClickedPoint = new Point2D(event.getX(), event.getY());
            } else if (mainController.editorMode.getValue() == EditorMode.ADDING_BUNDLE) {
                createBundle();
                BundleDto dto = larmanController.getLastBundle();
                editorWindow(dto);
                ObservableList<String> itemList = FXCollections.observableArrayList(dto.id);
                mainController.getListView().setItems(itemList);
                draw();
            } else if (mainController.editorMode.getValue() == EditorMode.POINTER) {
                selectBundle();
            } else if (mainController.editorMode.getValue() == EditorMode.DELETE) {
                deleteBundle();
                draw();
            } else if (mainController.editorMode.getValue() == EditorMode.EDIT) {
                if (larmanController.getSelectedBundles(mousePositionInRealCoords).size() != 0) {
                    BundleDto dto = larmanController.getTopBundle(mousePositionInRealCoords);
                    editorWindow(dto);
                    mainController.editorMode.setValue(EditorMode.POINTER);
                    draw();
                }
            }
        });

        setOnMouseDragged(event -> {
            //Point2D clippedPoint = snapPointToGrid(new Point2D(event.getX(), event.getY()));
            if (event.getButton() == MouseButton.SECONDARY || event.getButton() == MouseButton.MIDDLE) {
                Point2D newDraggedPoint = new Point2D(event.getX(), event.getY());
                dragVector = newDraggedPoint.subtract(lastClickedPoint).multiply(1 / zoom);
                draw();
            }
            if (mainController.editorMode.getValue() == EditorMode.POINTER) {
                if (selectedBundleId != null) {
                    Point2D planPosition = new Point2D(event.getX(), event.getY());
                    larmanController.modifyBundlePosition(selectedBundleId, transformPlanCoordsToRealCoords(planPosition));
                    draw();
                }
            }
        });

        setOnMouseReleased(event -> {
            //Point2D clippedPoint = snapPointToGrid(new Point2D(event.getX(), event.getY()));
            if (event.getButton() == MouseButton.SECONDARY || event.getButton() == MouseButton.MIDDLE) {
                translateVector = translateVector.add(dragVector);
                dragVector = new Point2D(0, 0);
                draw();
            }
        });

        setOnMouseMoved(event -> {
            Point2D planPosition = new Point2D(event.getX(), event.getY());
            mousePositionInRealCoords = transformPlanCoordsToRealCoords(planPosition);
            String text = "x:" + MathHelper.round(mousePositionInRealCoords.getX(), 2) + "  "
                    + "y:" + MathHelper.round(mousePositionInRealCoords.getY(), 2);
            mousePositionLabel.setText(text);
        });

        setOnScroll(event -> {
            if (event.isControlDown()) handleZoom(event.getDeltaY(), new Point2D(event.getX(), event.getY()));
            else handlePanning(event);
        });

        setOnKeyPressed(event -> {
            if (event.isControlDown()) {
                double delta = event.getCode() == KeyCode.EQUALS ? 1 : event.getCode() == KeyCode.MINUS ? -1 : 0;
                handleZoom(delta, getPlanCenterCoords());
            }
        });
    }

    private void editorWindow(BundleDto dto){
        JavafxHelper.addEditorView("Editor", "Editor", false, dto);
        larmanController.modifyBundleProperties(dto.id, dto.barcode, dto.height, dto.width, dto.length, dto.time, dto.date, dto.essence, dto.plankSize, dto.angle);
    }
    private void handleZoom(double delta, Point2D position) {
        Point2D panningVector = position.subtract(getPlanCenterCoords()).multiply(ConfigHelper.zoomFactor - 1);
        if (delta > 0) {
            // ZOOM
            zoom *= ConfigHelper.zoomFactor;
            translateVector = translateVector.subtract(panningVector.multiply(1.0 / zoom));
        } else if (delta < 0) {
            // UNZOOM
            translateVector = translateVector.add(panningVector.multiply(1.0 / zoom));
            zoom /= ConfigHelper.zoomFactor;
        } else return;

        draw();
    }

    private void handlePanning(ScrollEvent event) {
        Point2D panningVector = new Point2D(
                event.getDeltaX() * 0.5 * (1.0 / zoom),
                event.getDeltaY() * 0.5 * (1.0 / zoom));
        translateVector = translateVector.add(panningVector);
        draw();
    }

    private Point2D transformRealCoordsToPlanCoords(Point2D realPosition) {
        Point2D zoomedVector = GeomHelper.invertY(realPosition).add(translateVector).add(dragVector);
        return zoomedVector.multiply(zoom).add(getPlanCenterCoords());
    }

    private Point2D transformPlanCoordsToRealCoords(Point2D planPosition) {
        Point2D withoutPlanOffset = planPosition.subtract(getPlanCenterCoords());
        Point2D unzoomed = withoutPlanOffset.multiply(1.0 / zoom);
        Point2D untranslated = unzoomed.subtract(translateVector).subtract(dragVector);
        return GeomHelper.invertY(untranslated);
    }

    private Point2D getPlanCenterCoords() {
        return new Point2D(getWidth() / 2.0, getHeight() / 2.0);
    }

    private void selectBundle() {
        BundleDto bundle = larmanController.getTopBundle(mousePositionInRealCoords);
        if (bundle != null) {
            mainController.updateBundleInfo(bundle);
            selectedBundleId = bundle.id;
        } else {
            selectedBundleId = null;
        }
        // TODO notify mainController for sideview
        // TODO Highlight selected bundles
    }

    private void createBundle() {
        larmanController.createBundle(mousePositionInRealCoords);
        mainController.editorMode.setValue(EditorMode.POINTER);
    }

    private void deleteBundle() {
        BundleDto topBundle = larmanController.getTopBundle(mousePositionInRealCoords);
        if (topBundle != null) larmanController.deleteBundle(topBundle.id);
        mainController.editorMode.setValue(EditorMode.POINTER);
    }

    public void draw() {
        getChildren().clear();

        List<BundleDto> bundles = larmanController.getBundles();

        drawAxes();
        drawBundles(bundles);
        drawOtherGraphics();
    }

    private void drawAxes() {
        Point2D realOriginOnPlan = transformRealCoordsToPlanCoords(new Point2D(0, 0));
        double screenWidth = getWidth();
        double screenHeight = getHeight();

        xAxis.setStartX(0);
        xAxis.setEndX(screenWidth);
        xAxis.setStartY(realOriginOnPlan.getY());
        xAxis.setEndY(realOriginOnPlan.getY());

        yAxis.setStartY(0);
        yAxis.setEndY(screenHeight);
        yAxis.setStartX(realOriginOnPlan.getX());
        yAxis.setEndX(realOriginOnPlan.getX());

        xAxis.setStroke(ColorHelper.setOpacity(Color.WHITE, 0.2));
        xAxis.getStrokeDashArray().add(10.0);
        yAxis.setStroke(ColorHelper.setOpacity(Color.WHITE, 0.2));
        yAxis.getStrokeDashArray().add(10.0);

        getChildren().add(xAxis);
        getChildren().add(yAxis);
    }

    private void drawBundles(List<BundleDto> bundles) {
        for (BundleDto bundleDto : bundles) {
            BundlePresenter bundlePresenter = new BundlePresenter(bundleDto);
            Point2D planPosition = transformRealCoordsToPlanCoords(bundleDto.position);
            bundlePresenter.setScale(zoom);
            bundlePresenter.setPosition(planPosition);
            getChildren().add(bundlePresenter.get());
        }
    }

    private void drawOtherGraphics() {
        getChildren().add(mousePositionLabel);
    }

//    private Point2D snapPointToGrid (Point2D point){
//        if(mainController.gridIsOn){
//            drawgrid();
//            Point2D realOriginOnPlan = transformRealCoordsToPlanCoords(new Point2D(point.getX(), point.getY()));
//
//    }
//        return point;
//    }
//
//    private void drawgrid() {
//        Point2D realOriginOnPlan = transformRealCoordsToPlanCoords(new Point2D(0, 0));
//        double screenWidth = getWidth();
//        double screenHeight = getHeight();
//        double rows = screenWidth / ConfigHelper.gridSquareSize;
//        double columns = screenHeight / ConfigHelper.gridSquareSize;
//
//        for(int i = 0; i < rows; i++)
//        {
//            xAxisGrid.setStroke(ColorHelper.setOpacity(Color.WHITE, 0.2));
//            xAxisGrid.getStrokeDashArray().add(10.0);
//            getChildren().add(xAxisGrid);
//        }
//        for(int j = 0; j < columns; j++)
//        {
//            yAxisGrid.setStroke(ColorHelper.setOpacity(Color.WHITE, 0.2));
//            yAxisGrid.getStrokeDashArray().add(10.0);
//            getChildren().add(yAxisGrid);
//        }
//    }
}