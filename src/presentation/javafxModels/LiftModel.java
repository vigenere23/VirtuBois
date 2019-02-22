package presentation.javafxModels;

import domain.dtos.LiftDto;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class LiftModel extends Rectangle {
    public LiftDto dto;

    public static LiftModel create(LiftDto dto) {
        Point2D topLeftCoordinates = getTopLeftCoordinates(dto);
        return new LiftModel(dto, topLeftCoordinates.getX(), topLeftCoordinates.getY());
    }

    private LiftModel(LiftDto dto, double leftX, double topY) {
        super(leftX, topY, dto.width, dto.length);
        this.dto = dto;
        this.rotateProperty().setValue(dto.angle);
    }

    public static Point2D getTopLeftCoordinates(LiftDto dto) {
        return new Point2D(
                dto.position.getX(),
                dto.position.getY()
        );
    }
}
