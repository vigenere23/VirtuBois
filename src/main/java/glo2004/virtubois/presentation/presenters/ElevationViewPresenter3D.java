package glo2004.virtubois.presentation.presenters;

import glo2004.virtubois.domain.controllers.LarmanController;
import glo2004.virtubois.domain.dtos.BundleDto;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import glo2004.virtubois.presentation.controllers.MainController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static glo2004.virtubois.helpers.ColorHelper.hex2Rgb;

public class ElevationViewPresenter3D implements IPresenter {

    private SubScene scene;
    private Camera camera;
    private Group group;
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private LarmanController larmanController;
    private MainController mainController;
    private List<BundleDto> allBundles;
    private Point2D lastPoint;
    private Point2D groupTranslate;
    private Point2D initGroupTranslate;
    private Map<Box, BundleDto> boxToBundleDtoMap;
    private Map<BundleDto, Box> dtoToBoxMap;
    private BundleDto focusedBundle;
    private ToggleButton viewAllBundlesButton;

    public ElevationViewPresenter3D(StackPane parent, MainController mainController) {
        this.mainController = mainController;
        this.larmanController = mainController.larmanController;
        boxToBundleDtoMap = new HashMap<>();
        dtoToBoxMap = new HashMap<>();
        allBundles = new ArrayList<>();
        group = new Group();
        scene = new SubScene(group, 1, 1, true, SceneAntialiasing.BALANCED);
        viewAllBundlesButton = new ToggleButton("Vue totale");
        parent.getChildren().add(scene);
        parent.getChildren().add(viewAllBundlesButton);
        this.scene.heightProperty().bind(parent.heightProperty());
        this.scene.widthProperty().bind(parent.widthProperty());
        scene.setFill(Color.valueOf("#37474f"));

        camera = new PerspectiveCamera(true);
        scene.setCamera(camera);
        camera.translateXProperty().set(0);
        camera.translateYProperty().set(0);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        initControl(group, scene);
    }

    private void initControl(Group group, SubScene scene) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
            xRotate = new Rotate(0, Rotate.X_AXIS),
            yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            switch (event.getButton()) {
                case SECONDARY: {
                    anchorX = event.getSceneX();
                    anchorY = event.getSceneY();
                    anchorAngleX = angleX.get();
                    anchorAngleY = angleY.get();
                    break;
                }
                case PRIMARY: {
                    lastPoint = new Point2D(event.getSceneX(), event.getSceneY());
                    groupTranslate = new Point2D(group.getTranslateX(), group.getTranslateY());
                    break;
                }
            }

        });

        scene.setOnMouseDragged(event -> {
            switch (event.getButton()) {
                case SECONDARY: {
                    if (90.0 > anchorAngleX - (anchorY - event.getSceneY()) && anchorAngleX - (anchorY - event.getSceneY()) > 0.0) {
                        angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
                    }
                    angleY.set(anchorAngleY + anchorX - event.getSceneX());
                    break;
                }
                case PRIMARY: {
                    Point2D direction = new Point2D((event.getSceneX() - lastPoint.getX()) / 50.0, (event.getSceneY() - lastPoint.getY()) / 50.0);
                    group.translateYProperty().set(groupTranslate.getY() + direction.getY());
                    group.translateXProperty().set(groupTranslate.getX() + direction.getX());
                    break;
                }
            }
        });

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            if (camera.getTranslateZ() > 1.0 || camera.getTranslateZ() < -1.0) {
                camera.translateZProperty().set(camera.getTranslateZ() - delta * Math.abs(camera.getTranslateZ()) / 300.0d);
            } else {
                camera.translateZProperty().set(camera.getTranslateZ() - delta / 100.0d);
            }
        });
        viewAllBundlesButton.setOnAction(event -> {
            if (viewAllBundlesButton.isSelected()) {
                if (!larmanController.getBundles().isEmpty()) {
                    setFocusedBundle(mainController.getYardPresenter().getTopSelectedBundle());
                }
            } else {
                clearBundles(false);
                mainController.getYardPresenter().setTopSelectedBundle(null);
            }
        });
    }

//https://github.com/afsalashyana/JavaFX-3D/blob/master/src/gc/tutorial/chapt4/Rotation3DWithMouse.java

    @Override
    public void draw() {
        for (BundleDto bundle : allBundles) {
            Box box = new Box(bundle.width, bundle.height, bundle.length);
            box.setRotationAxis(new Point3D(0.0, 1.0, 0.0));
            box.setRotate(-bundle.angle);
            box.setTranslateX(bundle.getX() - initGroupTranslate.getX());
            box.setTranslateY(-(bundle.height) / 2 - bundle.getZ() + 1);
            box.setTranslateZ(bundle.getY() - initGroupTranslate.getY());

            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(hex2Rgb(bundle.color));
            material.setDiffuseMap(new Image("/glo2004/virtubois/presentation/assets/images/bois.jpg"));
            box.setMaterial(material);

            boxToBundleDtoMap.put(box, bundle);
            dtoToBoxMap.put(bundle, box);

            if (bundle.equals(focusedBundle)) {
                PhongMaterial phongMaterial = (PhongMaterial) box.getMaterial();
                Color color = phongMaterial.getDiffuseColor();
                phongMaterial.setDiffuseColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5));
            }

            box.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    for (Map.Entry<Box, BundleDto> entry : boxToBundleDtoMap.entrySet()) {
                        PhongMaterial phongMaterial = (PhongMaterial) entry.getKey().getMaterial();
                        Color color = phongMaterial.getDiffuseColor();
                        phongMaterial.setDiffuseColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 1.0));
                    }
                    mainController.updateBundleInfo(boxToBundleDtoMap.get(box));
                    PhongMaterial phongMaterial = (PhongMaterial) box.getMaterial();
                    Color color = phongMaterial.getDiffuseColor();
                    phongMaterial.setDiffuseColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5));
                    mainController.getYardPresenter().setTopSelectedBundle(boxToBundleDtoMap.get(box));
                }
            });
            group.getChildren().add(box);
        }
    }

    public void setFocusedBundle(BundleDto bundle) {
        clearBundles(false);
        focusedBundle = bundle;
        if (!viewAllBundlesButton.isSelected()) {
            allBundles = larmanController.getAllCollidingBundles(bundle);
            setInitialGroupTranslate();
            draw();
        } else {
            allBundles = larmanController.getBundles();
            setInitialGroupTranslate();
            draw();
        }
    }

    public void clearBundles(boolean fromMain) {
        if (!fromMain || !viewAllBundlesButton.isSelected()) {
            allBundles.clear();
            group.getChildren().clear();
            boxToBundleDtoMap.clear();
            dtoToBoxMap.clear();
        }
        if (fromMain && viewAllBundlesButton.isSelected()) {
            deselect();
        }
        if (!viewAllBundlesButton.isSelected()) {
            camera.translateXProperty().set(0.0);
            camera.translateYProperty().set(0.0);
            camera.translateZProperty().set(0.0);
            group.translateXProperty().set(0.0);
            group.translateYProperty().set(0.0);
            angleX.set(0.0);
            angleY.set(0.0);
        }
        focusedBundle = null;
    }

    private void setInitialGroupTranslate() {
        double minX = allBundles.get(0).position.getX();
        double maxX = allBundles.get(0).position.getX();
        double minY = allBundles.get(0).position.getY();
        double maxY = allBundles.get(0).position.getY();
        double maxZ = allBundles.get(0).topZ;
        double minHeight = allBundles.get(0).height;

        for (BundleDto bundle : allBundles) {
            BundlePresenter presenter = new BundlePresenter(bundle);
            for (glo2004.virtubois.helpers.Point2D position : presenter.getPoints()) {
                if (position.getX() < minX) {
                    minX = position.getX();
                }
                if (position.getX() > maxX) {
                    maxX = position.getX();
                }
                if ((bundle.height + bundle.z > maxZ)) {
                    maxZ = bundle.height + bundle.z;
                }
                if (position.getY() < minY) {
                    minY = position.getY();
                }
                if (position.getY() > maxY) {
                    maxY = position.getY();
                }
                if (bundle.z == 0.0) {
                    if (bundle.height < minHeight) {
                        minHeight = bundle.height;
                    }
                }
            }
        }
        double moyX = (maxX + minX) / 2.0;
        double moyY = (maxY + minY) / 2.0;
        initGroupTranslate = new Point2D(moyX, moyY);
        double deltaX = maxX - minX;
        double deltaY = maxY - minY;
        double cameraTranslateX = deltaX + 3;
        double cameraTranslateY = 11.0 / 3.0 * maxZ;
        double cameraTranslateZ = deltaY + 3;

        double translate = Math.max(cameraTranslateX, Math.max(cameraTranslateY, cameraTranslateZ));
        if (!viewAllBundlesButton.isSelected() || camera.getTranslateZ() == 0) {
            camera.translateZProperty().set(-translate);
        }

        Cylinder plancher = new Cylinder(Math.max(deltaX, Math.max(deltaY, maxZ * 4)) + 20, 0.1);
        plancher.setRotate(-90);
        plancher.setTranslateY(1);
        plancher.setRotationAxis(new Point3D(0, 1, 0));
        plancher.setRotate(90);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.GREY);
        Image image = new Image("/glo2004/virtubois/presentation/assets/images/asphalte.jpg", 3000, 3000, true, true);
        material.setDiffuseMap(image);
        plancher.setMaterial(material);

        AmbientLight sun = new AmbientLight();

        group.getChildren().addAll(sun, plancher);

    }

    private void deselect() {
        if (focusedBundle != null && !allBundles.isEmpty()) {
            for (int i = 2; i < group.getChildren().size(); i++) {
                Box box = (Box) group.getChildren().get(i);
                PhongMaterial material = (PhongMaterial) box.getMaterial();
                Color color = material.getDiffuseColor();
                material.setDiffuseColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 1.0));
            }
        }
    }
}