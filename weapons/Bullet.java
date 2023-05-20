package weapons;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Bullet {
    protected final double direction;
    protected BufferedImage image;

    protected final Point2D.Double position;
    private int damage;
    public int getDamage() {
        return damage;
    }



    protected Bullet(double direction, Point position, int damage) {
        this.direction = direction;
        this.position = new Point2D.Double(position.x, position.y);
        this.damage = damage;
    }

    public abstract void tick();


    public abstract void render(Graphics2D graphics2D);
    public abstract Rectangle2D bounds();



}
