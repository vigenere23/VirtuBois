package presentation.presenters;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import enums.EditorMode;
import helpers.ColorHelper;
import helpers.ConfigHelper;
import helpers.GeomHelper;
import helpers.MathHelper;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import presentation.controllers.MainController;

import java.util.ArrayList;
import java.util.List;

public class YardPresenter extends Pane implements IPresenter {
    private List<BundlePresenter> bundles;
    private LiftPresenter lift;
    private DoubleProperty zoom;
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
        zoom = new SimpleDoubleProperty(ConfigHelper.defaultZoom);
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
            if (event.getButton() == MouseButton.SECONDARY || event.getButton() == MouseButton.MIDDLE) {
                lastClickedPoint = new Point2D(event.getX(), event.getY());
            }
            else if (mainController.editorMode == EditorMode.ADDING_BUNDLE) {
                createBundle(new Point2D(event.getX(), event.getY()));
                draw();
            }
        });
        setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.SECONDARY || event.getButton() == MouseButton.MIDDLE) {
                Point2D newDraggedPoint = new Point2D(event.getX(), event.getY());
                dragVector = newDraggedPoint.subtract(lastClickedPoint).multiply(1 / zoom.getValue());
                draw();
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
            if (event.getCode() == KeyCode.ESCAPE) {
                mainController.editorMode = EditorMode.NONE;
            }
        });
    }

    private void handleZoom(double delta, Point2D position) {
        Point2D panningVector = position.subtract(getPlanCenterCoords()).multiply(ConfigHelper.zoomFactor - 1);
        if (delta > 0) {
            // ZOOM
            zoom.setValue(zoom.getValue() * ConfigHelper.zoomFactor);
            translateVector = translateVector.subtract(panningVector.multiply(1 / zoom.getValue()));
        } else if (delta < 0) {
            // UNZOOM
            translateVector = translateVector.add(panningVector.multiply(1 / zoom.getValue()));
            zoom.setValue(zoom.getValue() / ConfigHelper.zoomFactor);
        } else return;

        draw();
    }

    private void handlePanning(ScrollEvent event) {
        Point2D panningVector = new Point2D(
                event.getDeltaX() * 0.5 * (1 / zoom.getValue()),
                event.getDeltaY() * 0.5 * (1 / zoom.getValue()));
        translateVector = translateVector.add(panningVector);
        draw();
    }

    private Point2D transformRealCoordsToPlanCoords(Rectangle rec) {
        Point2D position = new Point2D(rec.getX(), rec.getY());
        Point2D centerOffset = new Point2D(rec.getWidth() / 2.0, -rec.getHeight() / 2.0);
        return transformRealCoordsToPlanCoords(position.subtract(centerOffset));
    }

    private Point2D transformRealCoordsToPlanCoords(Point2D realPosition) {
        Point2D zoomedVector = GeomHelper.invertY(realPosition).add(translateVector).add(dragVector);
        return zoomedVector.multiply(zoom.getValue()).add(getPlanCenterCoords());
    }

    private Point2D transformPlanCoordsToRealCoords(Point2D planPosition) {
        Point2D withoutPlanOffset = planPosition.subtract(getPlanCenterCoords());
        Point2D unzoomed = withoutPlanOffset.multiply(1.0 / zoom.getValue());
        Point2D untranslated = unzoomed.subtract(translateVector).subtract(dragVector);
        return GeomHelper.invertY(untranslated);
    }

    private Point2D getPlanCenterCoords() {
        return new Point2D(getWidth() / 2.0, getHeight() / 2.0);
    }

    private void createBundle(Point2D planPosition) {
        Point2D realPosition = transformPlanCoordsToRealCoords(planPosition);
        larmanController.createBundle(realPosition);
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
        for (BundleDto bundle : bundles) {
            BundlePresenter bundlePresenter = new BundlePresenter(bundle);
            Point2D planPosition = transformRealCoordsToPlanCoords(bundlePresenter);
            bundlePresenter.setX(planPosition.getX());
            bundlePresenter.setY(planPosition.getY());
            bundlePresenter.setWidth(bundlePresenter.getWidth() * zoom.getValue());
            bundlePresenter.setHeight(bundlePresenter.getHeight() * zoom.getValue());
            getChildren().add(bundlePresenter);
        }
    }

    private void drawOtherGraphics() {
        getChildren().add(mousePosition);
    }
}
