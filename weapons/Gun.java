package weapons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Gun {
    protected BufferedImage image;
    protected ArrayList<Bullet> bullets = new ArrayList<>();

    public abstract void shoot(double rotation, Rectangle2D PlayerBounds) throws IOException;

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
    public Rectangle2D[] getBulletBounds() {
        Rectangle2D[] bulletBounds = new Rectangle2D[bullets.size()];
        for (int i = 0; i < bullets.size(); i++) {
            bulletBounds[i] = bullets.get(i).bounds();
        }
        return bulletBounds;
    }

    public void render(Graphics2D graphics2D, double rotation, Point position) {

        ArrayList<Bullet> bulletsCopy = new ArrayList<>(bullets); // copy bullets to avoid concurrent modification exception!
        bulletsCopy.forEach(bullet -> bullet.render(graphics2D));



        AffineTransform old = graphics2D.getTransform();

        graphics2D.rotate(rotation, position.x, position.y);


        // convert to degrees for easier comparison
        rotation = Math.toDegrees(rotation);
        if ((rotation <= -90 && rotation > -270)  || (rotation >= 90 && rotation < 270)) {
            graphics2D.drawImage(image, position.x, position.y, image.getWidth(), -image.getHeight(), null);
        } else {
            graphics2D.drawImage(image, position.x, position.y, null);
        }

        graphics2D.setTransform(old);
        rotation = Math.toRadians(rotation);
    }

    public void tick() {
        bullets.forEach(Bullet::tick);
    }

    public void removeBulletFromIndex(int index) {
        bullets.remove(index);
    }



}
