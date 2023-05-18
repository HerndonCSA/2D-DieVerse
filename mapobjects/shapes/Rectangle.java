package mapobjects.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import mapobjects.Material;
import mapobjects.ShapeObject;
import mapobjects.Type;

public class Rectangle extends ShapeObject{
    private final int height;
    private final int width;
    

    public Rectangle(Point position, Type type, Material material, int height, int width) {
        super(position, type, material);
        this.height = height;
        this.width = width;
    }

    public Rectangle(Point position, Type type, Color color, int height, int width) {
        super(position, type, color);
        this.height = height;
        this.width = width;
    }

    @Override
    public void render(Graphics2D graphics2d) {
        // first, set the color / material
        if (material != null) {
            // TODO: implement
        } else {
            graphics2d.setColor(color);
        }

        if(debugTimer > 0) {
            graphics2d.draw(bounds());
            debugTimer--;
        }
        else graphics2d.fillRect(position.x, position.y, width, height);
    }


    @Override
    public void tick() {
        // TODO Auto-generated method stub
    }

    @Override
    public Rectangle2D bounds() {
        return new Rectangle2D.Double(position.x, position.y, width, height);
    }
}
