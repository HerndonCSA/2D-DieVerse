package mapobjects.shapes;

import mapobjects.Material;
import mapobjects.ShapeObject;
import mapobjects.Type;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Square extends ShapeObject {
    private final int length;
    
    public Square(Point position, Type type, Material material, int length) {
        super(position, type, material);
        this.length = length;
    }

    public Square(Point position, Type type, Color color, int length) {
        super(position, type, color);
        this.length = length;
    }


    @Override
    public Rectangle2D bounds() {
        return new Rectangle2D.Double(position.x, position.y, length, length);
    }

    @Override
    public void render(Graphics2D graphics2d) {
        // first, set the color / material
        if (material != null) {
            // TODO: implement
        } else {
            graphics2d.setColor(color);
        }
        
//         then, draw the shape
        if(debugTimer > 0) {
            graphics2d.draw(bounds());
            debugTimer--;
        }
        else graphics2d.fillRect(position.x, position.y, length, length);

//        // debug, render bounds as a outline instead:
//        graphics2d.draw(bounds());
    }
 
    @Override
    public void tick() {

    }
}
