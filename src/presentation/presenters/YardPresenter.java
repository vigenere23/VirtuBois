package presentation.presenters;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import domain.dtos.LiftDto;
import domain.entities.Bundle;
import domain.entities.Lift;
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
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import presentation.controllers.MainController;

import java.util.List;

public class YardPresenter extends Pane implements IPresenter, Cloneable {
    private double zoom;
    private Point2D lastClickedPoint;
    private Point2D dragVector;
    private Point2D translateVector;
    private Point2D selectionOffsetVector;

    private BundleDto topSelectedBundle;
    private LiftPresenter liftPresenter;
    private boolean canDrag;
    private DropShadow dropShadow;

    private Point2D mousePositionInRealCoords;
    private Label mousePositionLabel;
    private Line xAxis;
    private Line yAxis;

    private MainController mainController;
    private LarmanController larmanController;
    private boolean shouldUpdate;

    public YardPresenter(MainController mainController) {
        super();
        setFocusTraversable(false);
        this.mainController = mainController;

        larmanController = mainController.larmanController;
        zoom = ConfigHelper.defaultZoom;
        canDrag = true;
        canDrag = false;
        shouldUpdate = false;
        dragVector = new Point2D(0, 0);
        translateVector = new Point2D(0, 0);
        selectionOffsetVector = new Point2D(0, 0);
        mousePositionLabel = new Label("x:0  y:0");
        mousePositionLabel.setAlignment(Pos.BOTTOM_RIGHT);
        xAxis = new Line();
        yAxis = new Line();
        liftPresenter = new LiftPresenter(larmanController.getLift());

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
                    if (topSelectedBundle != null) {
                        if (isOverAll(topSelectedBundle)) {
                            canDrag = true;
                        }
                    }
                    break;
                case ADDING_BUNDLE:
                    createBundle();
                    break;
                case DELETE:
                    updateSelectedBundles();
                    if (topSelectedBundle != null) {
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
                if (canDrag) {
                    shouldUpdate = true;
                    Point2D newBundlePosition = mainController.gridIsOn
                        ? positionInGrid(mousePositionInRealCoords)
                        : mousePositionInRealCoords.subtract(selectionOffsetVector);

                    larmanController.modifyBundlePosition(topSelectedBundle.id, newBundlePosition);
                    draw();
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
            if (shouldUpdate) {
                updateSelectedBundles();
            }
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
        if(event.getCode().equals(KeyCode.D)){
           liftPresenter.turnRight();
           draw();
        }
        if(event.getCode().equals(KeyCode.A)){
            liftPresenter.turnLeft();
            draw();
        }
        if(event.getCode().equals(KeyCode.W)){
            liftPresenter.forward();
            draw();
        }
        if(event.getCode().equals(KeyCode.S)){
            liftPresenter.backward();
            draw();
        }
        /*if (event.getCode().equals(KeyCode.UP)){
            UndoRedo.add(larmanController.getYard());
            LiftDto liftDtoUp = new LiftDto(larmanController.getYard().getLift());
            larmanController.getYard().setLift(larmanController.moveForward(liftDtoUp).position);
            draw();
        }
        if (event.getCode().equals(KeyCode.DOWN)){
            UndoRedo.add(larmanController.getYard());
            LiftDto liftDtoDown = new LiftDto(larmanController.getYard().getLift());
            larmanController.getYard().setLift(larmanController.moveBackward(liftDtoDown).position);
            draw();
        }
        if (event.getCode().equals(KeyCode.LEFT)){
            UndoRedo.add(larmanController.getYard());
            LiftDto liftDtoLeft = new LiftDto(larmanController.getYard().getLift());
            larmanController.getYard().setLift(larmanController.turnLeft(liftDtoLeft).position);
            draw();
        }
        if (event.getCode().equals(KeyCode.RIGHT)) {
            UndoRedo.add(larmanController.getYard());
            LiftDto liftDtoRight = new LiftDto(larmanController.getYard().getLift());
            larmanController.getYard().setLift(larmanController.turnRight(liftDtoRight).position);
            draw();
        }
        */
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
            //mainController.updateElevationView(topSelectedBundle);
            mainController.setFocusedBundleElevView(topSelectedBundle);
            mainController.updateBundleInfo(topSelectedBundle);
            mainController.setFocusedBundleElevView(topSelectedBundle);
            selectionOffsetVector = mousePositionInRealCoords.subtract(topSelectedBundle.position);
        } else {
            topSelectedBundle = null;
            mainController.clearElevationView();
            mainController.clearAllBundleInfo();
            selectionOffsetVector = new Point2D(0, 0);
        }
    }

    private void createBundle() {
        UndoRedo.add(larmanController.getYard());
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
        int x = (int)(point.getX()/ConfigHelper.gridSquareSize) * ConfigHelper.gridSquareSize;
        if (x >= 0) {
            x += ConfigHelper.gridSquareSize;
        }
        int y = (int)(point.getY()/ConfigHelper.gridSquareSize) * ConfigHelper.gridSquareSize;
        if (y <= 0) {
            y -= ConfigHelper.gridSquareSize;
        }
        int prevX = x - ConfigHelper.gridSquareSize;
        int prevY = y + ConfigHelper.gridSquareSize;
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
        UndoRedo.add(larmanController.getYard());
        larmanController.deleteBundle(id);
        mainController.editorMode.setValue(EditorMode.POINTER);
        mainController.clearAllBundleInfo();
        mainController.addTableViewBundles(larmanController.getBundles());
        mainController.clearElevationView();
        draw();
    }

    public void draw() {
        if(UndoRedo.getUndo() == 0) {
            mainController.undoButton.setDisable(true);
        } else {
            mainController.undoButton.setDisable(false);
        }
        if (UndoRedo.getRedo() == 0) {
            mainController.redoButton.setDisable(true);
        } else {
            mainController.redoButton.setDisable(false);
        }
        getChildren().clear();
        drawAxes();
        if (mainController.gridIsOn) {
            drawGrid();
        }
        drawLift();
        drawBundles(larmanController.getBundlesSortedZ());
        mainController.clearTableView();
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
                if (bundleDto.equals(topSelectedBundle)){
                    bundlePresenter.getRectangle().setEffect(dropShadow);
                }
            }
            getChildren().add(bundlePresenter.getRectangle());
        }
    }

    private void drawLift(){
        Point2D planPosition = transformRealCoordsToPlanCoords(liftPresenter.dto.position);
        liftPresenter.setScale(zoom);
        liftPresenter.setPosition(planPosition);
        getChildren().add(liftPresenter.getRectangle());
    }

    private void drawOtherGraphics() {
        getChildren().add(mousePositionLabel);
    }

    private void drawGrid() {
        double screenWidth = getWidth();
        double screenHeight = getHeight();
        Point2D coord1 = transformPlanCoordsToRealCoords(new Point2D(0,0));
        Point2D coord2 = transformPlanCoordsToRealCoords(new Point2D(screenWidth, screenHeight));

        int nextX = (int)(coord1.getX()/ConfigHelper.gridSquareSize) * ConfigHelper.gridSquareSize;
        int nextY = (int)(coord1.getY()/ConfigHelper.gridSquareSize) * ConfigHelper.gridSquareSize;

        for (int x = nextX; x <= coord2.getX(); x += ConfigHelper.gridSquareSize) {
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

        for (int y = nextY; y >= coord2.getY(); y -= ConfigHelper.gridSquareSize) {
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

    private boolean isOverAll(BundleDto bundleToCheck)
    {
        List<BundleDto> bundlesToCompare = larmanController.getCollidingBundles(bundleToCheck);
        for (BundleDto bundleDto : bundlesToCompare)
        {
            if (bundleDto.z > bundleToCheck.z) return false;
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

    private boolean checkIfColliding(LiftDto lift) {
        boolean isColliding = false;
        List<BundleDto> bundlesToCheck = larmanController.getBundles();
        CenteredRectangle rectangleLiftArms = new CenteredRectangle(lift.armsPosition.getX() + lift.armsLength/2, lift.armsPosition.getY() + lift.armsLength/2, lift.armsWidth, lift.armsLength, lift.angle);
        for(BundleDto bundleDto: bundlesToCheck){
            CenteredRectangle rectangleBundle = new CenteredRectangle(bundleDto.position.getX(), bundleDto.position.getY(), bundleDto.width, bundleDto.length, bundleDto.angle);
            if (GeomHelper.rectangleCollidesRectangle(rectangleLiftArms, rectangleBundle)){
                isColliding = true;
            }
            else {
                isColliding = false;
            }
        }
        return isColliding;
    }
}
