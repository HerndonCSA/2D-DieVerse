package weapons;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PistolBullet extends Bullet {
    String imagePath = "weapons/pistol_bullet.png";

    public PistolBullet(int direction, Point position) throws IOException {
        super(direction, position);
        image = ImageIO.read(new File(imagePath));
    }

    @Override
    public void render(Graphics2D graphics2D) {
        graphics2D.rotate(Math.toRadians(direction), position.x, position.y);
        graphics2D.drawImage(image, position.x, position.y, null);
        graphics2D.rotate(Math.toRadians(-direction), position.x, position.y);
    }
}
