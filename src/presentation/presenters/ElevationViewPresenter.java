package presentation.presenters;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import helpers.Point2D;
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

    public ElevationViewPresenter(MainController mainController){
        super();
        setFocusTraversable(true);

        this.mainController = mainController;
        this.larmanController = mainController.larmanController;

        dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setColor(Color.GREY);
    }

    private void initEventHandlers(){
        widthProperty().addListener(observable -> draw());
        heightProperty().addListener(observable -> draw());
    }

    private void setBundles(List<BundleDto> bundles){
        allBundles = bundles;
        draw();
    }
    private void clearBundles(){
        allBundles.clear();
        draw();
    }

    private void drawBundles(){
        double height = getHeight();
        double width = getWidth();
        double minLength = 0;
        double maxLength = 0;
        double maxHeight = 0;
        for(BundleDto bundle : allBundles){
            if ((bundle.position.getX()-(bundle.length/2)) < minLength){
                minLength = bundle.position.getX() - (bundle.length/2);
            }
            if((bundle.position.getX() + (bundle.length/2) < maxLength)){
                maxLength = bundle.position.getX() + (bundle.length/2);
            }
            if((bundle.height + bundle.z > maxHeight)){
                maxHeight = bundle.height + bundle.z;
            }
        }
        double scaleZ = height/maxHeight;
        double scaleX = width/maxLength;

        for(BundleDto presenter : allBundles){
            double xPos = 0;
            double zPos = 0;
            if(minLength < 0) {
                xPos = (presenter.position.getX() + Math.abs(minLength)) * scaleX;
            }
            else{
                xPos = (presenter.position.getX() - minLength) * scaleX;
            }

            zPos = (maxHeight - presenter.z)*scaleZ;

            BundlePresenter bundlePresenter = new BundlePresenter(presenter);
            Rectangle rectangle = bundlePresenter.get();
            rectangle.setWidth(presenter.width*scaleX);
            rectangle.setHeight(presenter.height*scaleZ);
            rectangle.setX(xPos - rectangle.getWidth()/2);
            rectangle.setY(zPos - rectangle.getHeight()/2);
            getChildren().add(rectangle);

        }

    }



    @Override
    public void draw() {
        getChildren().clear();
        if(!allBundles.isEmpty())
        {
            drawBundles();
        }

    }
}
