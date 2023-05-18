import mapobjects.MapObject;
import mapobjects.Type;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class Map {
    private final List<MapObject> objects;

    public Map(List<MapObject> objects) {
        this.objects = objects;
    }

    public void tick() {
        objects.forEach(MapObject::tick);
    }

    public void render(Graphics2D graphics) {
        objects.forEach(object -> {
            object.render(graphics);
        });
    }

    // pass in current player Rectangle2D, new vertical and horizontal position,
    // return a PlayerStatus object, it contains canMoveVertical and canMoveHorizontal as well as status effects of the new position
    // the name of this method is called getNewPlayerStatus


        public static Rectangle2D translateRectangleY(Rectangle2D rect, int yTranslation) {
            // Create a new Rectangle2D with the same dimensions, but translated along the y-axis
            // print out before and after ( this method is used for the player )

            return new Rectangle2D.Double(rect.getX(), rect.getY() + yTranslation, rect.getWidth(), rect.getHeight());
        }

        public static Rectangle2D translateRectangleX(Rectangle2D rect, int xTranslation) {
            // Create a new Rectangle2D with the same dimensions, but translated along the x-axis

            return new Rectangle2D.Double(rect.getX() + xTranslation, rect.getY(), rect.getWidth(), rect.getHeight());
        }


    public PlayerStatus getNewPlayerStatus(Rectangle2D currentRect, int verticalMov, int horizontalMove) {
        boolean canMoveVertically = true;
        boolean canMoveHorizontally = true;
        int damage = 0;
        boolean isSlowed = false;
        Rectangle2D newRect;

        // check if the player can move vertically
        newRect = translateRectangleY(currentRect, verticalMov);
        if(!canMove(newRect)) {
            canMoveVertically = false;
        }

        // check if the player can move horizontally
        newRect = translateRectangleX(currentRect, horizontalMove);
        if(!canMove(newRect)) {
            canMoveHorizontally = false;
        }

        return new PlayerStatus(canMoveVertically, canMoveHorizontally, damage, isSlowed);

    }

    // check if the player can move vertically
    private boolean canMove(Rectangle2D rect) {
        for (MapObject object : objects) {
            if(object.getType() == Type.TRANSPARENT || object.getType() == Type.BACKGROUND) continue;
            if (object.bounds().intersects(rect)) {
//                 object.debug();
                return false;
            }
        }
        return true;
    }

}