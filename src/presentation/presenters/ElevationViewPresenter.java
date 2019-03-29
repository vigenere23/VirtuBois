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

    private void drawBundles() {
        double height = getHeight();
        double width = getWidth();
        double minLength = allBundles.get(0).getX() - (allBundles.get(0).width/2);
        double maxLength = allBundles.get(0).getX() + (allBundles.get(0).width/2);
        double maxHeight = allBundles.get(0).z + allBundles.get(0).height;
        for (BundleDto bundle : allBundles) {
            if ((bundle.position.getX()-(bundle.width/2)) < minLength){
                minLength = bundle.position.getX() - (bundle.width/2);
            }
            if((bundle.position.getX() + (bundle.width/2) > maxLength)){
                maxLength = bundle.position.getX() + (bundle.width/2);
            }
            if ((bundle.height + bundle.z > maxHeight)){
                maxHeight = bundle.height + bundle.z;
            }
        }


        double scaleZ = height/maxHeight;
        double scaleX = width/(maxLength - minLength);

        for (BundleDto presenter : allBundles) {
            double xPos = 0;
            double zPos = 0;
            if (minLength < 0) {
                xPos = (presenter.position.getX() + Math.abs(minLength)) * scaleX;
            }
            else {
                xPos = (presenter.position.getX() - minLength) * scaleX;
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

    public void draw() {
        getChildren().clear();
        if (!allBundles.isEmpty())
            drawBundles();
    }
}
