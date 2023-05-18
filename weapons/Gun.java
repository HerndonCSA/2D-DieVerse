package weapons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Gun {
    protected BufferedImage image;
    protected ArrayList<Bullet> bullets = new ArrayList<>();

    public abstract void shoot(int rotation, Point position) throws IOException;

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void render(Graphics2D graphics2D, int rotation, Point position) {
        bullets.forEach(bullet -> bullet.render(graphics2D));



        AffineTransform old = graphics2D.getTransform();

        graphics2D.rotate(Math.toRadians(rotation), position.x, position.y);

        if ((rotation <= -90 && rotation > -270)  || (rotation >= 90 && rotation < 270)) {
            graphics2D.drawImage(image, position.x, position.y, image.getWidth(), -image.getHeight(), null);
        } else {
            graphics2D.drawImage(image, position.x, position.y, null);
        }

        graphics2D.setTransform(old);
    }

    public void tick() {
        bullets.forEach(Bullet::tick);
    }



}
