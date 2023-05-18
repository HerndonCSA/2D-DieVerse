package mapobjects;
import java.awt.geom.Rectangle2D;
import java.awt.*;

public abstract class MapObject {
    protected final Point position;
    protected final Type type;
    protected boolean transparent = false; // will only be true of type is TRANSPARENT
    // another way to implement transparency is to have a separate class for transparent objects but no im too lazy

    public MapObject(Point position, Type type) {
        this.position = position;
        this.type = type;
    }

    public abstract Rectangle2D bounds();
    public abstract void render(Graphics2D graphics2d);
    public abstract void tick();

    public Point getPosition() {
        return position;
    }

    public Type getType() {
        return type;
    }

    public void enableTransparency() {
        if (type != Type.TRANSPARENT) {
            throw new UnsupportedOperationException("Cannot enable transparency on a non-transparent object");
        }
        transparent = true;
    }

    public void disableTransparency() {
        if (type != Type.TRANSPARENT) {
            throw new UnsupportedOperationException("Cannot disable transparency on a non-transparent object");
        }
        transparent = false;
    }
    protected int debugTimer = 0;
    public void debug() {
        debugTimer = 30;
    }
    
}