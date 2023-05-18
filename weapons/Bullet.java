package weapons;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Bullet {
    protected final int direction;
    protected final Point position;
    protected BufferedImage image;

    protected Bullet(int direction, Point position) {
        this.direction = direction;
        this.position = position;
    }

    public abstract void render(Graphics2D graphics2D);

    public void tick() {
        position.x += Math.cos(Math.toRadians(direction));
        position.y += Math.sin(Math.toRadians(direction));
    }
}
