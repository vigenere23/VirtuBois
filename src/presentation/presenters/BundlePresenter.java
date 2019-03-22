package presentation.presenters;

import domain.dtos.BundleDto;
import helpers.CenteredRectangle;
import helpers.ColorHelper;
import helpers.ConfigHelper;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineJoin;

public class BundlePresenter extends CenteredRectangle implements IPresenter {

    public BundleDto dto;

    public BundlePresenter(BundleDto dto) {
        super(dto.position.getX(), dto.position.getY(), dto.width, dto.length, dto.angle);
        this.dto = dto;
        draw();
    }

    public void draw() {
        Color color = Color.web(dto.color);
        rectangle.setFill(ColorHelper.setOpacity(color, ConfigHelper.bundleOpacity));
        rectangle.setStroke(color);
        rectangle.setStrokeWidth(ConfigHelper.bundleBorderWidth);
    }

    public void markAsSelected() {
        rectangle.getStrokeDashArray().add(5.0);
    }

    public void markAsTopSelected() {
        rectangle.setStroke(Color.WHITE);
    }
}
