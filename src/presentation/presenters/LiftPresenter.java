package presentation.presenters;

import domain.dtos.LiftDto;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class LiftPresenter extends Rectangle implements IPresenter {
    public LiftDto dto;

    public static LiftPresenter create(LiftDto dto) {
        Point2D topLeftCoordinates = getTopLeftCoordinates(dto);
        return new LiftPresenter(dto, topLeftCoordinates.getX(), topLeftCoordinates.getY());
    }

    private LiftPresenter(LiftDto dto, double leftX, double topY) {
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

    public void draw() {

    }
}
