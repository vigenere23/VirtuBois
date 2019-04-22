package presentation.presenters;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import helpers.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import presentation.controllers.MainController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElevationViewPresenter extends Pane implements IPresenter {
    private LarmanController larmanController;
    private MainController mainController;
    private DropShadow dropShadow;
    private List<BundleDto> allBundles;
    private Map<Rectangle, BundleDto> rectangleBundleDtoMap;
    private Map<BundleDto, Rectangle> dtoToRectangleMap;

    public ElevationViewPresenter(MainController mainController) {
        super();
        setFocusTraversable(true);
        this.larmanController = mainController.larmanController;
        this.mainController = mainController;

        allBundles = new ArrayList<>();
        rectangleBundleDtoMap = new HashMap<>();
        dtoToRectangleMap = new HashMap<>();

        dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setColor(Color.GREY);
    }

    private void initEventHandlers() {
        widthProperty().addListener(observable -> draw());
        heightProperty().addListener(observable -> draw());
    }

    public void setBundles(BundleDto bundle) {
        clearBundles();
        allBundles = larmanController.getAllCollidingBundles(bundle);
        draw();
    }

    public void clearBundles() {
        allBundles.clear();
        rectangleBundleDtoMap.clear();
        dtoToRectangleMap.clear();
        draw();
    }

    public void setFocusedBundle(BundleDto bundle) {
        for (Map.Entry<Rectangle, BundleDto> entry : rectangleBundleDtoMap.entrySet()) {
            entry.getKey().setEffect(null);
        }
        dtoToRectangleMap.get(bundle).setEffect(dropShadow);
    }

    private void drawBundlesAxisX() {
        double height = getHeight();
        double width = getWidth();
        double minWidth = allBundles.get(0).position.getX();
        double maxWidth = allBundles.get(0).position.getX();
        double maxHeight = allBundles.get(0).z + allBundles.get(0).height;
        for (BundleDto bundle : allBundles) {
            BundlePresenter presenter = new BundlePresenter(bundle);
            for (Point2D position : presenter.getPoints()) {
                if (position.getX() < minWidth) {
                    minWidth = position.getX();
                }
                if (position.getX() > maxWidth) {
                    maxWidth = position.getX();
                }
                if ((bundle.height + bundle.z > maxHeight)) {
                    maxHeight = bundle.height + bundle.z;
                }
            }
        }

        double scaleZ = height / maxHeight;
        double scaleX = width / (maxWidth - minWidth);

        for (BundleDto presenter : allBundles) {
            double xPos = 0;
            double zPos = 0;
            if (minWidth < 0) {
                xPos = (presenter.position.getX() + Math.abs(minWidth)) * scaleX;
            } else {
                xPos = (presenter.position.getX() - minWidth) * scaleX;
            }

            zPos = (maxHeight - presenter.z) * scaleZ;

            BundlePresenter bundlePresenter = new BundlePresenter(presenter);
            double minX = bundlePresenter.getPoints().get(0).getX();
            double maxX = bundlePresenter.getPoints().get(0).getX();
            for (Point2D summit : bundlePresenter.getPoints()) {
                if (summit.getX() < minX) {
                    minX = summit.getX();
                } else if (summit.getX() > maxX) {
                    maxX = summit.getX();

                }
            }
            double oldAngle = presenter.angle;
            presenter.angle = 0.;
            BundlePresenter bundleToShow = new BundlePresenter(presenter);
            Rectangle rectangle = bundleToShow.getRectangle();
            rectangle.setWidth((maxX - minX) * scaleX);
            rectangle.setHeight(presenter.height * scaleZ);
            rectangle.setX(xPos - rectangle.getWidth() / 2);
            rectangle.setY(zPos - rectangle.getHeight());
            if (presenter.id == mainController.getYardPresenter().getTopSelectedBundle().id) {
                rectangle.setEffect(dropShadow);
            }
            presenter.angle = oldAngle;
            rectangleBundleDtoMap.put(rectangle, presenter);
            dtoToRectangleMap.put(presenter, rectangle);

            rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                for (Map.Entry<Rectangle, BundleDto> entry : rectangleBundleDtoMap.entrySet()) {
                    entry.getKey().setEffect(null);
                }
                if (event.getButton() == MouseButton.PRIMARY) {
                    mainController.updateBundleInfo(rectangleBundleDtoMap.get(rectangle));
                    rectangle.setEffect(dropShadow);
                    mainController.getYardPresenter().setTopSelectedBundle(rectangleBundleDtoMap.get(rectangle));
                }
            });
            getChildren().add(rectangle);
        }
    }

    private void drawBundlesAxisY() {
        double height = getHeight();
        double width = getWidth();
        double minLength = allBundles.get(0).position.getY();
        double maxLength = allBundles.get(0).position.getY();
        double maxHeight = allBundles.get(0).z + allBundles.get(0).height;
        for (BundleDto bundle : allBundles) {
            BundlePresenter presenter = new BundlePresenter(bundle);
            for (Point2D position : presenter.getPoints()) {
                if (position.getY() < minLength) {
                    minLength = position.getY();
                }
                if (position.getY() > maxLength) {
                    maxLength = position.getY();
                }
                if ((bundle.height + bundle.z > maxHeight)) {
                    maxHeight = bundle.height + bundle.z;
                }
            }
        }

        double scaleZ = height / maxHeight;
        double scaleY = width / (maxLength - minLength);

        for (BundleDto presenter : allBundles) {
            double yPos = 0;
            double zPos = 0;
            if (minLength < 0) {
                yPos = (presenter.position.getY() + Math.abs(minLength)) * scaleY;
            } else {
                yPos = (presenter.position.getY() - minLength) * scaleY;
            }

            zPos = (maxHeight - presenter.z) * scaleZ;

            BundlePresenter bundlePresenter = new BundlePresenter(presenter);
            double minY = bundlePresenter.getPoints().get(0).getY();
            double maxY = bundlePresenter.getPoints().get(0).getY();
            for (Point2D summit : bundlePresenter.getPoints()) {
                if (summit.getY() < minY) {
                    minY = summit.getY();
                } else if (summit.getY() > maxY) {
                    maxY = summit.getY();

                }
            }
            double oldAngle = presenter.angle;
            presenter.angle = 0.;
            BundlePresenter bundleToShow = new BundlePresenter(presenter);
            Rectangle rectangle = bundleToShow.getRectangle();
            rectangle.setWidth((maxY - minY) * scaleY);
            rectangle.setHeight(presenter.height * scaleZ);
            rectangle.setX(yPos - rectangle.getWidth() / 2);
            rectangle.setY(zPos - rectangle.getHeight());
            if (presenter.id == mainController.getYardPresenter().getTopSelectedBundle().id) {
                rectangle.setEffect(dropShadow);
            }
            presenter.angle = oldAngle;
            rectangleBundleDtoMap.put(rectangle, presenter);
            dtoToRectangleMap.put(presenter, rectangle);

            rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                for (Map.Entry<Rectangle, BundleDto> entry : rectangleBundleDtoMap.entrySet()) {
                    entry.getKey().setEffect(null);
                }
                if (event.getButton() == MouseButton.PRIMARY) {
                    mainController.updateBundleInfo(rectangleBundleDtoMap.get(rectangle));
                    rectangle.setEffect(dropShadow);
                    mainController.getYardPresenter().setTopSelectedBundle(rectangleBundleDtoMap.get(rectangle));
                }
            });
            getChildren().add(rectangle);
        }
    }

    public void draw() {
        getChildren().clear();
        if (!allBundles.isEmpty()) {
            if (mainController.elevationViewMode == 'x') {
                allBundles = larmanController.sortBundlesY(allBundles);
                drawBundlesAxisX();
            }
            if (mainController.elevationViewMode == 'y') {
                allBundles = larmanController.sortBundlesX(allBundles);
                drawBundlesAxisY();
            }
        }
    }
}
