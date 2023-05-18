import mapobjects.Type;
import weapons.Gun;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
// import arraylist
import java.util.ArrayList;

public class Player {
    private BufferedImage image;

    private Point position;
    private int health;
    private int yVelocity = 0;
    private Map map;
    private Gun gun;

    private HashMap<Integer, String> keys = new HashMap<>();


    public Player(Point position, String imagePath, boolean USING_WASD, Map map, Gun gun) throws IOException {
        this.position = position;
        this.health = 100;
        this.image = ImageIO.read(new File(imagePath));
        this.map = map;
        this.gun = gun;

        // shoot for arrow keys is "\" and for WASD is "f"
        if (USING_WASD) {
            keys.put(KeyEvent.VK_W, "up");
            keys.put(KeyEvent.VK_A, "left");
            keys.put(KeyEvent.VK_D, "right");
            keys.put(KeyEvent.VK_F, "shoot");

        } else {
            keys.put(KeyEvent.VK_UP, "up");
            keys.put(KeyEvent.VK_LEFT, "left");
            keys.put(KeyEvent.VK_RIGHT, "right");
            keys.put(KeyEvent.VK_BACK_SLASH, "shoot");
        }
    }

    public Rectangle2D bounds() {
        return new Rectangle2D.Double(position.x, position.y, image.getWidth(), image.getHeight());
    }


    // the normal tick  method is called 240 times per second, this one is called 30 times per second
    // this is used for things like gravity as it would be too fast otherwise
    private int tickCounter = 0;

    private void secondaryTick() {
        yVelocity += 1;
    }
    int jumpCoolDown = 0;
    boolean leftPressed = false;
    boolean rightPressed = false;

    public void handleKeyPress(int keyCode, Player otherPlayer) {
        // use KeyEvent.whatever to be clear
        // https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html
        String action = keys.get(keyCode);
        if (action == null) {
            return;
        }

        switch (action) {
            case "up" -> {
                if (jumpCoolDown == 0) {
                    yVelocity -= 5;
                    jumpCoolDown = 70;
                }
            }
            case "left" -> leftPressed = true;
            case "right" -> rightPressed = true;
            case "shoot" -> {
                try {
                    gun.shoot((int) Math.toDegrees(Math.atan2(otherPlayer.position.y - position.y, otherPlayer.position.x - position.x)), position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void handleKeyRelease(int keyCode, Player otherPlayer) {
        String action = keys.get(keyCode);
        if (action == null) {
            return;
        }
        // turn off left and right movement if the key is released
        switch (action) {
            case "left" -> leftPressed = false;
            case "right" -> rightPressed = false;
        }
    }

    public void tick() {
        // tick the gun
        gun.tick();
        // secondary tick logic
        tickCounter++;
        if (tickCounter % 8 == 0) {
            secondaryTick();
        }
        jumpCoolDown = Math.max(0, jumpCoolDown - 1);

        // handle left and right movement, no velocity change, just direct position change

        int xTranslation = 0;

        if (leftPressed) {
            xTranslation -= 2;
        }
        if (rightPressed) {
            xTranslation += 2;
        }

        // apply YVelocity, if possible (check from status.canMove[Up/Down]())
        // pass in current player Rectangle2D, new vertical and horizontal position,
        Rectangle2D rect = bounds();
        PlayerStatus status = map.getNewPlayerStatus(bounds(), yVelocity, xTranslation);


        // if the yVelocity was positive and canMoveVertically() is false, then the player is on the ground
        // also if we can move vertically then apply the yVelocity
        if (status.canMoveVertically()) {
            position.y += yVelocity;
        }

        if (!status.canMoveVertically()) {
            yVelocity = 0;
        }


        if (status.canMoveHorizontally()) {
            position.x += xTranslation;
        }
    }

    public void render(Graphics2D graphics2d, Player otherPlayer) {
        graphics2d.drawImage(image, position.x, position.y, null);
        // debug, render bounds as an outline instead:
//        graphics2d.draw(bounds());

            // render the gun
        // find the angle between the two players
        int angle = (int) Math.toDegrees(Math.atan2(otherPlayer.position.y - position.y, otherPlayer.position.x - position.x));
        gun.render(graphics2d, angle, new Point(position.x + image.getWidth() / 2, position.y + image.getHeight() / 2));

    }


}
