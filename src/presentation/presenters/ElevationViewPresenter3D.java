package presentation.presenters;

import domain.controllers.LarmanController;
import domain.dtos.BundleDto;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import presentation.controllers.MainController;
import java.util.ArrayList;
import java.util.List;

import static helpers.ColorHelper.hex2Rgb;

public class ElevationViewPresenter3D implements IPresenter {

    private SubScene scene;
    private Camera camera;
    private Group group;
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private final DoubleProperty transX = new SimpleDoubleProperty(0);
    private final DoubleProperty transY = new SimpleDoubleProperty(0);
    private LarmanController larmanController;
    private MainController mainController;
    private List<BundleDto> allBundles;

    public ElevationViewPresenter3D(StackPane parent, MainController mainController) {
        this.mainController = mainController;
        this.larmanController = mainController.larmanController;
        allBundles = new ArrayList<>();
        group = new Group();
        scene = new SubScene(group, 1, 1, true, null);
        parent.getChildren().add(scene);
        this.scene.heightProperty().bind(parent.heightProperty());
        this.scene.widthProperty().bind(parent.widthProperty());
        scene.setFill(Color.valueOf("#37474f"));

        camera = new PerspectiveCamera(true);
        scene.setCamera(camera);
        camera.translateXProperty().set(0);
        camera.translateYProperty().set(0);
        camera.translateZProperty().set(-10);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        initControl(group, scene);
    }

    private void initControl(Group group, SubScene scene) {
        Rotate xRotate;
        Rotate yRotate;
        Translate translate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS),
                translate = new Translate(0, 0)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        translate.xProperty().bind(transX);
        translate.yProperty().bind(transY);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W: {
                    transY.set(transY.get() + 5.0);
                    break;
                }
                case S: {
                    transY.set(transY.get() - 5.0);
                    break;
                }
                case A: {
                    transX.set(transX.get() - 5.0);
                    break;
                }
                case D: {
                    transX.set(transX.get() + 5.0);
                    break;
                }
            }
        });

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta / 100.0d);
        });
    }


//https://github.com/afsalashyana/JavaFX-3D/blob/master/src/gc/tutorial/chapt4/Rotation3DWithMouse.java

    @Override
    public void draw() {
        for (BundleDto bundle : allBundles){
            Box box = new Box(bundle.width, bundle.height, bundle.length);
            box.setRotationAxis(new Point3D(0.0, 1.0, 0.0));
            box.setRotate(-bundle.angle);
            box.setTranslateX(bundle.getX());
            box.setTranslateY(-bundle.getZ());
            box.setTranslateZ(bundle.getY());

            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(hex2Rgb(bundle.color));
            box.setMaterial(material);

            box.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                if (event.getButton() == MouseButton.PRIMARY) {

                }
            });
            group.getChildren().add(box);
        }
    }

    public void setFocusedBundle(BundleDto bundle) {
        clearBundles();
        larmanController.getAllCollidingBundles(allBundles,bundle);
        draw();
    }

    public void clearBundles() {
        allBundles.clear();
        group.getChildren().clear();
        draw();
    }

}