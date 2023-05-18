package mapobjects;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class ShapeObject extends MapObject {
    protected final Material material;
    protected final Color color;

    public ShapeObject(Point position, Type type, Material material) {
        super(position, type);
        this.material = material; 
        this.color = null;
    }

    public ShapeObject(Point position, Type type, Color color) {
        super(position, type);
        this.material = null;
        this.color = color;
    }

    public abstract void render(Graphics2D graphics2d);
    public abstract void tick();
    public abstract Rectangle2D bounds();

    public Material getMaterial() {
        return material;
    }

    public Color getColor() {
        return color;
    }
}
