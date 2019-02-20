package presentation.models;

import domain.dtos.ChargerDto;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class ChargerModel extends Rectangle {
    public ChargerDto dto;

    public static ChargerModel create(ChargerDto dto) {
        Point2D topLeftCoordinates = getTopLeftCoordinates(dto);
        return new ChargerModel(dto, topLeftCoordinates.getX(), topLeftCoordinates.getY());
    }

    private ChargerModel(ChargerDto dto, double leftX, double topY) {
        super(leftX, topY, dto.width, dto.length);
        this.dto = dto;
        this.rotateProperty().setValue(dto.angle);
    }

    public static Point2D getTopLeftCoordinates(ChargerDto dto) {
        return new Point2D(
                dto.position.getX(),
                dto.position.getY()
        );
    }
}
