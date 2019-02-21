package presentation.javafxModels;

import domain.dtos.PackDto;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class PackModel extends Rectangle implements IModel {

    public PackDto dto;

    public static PackModel create(PackDto dto) {
        Point2D topLeftCoordinates = getTopLeftCoordinates(dto);
        return new PackModel(dto, topLeftCoordinates.getX(), topLeftCoordinates.getY());
    }

    private PackModel(PackDto dto, double leftX, double topY) {
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
