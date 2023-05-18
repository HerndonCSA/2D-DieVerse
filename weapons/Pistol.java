package weapons;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Pistol extends Gun {
    String ImagePath = "weapons/pistol.png";
    public Pistol() throws IOException {
        super();
        this.image = ImageIO.read(new File(ImagePath));
    }

    @Override
    public void shoot(int rotation, Point position) throws IOException {
        bullets.add(new PistolBullet(rotation, new Point(position.x + 10, position.y + 25)));
    }
}
