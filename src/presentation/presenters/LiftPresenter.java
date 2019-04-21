package presentation.presenters;

import domain.dtos.LiftDto;
import helpers.CenteredRectangle;
import helpers.ConfigHelper;
import helpers.GeomHelper;
import helpers.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class LiftPresenter extends CenteredRectangle implements IPresenter {
    public LiftDto dto;
    private CenteredRectangle arms;
    private Point2D realArmsPosition;
    private static double distance = 1.25;


    public LiftPresenter(LiftDto dto) {
        super(dto.position.getX(), dto.position.getY(), dto.width, dto.length, dto.angle);
        this.dto = dto;
        this.arms = new CenteredRectangle(dto.position.getX(), dto.position.getY(), 0.75, 1.0, dto.angle);
        realArmsPosition = new Point2D();
        calculateArmsPosition();
        draw();
    }

    public void turnRight() {
        turn(true);
    }

    public void turnLeft() {
        turn(false);
    }

    private void turn(boolean turnRight) {
        if (turnRight) dto.angle += ConfigHelper.liftAngleIncrement;
        else dto.angle -= ConfigHelper.liftAngleIncrement;
        getRectangle().setRotate(dto.angle);
        calculateArmsPosition();
    }

    public void forward() {
        move(true);
    }

    public void backward() {
        move(false);
    }

    private void move(boolean moveForward) {
        Point2D increment = new Point2D(ConfigHelper.liftPositionIncrement, ConfigHelper.liftPositionIncrement);
        Point2D rotatedIncrement = GeomHelper.getRotatedVector(increment, -dto.angle + 90);
        if (moveForward) dto.position = dto.position.add(rotatedIncrement);
        else dto.position = dto.position.subtract(rotatedIncrement);
        setPosition(dto.position);
        calculateArmsPosition();
    }

    public void calculateArmsPosition() {
        double xPos = dto.position.getX() + (distance * Math.cos((-dto.angle + 90) * 2 * Math.PI / 360));
        double yPos = dto.position.getY() + (distance * Math.sin((-dto.angle + 90) * 2 * Math.PI / 360));
        realArmsPosition.setX(xPos);
        realArmsPosition.setY(yPos);
        arms.setPosition(realArmsPosition);
        arms.getRectangle().setRotate(dto.angle);
    }

    public Point2D getArmsPosition() {
        return realArmsPosition;
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
