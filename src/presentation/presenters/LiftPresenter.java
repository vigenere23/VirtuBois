package presentation.presenters;

import domain.dtos.LiftDto;
import helpers.CenteredRectangle;
import helpers.ConfigHelper;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class LiftPresenter extends CenteredRectangle implements IPresenter {
    public LiftDto dto;
    private CenteredRectangle arms;


    public LiftPresenter(LiftDto dto) {
        super(dto.position, dto.width, dto.length, dto.angle);
        this.dto = dto;
        this.arms = new CenteredRectangle(dto.armsPosition, ConfigHelper.armsWidth, ConfigHelper.armsLength, dto.angle);
        draw();
    }

    public CenteredRectangle getArms() {
        return arms;
    }

    @Override
    public void draw() {
        Image image = new Image("presentation/assets/images/lift2.png");
        rectangle.setFill(new ImagePattern(image));
        arms.getRectangle().setFill(Color.RED);
    }
}
