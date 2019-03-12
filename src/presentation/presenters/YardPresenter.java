package presentation.presenters;

import helpers.ColorHelper;
import helpers.ConfigHelper;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineJoin;

import java.util.ArrayList;
import java.util.List;

public class YardPresenter extends Pane implements IPresenter {
    private List<BundlePresenter> bundles;
    private LiftPresenter lift;
    private List<Rectangle> rectangles;
    private DoubleProperty zoom;
    private Point2D lastClickedPoint;
    private Point2D dragVector;
    private Point2D translateVector;

    private Label mousePosition;
    private Line xAxis;
    private Line yAxis;

    public YardPresenter() {
        super();
        setFocusTraversable(true);

        bundles = new ArrayList<>();
        rectangles = new ArrayList<>();
        zoom = new SimpleDoubleProperty(1.0);
        dragVector = new Point2D(0, 0);
        translateVector = new Point2D(0.0, 0.0);
        mousePosition = new Label("x:0  y:0");
        mousePosition.setAlignment(Pos.BOTTOM_RIGHT);// TODO Not working...
        xAxis = new Line();
        yAxis = new Line();

        initRectangles();
        initEventListeners();
        draw();
    }

    private void initEventListeners() {
        widthProperty().addListener(observable -> draw());
        heightProperty().addListener(observable -> draw());
        zoom.addListener(observable -> draw());

        setOnMousePressed(event -> {
            requestFocus();
            lastClickedPoint = new Point2D(event.getX(), event.getY());
        });
        setOnMouseDragged(event -> {
            Point2D newDraggedPoint = new Point2D(event.getX(), event.getY());
            dragVector = newDraggedPoint.subtract(lastClickedPoint).multiply(1 / zoom.getValue());
            draw();
        });
        setOnMouseReleased(event -> {
            translateVector = translateVector.add(dragVector);
            dragVector = new Point2D(0, 0);
            draw();
        });
        setOnMouseMoved(event -> {
            Point2D planPosition = new Point2D(event.getX(), event.getY());
            Point2D realPosition = transformPlanCoordsToRealCoords(planPosition);
            String text = "x:" + Math.round(realPosition.getX()) + "  " + "y:" + Math.round(realPosition.getY());
            mousePosition.setText(text);
        });
        setOnScroll(event -> {
            if (event.isControlDown()) handleZoom(event.getDeltaY());
            else handlePanning(event);
        });
        setOnKeyPressed(event -> {
            if (event.isControlDown()) {
                double delta = event.getCode() == KeyCode.EQUALS ? 1 : event.getCode() == KeyCode.MINUS ? -1 : 0;
                handleZoom(delta);
            }
        });
    }

    private void handleZoom(double delta) {
        if (delta > 0) {
            zoom.setValue(zoom.getValue() * 1.25);
        } else if (delta < 0) {
            zoom.setValue(zoom.getValue() * 0.75);
        }
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
        Point2D invertedPosition = new Point2D(realPosition.getX(), -realPosition.getY());
        Point2D screenOffsetVector = new Point2D(getWidth() / 2.0, getHeight() / 2.0);
        Point2D zoomedVector = invertedPosition.add(translateVector).add(dragVector);
        return zoomedVector.multiply(zoom.getValue()).add(screenOffsetVector);
    }

    private Point2D transformPlanCoordsToRealCoords(Point2D planPosition) {
        Point2D screenOffsetVector = new Point2D(getWidth() / 2.0, getHeight() / 2.0);
        Point2D withoutScreenOffset = planPosition.subtract(screenOffsetVector);
        Point2D unzoomed = withoutScreenOffset.multiply(1.0 / zoom.getValue());
        Point2D untranslated = unzoomed.subtract(translateVector).subtract(dragVector);
        return new Point2D(untranslated.getX(), -untranslated.getY());
    }

    private void initRectangles() {
        Rectangle rec1 = new Rectangle(40, 40, 80, 60);
        rec1.setRotate(30);
        initRectangleColors(rec1);

        Rectangle rec2 = new Rectangle(-40, -40,80, 120);
        rec2.setRotate(251);
        initRectangleColors(rec2);

        Rectangle rec3 = new Rectangle(0, 0,70, 60);
        rec3.setRotate(0);
        initRectangleColors(rec3);

        rectangles.add(rec1);
        rectangles.add(rec3);
        rectangles.add(rec2);
    }

    private void initRectangleColors(Rectangle rec) {
        Color color = ColorHelper.randomColor(ConfigHelper.bundleSaturation, ConfigHelper.bundleBrightness);
        rec.setStrokeWidth(ConfigHelper.bundleBorderWidth);
        rec.setStroke(color);
        rec.setStrokeLineJoin(StrokeLineJoin.ROUND);
        rec.setFill(ColorHelper.setOpacity(color, ConfigHelper.bundleOpacity));
    }

    public void draw() {
        getChildren().clear();

        drawAxes();
        drawBundles();
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

    private void drawBundles() {
        for (Rectangle rec : rectangles) {
            Rectangle recCopy = new Rectangle(rec.getWidth() * zoom.getValue(), rec.getHeight() * zoom.getValue());
            Point2D newPos = transformRealCoordsToPlanCoords(rec);
            recCopy.setX(newPos.getX());
            recCopy.setY(newPos.getY());
            recCopy.setFill(rec.getFill());
            recCopy.setRotate(rec.getRotate());
            recCopy.setFill(rec.getFill());
            recCopy.setStroke(rec.getStroke());
            recCopy.setStrokeWidth(rec.getStrokeWidth() * zoom.getValue());
            recCopy.setStrokeLineJoin(rec.getStrokeLineJoin());
            getChildren().add(recCopy);
        }
    }

    private void drawOtherGraphics() {
        getChildren().add(mousePosition);
    }
}
