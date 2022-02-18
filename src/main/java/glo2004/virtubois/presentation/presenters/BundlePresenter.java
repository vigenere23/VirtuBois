package glo2004.virtubois.presentation.presenters;

import glo2004.virtubois.domain.dtos.BundleDto;
import glo2004.virtubois.helpers.CenteredRectangle;
import glo2004.virtubois.helpers.ColorHelper;
import glo2004.virtubois.helpers.DefaultConfig;
import glo2004.virtubois.helpers.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class BundlePresenter extends CenteredRectangle implements IPresenter {

    public BundleDto dto;

    public BundlePresenter(BundleDto dto) {
        super(dto.position.getX(), dto.position.getY(), dto.width, dto.length, dto.angle);
        this.dto = dto;
        draw();
    }

    public void draw() {
        Color color = Color.web(dto.color);
        rectangle.setFill(ColorHelper.setOpacity(color, DefaultConfig.bundleOpacity));
        rectangle.setStroke(color);
        rectangle.setStrokeWidth(DefaultConfig.bundleBorderWidth);
    }

    // TODO should not be there
    public List<Point3D> get3DPoints() {
        List<Point3D> points3D = new ArrayList<>();
        List<Point2D> points2D = get2DPoints();

        points2D.forEach(point2D -> {
            points3D.add(new Point3D(point2D.getX(), point2D.getY(), dto.z));
        });
        points2D.forEach(point2D -> {
            points3D.add(new Point3D(point2D.getX(), point2D.getY(), dto.topZ));
        });

        return points3D;
    }
}
