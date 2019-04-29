package presentation.presenters;

import domain.dtos.LiftDto;
import helpers.CenteredRectangle;
import helpers.ConfigHelper;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class LiftPresenter extends CenteredRectangle implements IPresenter {
    public LiftDto dto;
    private CenteredRectangle arms;

    public LiftPresenter(LiftDto dto) {
        super(dto.position, dto.width, dto.length, -dto.angle);
        this.dto = dto;
        this.arms = new CenteredRectangle(dto.armsPosition, dto.armsWidth, dto.armsLength, -dto.angle);
        draw();
    }

    public CenteredRectangle getArms() {
        return arms;
    }

    @Override
    public void draw() {
        Image image = new Image("presentation/assets/images/lift2.png", 342.0,256.0,true,true);
        rectangle.setFill(new ImagePattern(image));
        Image imageArms = new Image("presentation/assets/images/goodArms.png", 164,124,true,true);
        arms.getRectangle().setFill(new ImagePattern(imageArms));
    }
}
