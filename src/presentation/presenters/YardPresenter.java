package presentation.presenters;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import domain.entities.Yard;
import enums.EditorMode;
import helpers.*;
import helpers.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import presentation.controllers.MainController;

import java.util.*;
import java.util.List;

public class YardPresenter extends Pane implements IPresenter {
    private List<BundlePresenter> bundles;
    private LiftPresenter lift;
    private double zoom;
    private Point2D lastClickedPoint;
    private Point2D dragVector;
    private Point2D translateVector;
    private Point2D selectionOffsetVector;

    private BundleDto topSelectedBundle;
    private boolean canDrag;
    DropShadow dropShadow;
    private int gridDimension = 1;

    private Point2D mousePositionInRealCoords;
    private Label mousePositionLabel;
    private Line xAxis;
    private Line yAxis;

    private MainController mainController;
    private LarmanController larmanController;

    public YardPresenter(MainController mainController) {
        super();
        setFocusTraversable(true);

        this.mainController = mainController;

        larmanController = mainController.larmanController;
        bundles = new ArrayList<>();
        zoom = ConfigHelper.defaultZoom;
        dragVector = new Point2D(0, 0);
        translateVector = new Point2D(0, 0);
        selectionOffsetVector = new Point2D(0, 0);
        mousePositionLabel = new Label("x:0  y:0");
        mousePositionLabel.setAlignment(Pos.BOTTOM_RIGHT);// TODO Not working...
        xAxis = new Line();
        yAxis = new Line();

        dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setColor(Color.GREY);

        initEventListeners();
        draw();
    }

    private void initEventListeners() {
        widthProperty().addListener(observable -> draw());
        heightProperty().addListener(observable -> draw());

        setOnMousePressed(this::handleOnMousePressedEvent);
        setOnMouseDragged(this::handleOnMouseDraggedEvent);
        setOnMouseReleased(this::handleOnMouseReleasedEvent);
        setOnMouseMoved(this::updateMousePosition);
        setOnScroll(this::handleOnScrollEvent);
        setOnKeyPressed(this::handleOnKeyPressedEvent);
    }

    private void handleOnMousePressedEvent(MouseEvent event) {
        requestFocus();
        if (event.getButton() == MouseButton.SECONDARY || event.getButton() == MouseButton.MIDDLE) {
            lastClickedPoint = new Point2D(event.getX(), event.getY());
        } else {
            switch (mainController.editorMode.getValue()) {
                case POINTER:
                    updateSelectedBundles();
                    break;
                case ADDING_BUNDLE:
                    createBundle();
                    break;
                case DELETE:
                    updateSelectedBundles();
                    if(topSelectedBundle != null) {
                        if (isOverAll(topSelectedBundle)) {
                            deleteBundle(topSelectedBundle.id);
                        }
                    }
                    break;
            }
        }
    }

    private void handleOnMouseDraggedEvent(MouseEvent event) {
        updateMousePosition(event);
        if (event.getButton() == MouseButton.SECONDARY || event.getButton() == MouseButton.MIDDLE) {
            Point2D newDraggedPoint = new Point2D(event.getX(), event.getY());
            dragVector = newDraggedPoint.subtract(lastClickedPoint).multiply(1 / zoom);
            draw();
        } else {
            if (mainController.editorMode.getValue() == EditorMode.POINTER) {
                if (topSelectedBundle != null) {
                    if(isOverAll(topSelectedBundle)) {
                        canDrag = true;
                    }
                    if(canDrag){
                        Point2D planPosition = transformPlanCoordsToRealCoords(new Point2D(event.getX(), event.getY()));
                        if (!mainController.gridIsOn) {
                            larmanController.modifyBundlePosition(topSelectedBundle.id, planPosition);
                        }else {
                            larmanController.modifyBundlePosition(topSelectedBundle.id, positionInGrid(planPosition));
                        }
                        draw();
                    }
                }
            }
        }
    }

    private void handleOnMouseReleasedEvent(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY || event.getButton() == MouseButton.MIDDLE) {
            translateVector = translateVector.add(dragVector);
            dragVector = new Point2D(0, 0);
            draw();
        } else {
            updateSelectedBundles();
            canDrag = false;
            draw();
        }
    }

    private void handleOnScrollEvent(ScrollEvent event) {
        if (event.isControlDown()) handleZoom(event.getDeltaY(), new Point2D(event.getX(), event.getY()));
        else handlePanning(event);
    }

    private void handleOnKeyPressedEvent(KeyEvent event) {
        if (event.isControlDown()) {
            double delta = event.getCode() == KeyCode.EQUALS ? 1 : event.getCode() == KeyCode.MINUS ? -1 : 0;
            handleZoom(delta, getPlanCenterCoords());
        }
    }

    private void updateMousePosition(MouseEvent event) {
        Point2D planPosition = new Point2D(event.getX(), event.getY());
        mousePositionInRealCoords = transformPlanCoordsToRealCoords(planPosition);
        String text = "x:" + MathHelper.round(mousePositionInRealCoords.getX(), 2) + "  "
                + "y:" + MathHelper.round(mousePositionInRealCoords.getY(), 2);
        mousePositionLabel.setText(text);
    }

    private void showBundleEditorWindow(BundleDto bundleDto) {
        JavafxHelper.popupBundleEditorView(bundleDto); // BLOCKING!!!
        larmanController.modifyBundleProperties(bundleDto);
        draw();
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

    private void updateSelectedBundles() {
        List<BundleDto> selectedBundles = larmanController.getSelectedBundles(mousePositionInRealCoords);
        if (!selectedBundles.isEmpty()) {
            topSelectedBundle = larmanController.getTopBundle(mousePositionInRealCoords);
            mainController.updateElevationView(selectedBundles);
            //mainController.updateBundleInfo(topSelectedBundle);
            selectionOffsetVector = mousePositionInRealCoords.subtract(topSelectedBundle.position);
        } else {
            topSelectedBundle = null;
            mainController.clearElevationView();
            mainController.clearAllBundleInfo();
            selectionOffsetVector = new Point2D(0, 0);
        }
    }

    private void createBundle() {
        if (mainController.gridIsOn) {
            BundleDto createdBundle = larmanController.createBundle(positionInGrid(mousePositionInRealCoords));
            showBundleEditorWindow(createdBundle);
            selectBundle(createdBundle);
        }
        else {
            BundleDto createdBundle = larmanController.createBundle(mousePositionInRealCoords);
            showBundleEditorWindow(createdBundle);
            selectBundle(createdBundle);
        }
        draw();
    }

    private Point2D positionInGrid(Point2D point) {
        int x = (int)(point.getX()/gridDimension) * gridDimension;
        if (x >= 0) {
            x += gridDimension;
        }
        int y = (int)(point.getY()/gridDimension) * gridDimension;
        if (y <= 0) {
            y -= gridDimension;
        }
        int prevX = x - gridDimension;
        int prevY = y + gridDimension;
        if (Math.abs(point.getX() - x) > Math.abs(point.getX() - prevX)) {
            x = prevX;
        }
        if (Math.abs(point.getY() - y) > Math.abs(point.getY() - prevY)) {
            y = prevY;
        }
        return new Point2D(x, y);
    }

    private void selectBundle(BundleDto bundleDto) {
        mainController.editorMode.setValue(EditorMode.POINTER);
        mousePositionInRealCoords = bundleDto.position;
        updateSelectedBundles();
    }

    private void deleteBundle(String id) {
        larmanController.deleteBundle(id);
        mainController.editorMode.setValue(EditorMode.POINTER);
        draw();
    }

    public void draw() {
        getChildren().clear();
        drawAxes();
        if (mainController.gridIsOn) {
            drawGrid();
        }
        drawBundles(larmanController.getBundlesSorted());
        if(!larmanController.getBundles().isEmpty()) {
            mainController.addTableViewBundles(larmanController.getBundles());
        }
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
            if (topSelectedBundle != null){
                if (bundleDto.id == topSelectedBundle.id){
                    bundlePresenter.get().setEffect(dropShadow);
                }
            }
            getChildren().add(bundlePresenter.get());
        }
    }

    private void drawOtherGraphics() {
        getChildren().add(mousePositionLabel);
    }

    private void drawGrid() {
        double screenWidth = getWidth();
        double screenHeight = getHeight();
        Point2D coord1 = transformPlanCoordsToRealCoords(new Point2D(0,0));
        Point2D coord2 = transformPlanCoordsToRealCoords(new Point2D(screenWidth, screenHeight));

        int nextX = (int)(coord1.getX()/gridDimension) * gridDimension;
        int nextY = (int)(coord1.getY()/gridDimension) * gridDimension;

        for (int x = nextX; x <= coord2.getX(); x += gridDimension){
            Point2D pointX = transformRealCoordsToPlanCoords(new Point2D(x, 0));
            Line line = new Line();
            line.setStartX(pointX.getX());
            line.setStartY(0);
            line.setEndX(pointX.getX());
            line.setEndY(screenHeight);

            line.setStroke(ColorHelper.setOpacity(Color.WHITE, 0.2));
            line.getStrokeDashArray().add(3.0);
            getChildren().add(line);

        }

        for (int y = nextY; y >= coord2.getY(); y -= gridDimension){
            Point2D pointY = transformRealCoordsToPlanCoords(new Point2D(0, y));
            Line line = new Line();
            line.setStartY(pointY.getY());
            line.setStartX(0);
            line.setEndY(pointY.getY());
            line.setEndX(screenWidth);

            line.setStroke(ColorHelper.setOpacity(Color.WHITE, 0.2));
            line.getStrokeDashArray().add(3.0);
            getChildren().add(line);
        }
    }

    private boolean isOverAll(BundleDto bundle)
    {
        List<BundleDto> bundlesToCompare = larmanController.getCollidingBundles(bundle);
        for (BundleDto i : bundlesToCompare)
        {
            if(i.z > bundle.z) {return false;}
        }
        return true;
    }

    public void setTopSelectedBundle(BundleDto bundle)
    {
        topSelectedBundle = bundle;
        draw();
    }

    public BundleDto getTopSelectedBundle(){
        return topSelectedBundle;
    }

    public Yard getYard(){
        return getYard();
    }
}
