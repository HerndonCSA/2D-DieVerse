import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class KeyManager implements KeyListener {
    // boolean values for each key
    private Player playerOne;
    private Player playerTwo;


    public KeyManager(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        playerOne.handleKeyPress(e.getKeyCode(), playerTwo);
        playerTwo.handleKeyPress(e.getKeyCode(), playerTwo);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        playerOne.handleKeyRelease(e.getKeyCode(), playerTwo);
        playerTwo.handleKeyRelease(e.getKeyCode(), playerTwo);
    }
}