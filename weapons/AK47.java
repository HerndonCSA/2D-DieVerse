package weapons;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

public class AK47 extends Gun {
    String ImagePath = "weapons/AK47.png";
    public AK47() throws IOException {
        super();
        this.image = ImageIO.read(new File(ImagePath));
    }

    int shootCooldown = 0;

    @Override
    public void shoot(double rotation, Rectangle2D PlayerBounds) throws IOException {
        if (shootCooldown > 0) return;
        // shoot from the middle of the player
        Point initialBulletPosition = new Point((int) PlayerBounds.getCenterX(), (int) PlayerBounds.getCenterY());
        bullets.add(new AK47Bullet(rotation, initialBulletPosition));
        shootCooldown = 30;
    }

    @Override
    public void tick() {
        super.tick();
        shootCooldown = Math.max(0, shootCooldown - 1);
    }

}
