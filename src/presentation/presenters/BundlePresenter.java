package presentation.presenters;

import domain.dtos.BundleDto;
import helpers.ColorHelper;
import helpers.ConfigHelper;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineJoin;

public class BundlePresenter extends Rectangle implements IPresenter {

    public BundleDto dto;

    public BundlePresenter(BundleDto dto) {
        super(dto.position.getX(), dto.position.getY(), dto.width, dto.length);
        this.dto = dto;
        setRotate(dto.angle);
        draw();
    }

    public void draw() {
        Color color = Color.web(dto.color);
        setFill(ColorHelper.setOpacity(color, ConfigHelper.bundleOpacity));
        setStroke(color);
        setStrokeWidth(ConfigHelper.bundleBorderWidth);
    }
}
