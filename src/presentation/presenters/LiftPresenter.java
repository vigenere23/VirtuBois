package presentation.presenters;

import domain.dtos.LiftDto;
import helpers.CenteredRectangle;
import helpers.Point2D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class LiftPresenter extends CenteredRectangle implements IPresenter {
    public LiftDto dto;
    private CenteredRectangle arms;
    private static double distance = 1.0;


    public LiftPresenter(LiftDto dto) {
        super(dto.position.getX(), dto.position.getY(), dto.width, dto.length, dto.angle);
        this.dto = dto;
        this.arms = new CenteredRectangle(dto.position.getX(), dto.position.getY(), 0.5, 1.0, dto.angle);
        draw();
    }

    public void turnRight(){
        dto.angle = (dto.angle + 5.0)%360;
        getRectangle().setRotate(dto.angle);
    }

    public void turnLeft(){
        dto.angle = (dto.angle - 5.0)%360;
        getRectangle().setRotate(dto.angle);
    }

    public void forward(){
        dto.position.setX(dto.position.getX() + 0.2 * Math.cos((- dto.angle + 90 )* 2*Math.PI /360));
        dto.position.setY(dto.position.getY() + 0.2 * Math.sin((- dto.angle + 90 )* 2*Math.PI /360));
        setPosition(dto.position);
    }
    public void backward(){
         dto.position.setX(dto.position.getX() - 0.2 * Math.cos((- dto.angle + 90 )* 2*Math.PI /360));
         dto.position.setY(dto.position.getY() - 0.2 * Math.sin((- dto.angle + 90 )* 2*Math.PI /360));
         setPosition(dto.position);
    }
    public CenteredRectangle getArms(){
        return arms;
    }

    @Override
    public void draw() {
        Image image = new Image("presentation/assets/images/lift2.png");
        rectangle.setFill(new ImagePattern(image));
        arms.getRectangle().setFill(Color.RED);
    }
}
