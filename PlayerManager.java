import weapons.Bullet;

import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;
public class PlayerManager {
    private static PlayerManager playerInstance;


    private PlayerManager() {
        this.players = new ArrayList<>();
    }

    // Create the instance only if it does not exist. This is called "lazy initialization".
    public static PlayerManager getInstance() {
        if (playerInstance == null) {
           playerInstance = new PlayerManager();
        }
        return playerInstance;
    }


    private final List<Player> players;


    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void triggerKeyPresses(int keycode) {
        for(Player player: players) {
            player.handleKeyPress(keycode);
        }
    }

    public void triggerKeyReleases(int keycode) {
        for(Player player: players) {
            player.handleKeyRelease(keycode);
        }
    }

    public Player getOtherPlayer(int index) {
        return players.get(index);
    }



    public double getAngleToClosestPlayer(Player player) {
        double closestDistance = Double.MAX_VALUE;
        Player closestPlayer = null;

        Rectangle2D playerRect = player.bounds();
        double playerCenterX = playerRect.getCenterX();
        double playerCenterY = playerRect.getCenterY();

        for (Player otherPlayer : players) {
            if (otherPlayer == player) continue; // Skip the player itself

            Rectangle2D otherPlayerRect = otherPlayer.bounds();
            double otherPlayerCenterX = otherPlayerRect.getCenterX();
            double otherPlayerCenterY = otherPlayerRect.getCenterY();

            double distance = Math.hypot(playerCenterX - otherPlayerCenterX, playerCenterY - otherPlayerCenterY);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestPlayer = otherPlayer;
            }
        }

        if (closestPlayer == null) {
            throw new IllegalStateException("No other players in the game");
        }
        double angle = Math.atan2(closestPlayer.bounds().getCenterY() - player.bounds().getCenterY(), closestPlayer.bounds().getCenterX() - player.bounds().getCenterX());
        return angle;
    }


    // bullet collision, needs a list of bullets, check if it intersects with any player, if it does lower the health of that player
    public ArrayList<Integer> bulletCollision(ArrayList<Bullet> bullets, Player currentPlayer) {
        ArrayList<Integer> indexesToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            for (Player player : players) {
                if (bullet.bounds().intersects(player.bounds()) && player != currentPlayer) {
                    player.lowerHealth(bullet.getDamage());
                    // remove bullet by adding it to a list of indexes to remove
                    indexesToRemove.add(bullets.indexOf(bullet));
                }
            }
        }
        return indexesToRemove;
    }



}

