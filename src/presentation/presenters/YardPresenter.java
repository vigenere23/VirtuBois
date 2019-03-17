package presentation.presenters;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import domain.entities.Bundle;
import enums.EditorMode;
import helpers.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import presentation.Main;
import presentation.controllers.EditorController;
import presentation.controllers.IController;
import presentation.controllers.MainController;

import java.util.ArrayList;
import java.util.List;

public class YardPresenter extends Pane implements IPresenter {
    private List<BundlePresenter> bundles;
    private LiftPresenter lift;
    private double zoom;
    private Point2D lastClickedPoint;
    private Point2D dragVector;
    private Point2D translateVector;

    private Label mousePosition;
    private Line xAxis;
    private Line yAxis;

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
        mousePosition = new Label("x:0  y:0");
        mousePosition.setAlignment(Pos.BOTTOM_RIGHT);// TODO Not working...
        xAxis = new Line();
        yAxis = new Line();

        initEventListeners();
        draw();
    }

    private void initEventListeners() {
        widthProperty().addListener(observable -> draw());
        heightProperty().addListener(observable -> draw());

        setOnMousePressed(event -> {
            requestFocus();
            mainController.clearAllBundleInfo();
            if (event.getButton() == MouseButton.SECONDARY || event.getButton() == MouseButton.MIDDLE) {
                lastClickedPoint = new Point2D(event.getX(), event.getY());
            } else if (mainController.editorMode.getValue() == EditorMode.ADDING_BUNDLE) {
                createBundle(new Point2D(event.getX(), event.getY()));
                draw();
            } else if (mainController.editorMode.getValue() == EditorMode.POINTER) {
                getSelectedPacks(new Point2D(event.getX(), event.getY()));
            } else if (mainController.editorMode.getValue() == EditorMode.DELETE) {
                deleteBundle(new Point2D(event.getX(), event.getY()));
                draw();
            } else if (mainController.editorMode.getValue() == EditorMode.EDIT) {
                Point2D point = transformPlanCoordsToRealCoords(new Point2D(event.getX(), event.getY()));
                if (larmanController.getSelectedBundles(point).size() != 0) {
                    BundleDto dto = larmanController.getTopBundle(point);
                    EditorController editorController = JavafxHelper.addEditorView("Editor", "Editor", false, dto);
                    BundleDto modded = editorController.getBundleDto();
                    System.out.print(modded.plankSize);
                }
            }
        });

        setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.SECONDARY || event.getButton() == MouseButton.MIDDLE) {
                Point2D newDraggedPoint = new Point2D(event.getX(), event.getY());
                dragVector = newDraggedPoint.subtract(lastClickedPoint).multiply(1 / zoom);
                draw();
            }
            if (event.getButton() == MouseButton.PRIMARY && mainController.editorMode.getValue() == EditorMode.POINTER) {
                //TODO
                Point2D newDraggedPoint = new Point2D(event.getX(), event.getY());
                /*List<BundleDto> bundles = larmanController.getSelectedBundles(transformPlanCoordsToRealCoords(newDraggedPoint));
                if (bundles.size() == 1){
                    larmanController.modifyBundlePosition(bundles.get(0).id, transformPlanCoordsToRealCoords(newDraggedPoint));
                    draw();
                }*/
                if (larmanController.getSelectedBundles(transformPlanCoordsToRealCoords(newDraggedPoint)).size() != 0) {
                    BundleDto bundle = larmanController.getTopBundle(transformPlanCoordsToRealCoords(newDraggedPoint));
                    String id = getTopBundleStack(transformPlanCoordsToRealCoords(newDraggedPoint));
                    if (bundle.id == id) {
                        larmanController.modifyBundlePosition(bundle.id, transformPlanCoordsToRealCoords(newDraggedPoint));
                        draw();
                    }
                }
            }
        });

        setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.SECONDARY || event.getButton() == MouseButton.MIDDLE) {
                translateVector = translateVector.add(dragVector);
                dragVector = new Point2D(0, 0);
                draw();
            }
        });
        setOnMouseMoved(event -> {
            Point2D planPosition = new Point2D(event.getX(), event.getY());
            Point2D realPosition = transformPlanCoordsToRealCoords(planPosition);
            String text = "x:" + MathHelper.round(realPosition.getX(), 2) + "  "
                    + "y:" + MathHelper.round(realPosition.getY(), 2);
            mousePosition.setText(text);
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

    private void getSelectedPacks(Point2D position) {
        List<BundleDto> bundles = larmanController.getSelectedBundles(transformPlanCoordsToRealCoords(position));
        if (bundles.size() == 1) {
            mainController.updateBundleInfo(bundles.get(0));
        }
        // TODO notify mainController for sideview
        // TODO Highlight selected bundles
    }

    private String getTopBundleStack(Point2D position) {
        //TODO
        BundleDto bundle = larmanController.getTopBundle(position);
        BundlePresenter maxBundlePresenter = new BundlePresenter(bundle);
        List<BundleDto> bundlesdto = larmanController.getBundles();
        for (BundleDto currentDto : bundlesdto) {
            BundlePresenter newBundlePresenter = new BundlePresenter(currentDto);
            if (GeomHelper.rectangleCollidesRectangle(newBundlePresenter, maxBundlePresenter)) {
                if (maxBundlePresenter.dto.height < currentDto.height) {
                    System.out.println(88);
                    maxBundlePresenter = newBundlePresenter;
                }
            }
        }
        return maxBundlePresenter.dto.id;
    }

    private void createBundle(Point2D planPosition) {
        Point2D realPosition = transformPlanCoordsToRealCoords(planPosition);
        larmanController.createBundle(realPosition);
        mainController.editorMode.setValue(EditorMode.POINTER);
    }

    private void deleteBundle(Point2D planPosition) {

        Point2D realPos = transformPlanCoordsToRealCoords(planPosition);
        if (larmanController.getSelectedBundles(realPos).size() > 0) {
            String id = larmanController.getTopBundle(realPos).id;
            larmanController.deleteBundle(id);
        }
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
        getChildren().add(mousePosition);
    }
}