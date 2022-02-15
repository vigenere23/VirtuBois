package glo2004.virtubois.presentation.presenters;

import glo2004.virtubois.domain.dtos.BundleDto;
import glo2004.virtubois.helpers.CenteredRectangle;
import glo2004.virtubois.helpers.ColorHelper;
import glo2004.virtubois.helpers.DefaultConfig;
import javafx.scene.paint.Color;

public class BundlePresenter extends CenteredRectangle implements IPresenter {

    public BundleDto dto;

    public BundlePresenter(BundleDto dto) {
        super(dto.position.getX(), dto.position.getY(), dto.width, dto.length, dto.angle);
        this.dto = dto;
        draw();
    }

    public void draw() {
        Color color = Color.web(dto.color);
        rectangle.setFill(ColorHelper.setOpacity(color, DefaultConfig.bundleOpacity));
        rectangle.setStroke(color);
        rectangle.setStrokeWidth(DefaultConfig.bundleBorderWidth);
    }
}
