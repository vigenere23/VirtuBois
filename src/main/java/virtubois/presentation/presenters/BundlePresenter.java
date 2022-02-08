package virtubois.presentation.presenters;

import virtubois.domain.dtos.BundleDto;
import virtubois.helpers.CenteredRectangle;
import virtubois.helpers.ColorHelper;
import virtubois.helpers.ConfigHelper;
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
        rectangle.setFill(ColorHelper.setOpacity(color, ConfigHelper.bundleOpacity));
        rectangle.setStroke(color);
        rectangle.setStrokeWidth(ConfigHelper.bundleBorderWidth);
    }
}
