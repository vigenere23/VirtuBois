package presentation.presenters;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import presentation.controllers.MainController;

import java.util.List;

public class ElevationViewPresenter extends Pane implements IPresenter {

    private MainController mainController;
    private LarmanController larmanController;
    private DropShadow dropShadow;
    private List<BundleDto> allBundles;

    public ElevationViewPresenter(MainController mainController) {
        super();
        setFocusTraversable(true);

        this.mainController = mainController;
        this.larmanController = mainController.larmanController;

        dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setColor(Color.GREY);
    }

    private void initEventHandlers() {
        widthProperty().addListener(observable -> draw());
        heightProperty().addListener(observable -> draw());
    }

    public void setBundles(List<BundleDto> bundles){
        allBundles = bundles;
        draw();
    }
    public void clearBundles(){
        allBundles.clear();
        draw();
    }

    private void drawBundlesAxisX() {
        double height = getHeight();
        double width = getWidth();
        double minWidth = allBundles.get(0).getX() - (allBundles.get(0).width/2);
        double maxWidth = allBundles.get(0).getX() + (allBundles.get(0).width/2);
        double maxHeight = allBundles.get(0).z + allBundles.get(0).height;
        for (BundleDto bundle : allBundles) {
            if ((bundle.position.getX()-(bundle.width/2)) < minWidth){
                minWidth = bundle.position.getX() - (bundle.width/2);
            }
            if((bundle.position.getX() + (bundle.width/2) > maxWidth)){
                maxWidth = bundle.position.getX() + (bundle.width/2);
            }
            if ((bundle.height + bundle.z > maxHeight)){
                maxHeight = bundle.height + bundle.z;
            }
        }

        double scaleZ = height/maxHeight;
        double scaleX = width/(maxWidth - minWidth);

        for (BundleDto presenter : allBundles) {
            double xPos = 0;
            double zPos = 0;
            if (minWidth < 0) {
                xPos = (presenter.position.getX() + Math.abs(minWidth)) * scaleX;
            }
            else {
                xPos = (presenter.position.getX() - minWidth) * scaleX;
            }

            zPos = (maxHeight - presenter.z)*scaleZ;

            BundlePresenter bundlePresenter = new BundlePresenter(presenter);
            Rectangle rectangle = bundlePresenter.getRectangle();
            rectangle.setWidth(presenter.width*scaleX);
            rectangle.setHeight(presenter.height*scaleZ);
            rectangle.setX(xPos - rectangle.getWidth()/2);
            rectangle.setY(zPos - rectangle.getHeight());
            getChildren().add(rectangle);
        }
    }

    private void drawBundlesAxisY() {
        double height = getHeight();
        double width = getWidth();
        double minLength = allBundles.get(0).getY() - (allBundles.get(0).length/2);
        double maxLength = allBundles.get(0).getY() + (allBundles.get(0).length/2);
        double maxHeight = allBundles.get(0).z + allBundles.get(0).height;
        for (BundleDto bundle : allBundles) {
            if ((bundle.position.getY()-(bundle.length/2)) < minLength){
                minLength = bundle.position.getY() - (bundle.length/2);
            }
            if((bundle.position.getY() + (bundle.length/2) > maxLength)){
                maxLength = bundle.position.getY() + (bundle.length/2);
            }
            if ((bundle.height + bundle.z > maxHeight)){
                maxHeight = bundle.height + bundle.z;
            }
        }

        double scaleZ = height/maxHeight;
        double scaleY = width/(maxLength - minLength);

        for (BundleDto presenter : allBundles) {
            double yPos = 0;
            double zPos = 0;
            if (minLength < 0) {
                yPos = (presenter.position.getY() + Math.abs(minLength)) * scaleY;
            }
            else {
                yPos = (presenter.position.getY() - minLength) * scaleY;
            }

            zPos = (maxHeight - presenter.z)*scaleZ;

            BundlePresenter bundlePresenter = new BundlePresenter(presenter);
            Rectangle rectangle = bundlePresenter.getRectangle();
            rectangle.setWidth(presenter.length*scaleY);
            rectangle.setHeight(presenter.height*scaleZ);
            rectangle.setX(yPos - rectangle.getWidth()/2);
            rectangle.setY(zPos - rectangle.getHeight());
            getChildren().add(rectangle);
        }
    }

    public void draw() {
        getChildren().clear();
        if (!allBundles.isEmpty())
            drawBundlesAxisY();
    }
}
