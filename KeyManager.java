import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class KeyManager implements KeyListener{
    // boolean values for each key
    private PlayerManager playerManager = PlayerManager.getInstance();


    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        playerManager.triggerKeyPresses(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        playerManager.triggerKeyReleases(e.getKeyCode());
    }
}