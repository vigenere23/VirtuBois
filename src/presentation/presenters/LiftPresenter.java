package presentation.presenters;

import domain.dtos.LiftDto;
import helpers.CenteredRectangle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class LiftPresenter extends CenteredRectangle implements IPresenter {
    public LiftDto dto;

    public LiftPresenter(LiftDto dto) {
        super(dto.position.getX(), dto.position.getY(), dto.width, dto.length, dto.angle);
        this.dto = dto;
        draw();
    }

    public void draw() {
        Image image = new Image("presentation/assets/images/Lift.png");
        rectangle.setFill(new ImagePattern(image));
    }
}
