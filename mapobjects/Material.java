package mapobjects;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum Material {
    WOOD("/Users/soos/Documents/GitHub/2D-DieVerse/wood.png", 0, 1);

    private final int damage;
    private final int speedModifier;
    private final BufferedImage image;

    // Constructor
    Material(String imagePath, int damage, int speedModifier) {
        this.damage = damage;
        this.speedModifier = speedModifier;
        this.image = null;
    }

    private BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getters
    public BufferedImage getImage() {
        return image;
    }

    public int getDamage() {
        return damage;
    }

    public int getSpeedModifier() {
        return speedModifier;
    }
}
