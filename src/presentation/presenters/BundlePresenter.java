package presentation.presenters;

import domain.dtos.BundleDto;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class BundlePresenter extends Rectangle implements IPresenter {

    public BundleDto dto;

    public static BundlePresenter create(BundleDto dto) {
        Point2D topLeftCoordinates = getTopLeftCoordinates(dto);
        return new BundlePresenter(dto, topLeftCoordinates.getX(), topLeftCoordinates.getY());
    }

    private BundlePresenter(BundleDto dto, double leftX, double topY) {
        super(leftX, topY, dto.width, dto.length);
        this.dto = dto;
        this.rotateProperty().setValue(dto.angle);
    }

    public static Point2D getTopLeftCoordinates(BundleDto dto) {
        return new Point2D(
                dto.position.getX(),
                dto.position.getY()
        );
    }

    public void draw() {

    }
}
