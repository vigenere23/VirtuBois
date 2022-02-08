package virtubois.domain.dtos;

import virtubois.domain.entities.Drawable3D;

public abstract class Drawable3DDto extends DrawableDto {
    public double height;
    public double z;
    public double topZ;

    public Drawable3DDto(Drawable3D drawable3D) {
        super(drawable3D);
        height = drawable3D.getHeight();
        z = drawable3D.getZ();
        topZ = drawable3D.getTopZ();
    }
}
