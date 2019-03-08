package presentation.presenters;

import domain.dtos.PackDto;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class PackPresenter extends Rectangle implements IPresenter {

    public PackDto dto;

    public static PackPresenter create(PackDto dto) {
        Point2D topLeftCoordinates = getTopLeftCoordinates(dto);
        return new PackPresenter(dto, topLeftCoordinates.getX(), topLeftCoordinates.getY());
    }

    private PackPresenter(PackDto dto, double leftX, double topY) {
        super(leftX, topY, dto.width, dto.length);
        this.dto = dto;
        this.rotateProperty().setValue(dto.angle);
    }

    public static Point2D getTopLeftCoordinates(PackDto dto) {
        return new Point2D(
                dto.position.getX(),
                dto.position.getY()
        );
    }

    public void draw() {

    }
}
