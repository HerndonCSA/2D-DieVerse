package weapons;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

public class AK47Bullet extends Bullet {
    String imagePath = "weapons/pistol_bullet.png";

    public AK47Bullet(double direction, Point position) throws IOException {
        super(direction, position, 5);
        image = ImageIO.read(new File(imagePath));
    }

    @Override
    public void render(Graphics2D graphics2D) {
        graphics2D.rotate(direction, position.x, position.y);
        graphics2D.drawImage(image, (int) position.x, (int) position.y, null);
        graphics2D.rotate(-direction, position.x, position.y);
    }

    @Override
    public void tick() {
        position.x += Math.cos(direction) * 9;
        position.y += Math.sin(direction) * 9;
    }

    @Override
    public Rectangle2D bounds() {
        return new Rectangle2D.Double(position.x, position.y, image.getWidth(), image.getHeight());
    }
}
