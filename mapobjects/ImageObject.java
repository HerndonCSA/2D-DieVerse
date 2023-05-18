package mapobjects;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class ImageObject extends MapObject {
    private BufferedImage image;
    public ImageObject(Point p, Type t, String imagePath) {
        super(p, t);
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Rectangle2D bounds() {
        return new Rectangle2D.Double(position.x, position.y, image.getWidth(), image.getHeight());
    }

    public void render(Graphics2D graphics2d) {
        graphics2d.drawImage(image, position.x, position.y, null);

        // debug, render bounds as a outline instead:
//        graphics2d.draw(bounds());
    }


    public void tick() {
        // do nothing
    }
}
